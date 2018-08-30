/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package io.narayana.db;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Start databases according to the profile. The goal is to be able to work both with platform
 * independent simple H2 example, locally started databases in containers, e.g. Postgres, and
 * with remote databases configured via REST API, e.g. MS SQL, Oracle, Maria and more.
 *
 * @author <a href="mailto:karm@redhat.com">Michal Karm Babacek</a>
 */
public abstract class Allocator {
    private static final Logger LOGGER = Logger.getLogger(Allocator.class.getName());

    private static Allocator dbAllocator;

    public static Allocator getInstance() throws IOException {
        if (dbAllocator != null) {
            return dbAllocator;
        }
        final String mode = getProp("test.db.type");
        if ("pgsql".equals(mode)) {
            dbAllocator = new CIPostgreAllocator();
        } else if ("h2".equals(mode)) {
            dbAllocator = new H2Allocator();
        } else if ("container".equals(mode)) {
            dbAllocator = new PostgreContainerAllocator();
        } else if ("dballocator".equals(mode)) {
            dbAllocator = new DBAllocator();
        } else {
            throw new IllegalArgumentException("Unknown operation mode, expected pgsql or h2 or container or dballocator but it was: " + mode);
        }

        return dbAllocator;
    }

    public abstract DB allocateDB(final int expiryMinutes);

    public abstract DB allocateDB();

    public abstract boolean deallocateDB(final DB db);

    public abstract boolean reallocateDB(final int expiryMinutes, final DB db);

    public abstract boolean reallocateDB(final DB db);

    public abstract boolean cleanDB(final DB db);

    static boolean fileOK(final int minSize, final File file) {
        return file != null && file.exists() && FileUtils.sizeOf(file) >= minSize;
    }

    static boolean waitForTcp(final String host, final int port, final int connTimeoutMs, final long overallTimeoutMs) {
        final long timestamp = System.currentTimeMillis();
        final SocketAddress sa = new java.net.InetSocketAddress(host, port);
        while (System.currentTimeMillis() - timestamp < overallTimeoutMs) {
            try (final Socket socket = new Socket()) {
                socket.connect(sa, connTimeoutMs);
                socket.shutdownInput();
                socket.shutdownOutput();
                return true;
            } catch (IOException e) {
                LOGGER.fine(String.format("waitForTcp: %s:%d", host, port));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.FINE, "waitForTcp interrupted.", e);
            }
        }
        // still port not ready, failing
        return false;
    }

    /**
     * Loads proper driver dynamically.
     * Credit: http://www.kfu.com/%7Ensayer/Java/dyn-jdbc.html
     * <p>
     * Executes a simple SELECT 1 statement to verify the DB actually does something.
     *
     * @param db, used as:
     *            dbUrl            JDBC connection URL
     *            user             DB user
     *            pass             DB password
     *            driverClass      e.g. org.postgresql.Driver
     *            pathToDriverJar  Either a downloaded driver jar or a one from ShrinkWrap Maven resolver
     *            overallTimeoutMs How long do we keep trying with 1000ms pause in between
     * @return true on success
     * @throws ClassNotFoundException Fix the driver class name
     * @throws MalformedURLException  Fix the driver jar path
     */
    public static boolean executeTestStatement(final DB db, final String pathToDriverJar)
            throws ClassNotFoundException, MalformedURLException, IllegalAccessException, InstantiationException, SQLException {

        final String overallTimeout = getProp("db.timeout.waiting.for.heartbeat.statement");
        final long overallTimeoutMs = Long.parseLong(overallTimeout);
        if (overallTimeoutMs > TimeUnit.MINUTES.toMillis(5) || overallTimeoutMs < 100) {
            throw new IllegalArgumentException("db.timeout.waiting.for.heartbeat.statement out of expected range [100, 5*60*1000] ms.");
        }
        if (StringUtils.isBlank(db.heartBeatStatement)) {
            throw new IllegalArgumentException("db.heartBeatStatement on DB object must be set," +
                    "check your *Allocator class and db.timeout.heartbeat.statement property.");
        }

        final long timestamp = System.currentTimeMillis();
        final URLClassLoader ucl = new URLClassLoader(new URL[]{new URL(String.format("jar:file:%s!/", pathToDriverJar))});
        final Driver driver = (Driver) Class.forName(db.dsDriverClassName, true, ucl).newInstance();

        DriverManager.registerDriver(new Driver() {
            @Override
            public Connection connect(String url, Properties info) throws SQLException {
                return driver.connect(url, info);
            }

            @Override
            public boolean acceptsURL(String url) throws SQLException {
                return driver.acceptsURL(url);
            }

            @Override
            public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
                return driver.getPropertyInfo(url, info);
            }

            @Override
            public int getMajorVersion() {
                return driver.getMajorVersion();
            }

            @Override
            public int getMinorVersion() {
                return driver.getMinorVersion();
            }

            @Override
            public boolean jdbcCompliant() {
                return driver.jdbcCompliant();
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return driver.getParentLogger();
            }
        });

        while (System.currentTimeMillis() - timestamp < overallTimeoutMs) {
            try (
                    final Connection conn = DriverManager.getConnection(db.dsUrl, db.dsUser, db.dsPassword);
                    final Statement stmt = conn.createStatement();
                    final ResultSet rs = stmt.executeQuery(db.heartBeatStatement)
            ) {
                if (rs.next()) {
                    return true;
                }
            } catch (Exception e) {
                long remainingTime = overallTimeoutMs - (System.currentTimeMillis() - timestamp);
                LOGGER.log(Level.SEVERE, String.format("DB not ready to answer the test statement: %s. Remaining time: %d, approx %d attempts.",
                        db.heartBeatStatement, remainingTime, remainingTime / 1000), e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.FINE, "Waiting for DB to execute test statement was interrupted.", e);
            }
        }

        return false;
    }

    public static String getProp(final String prop) {
        final String v = System.getProperty(prop);
        if (StringUtils.isEmpty(v)) {
            throw new IllegalArgumentException(prop + " must not be empty.");
        }
        return v;
    }
}
