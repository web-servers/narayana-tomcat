/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package io.narayana.db;

/**
 * This is a remote database the TS does not have any control over.
 * The database is always present, always allocated.
 *
 * @author <a href="mailto:karm@redhat.com">Michal Karm Babacek</a>
 */
public class CIPostgreAllocator extends DefaultAllocator {

    CIPostgreAllocator() {
        // Use getInstance
    }

    @Override
    public DB allocateDB(final int expiryMinutes) {
        final String versionPostgreSQLDriver = getProp("version.postgresql");
        final String user = getProp("pgsql.user");
        final String password = getProp("pgsql.password");
        final String servername = getProp("pgsql.servername");
        final String portnumber = getProp("pgsql.portnumber");
        final int port = Integer.parseInt(portnumber);
        if (port > 65535 || port < 1025) {
            throw new IllegalArgumentException("pgsql.portnumber out of expected range [1025, 65535]");
        }
        final String databasename = getProp("pgsql.databasename");

        return new DB.Builder()
                .dsType("org.postgresql.xa.PGXADataSource")
                .dsUsername(user)
                .dsUser(user)
                .dsPassword(password)
                .dsDbName(databasename)
                .dsDbPort(String.valueOf(port))
                .dsDbHostname(servername)
                .dsUrl(String.format("jdbc:postgresql://%s:%d/%s", servername, port, databasename))
                .dsLoginTimeout("0")
                .dsFactory("org.postgresql.xa.PGXADataSourceFactory")
                .tdsType("javax.sql.XADataSource")
                .dbDriverArtifact("postgresql:postgresql:" + versionPostgreSQLDriver)
                .build();
    }
}
