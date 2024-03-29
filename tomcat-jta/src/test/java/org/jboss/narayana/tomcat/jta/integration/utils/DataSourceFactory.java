/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.narayana.tomcat.jta.integration.utils;

import java.util.Properties;

import org.apache.tomcat.dbcp.dbcp2.managed.BasicManagedDataSource;
import org.jboss.narayana.tomcat.jta.integration.utils.internal.DatabaseProvider;
import org.jboss.narayana.tomcat.jta.integration.utils.internal.PoolingDataSourceWrapperImpl;

/**
 * Creates instances of {@link PoolingDataSourceWrapper} that can be used for testing purposes.
 *
 * Example usage:
 * <code>
 * Properties driverProperties = new Properties();
 * driverProperties.put("user", "sa");
 * driverProperties.put("password", "");
 * driverProperties.put("url", "jdbc:h2:mem:jbpm-db;MVCC=true");
 * driverProperties.put("driverClassName", "org.h2.Driver");
 * driverProperties.put("className", "org.h2.jdbcx.JdbcDataSource");
 *
 * PoolingDataSourceWrapper pds = DataSourceFactory.setupPoolingDataSource("jdbc/jbpm-ds", driverProperties);
 * </code>
 *
 * Please note that a JNDI implementation must be on classpath together with jndi.properties
 * that specifies the InitialContextFactory class to be used.
 */
public final class DataSourceFactory {

    private DataSourceFactory() {
        throw new UnsupportedOperationException(DataSourceFactory.class.getSimpleName() + " should not be instantiated.");
    }

    /**
     * Creates a new instance of {@link PoolingDataSourceWrapper}.
     * @param datasourceName data source JNDI name
     * @param driverProperties properties that should be passed to JDBC driver
     * @return a new PoolingDataSourceWrapper instance
     */
    public static PoolingDataSourceWrapper setupPoolingDataSource(String datasourceName,
                                                                  Properties driverProperties) {
        return setupPoolingDataSource(datasourceName, driverProperties, new Properties());
    }

    /**
     * Creates a new instance of {@link PoolingDataSourceWrapper}.
     * @param datasourceName data source JNDI name
     * @param driverProperties properties that should be passed to JDBC driver
     * @param poolingProperties properties of {@link BasicManagedDataSource} pooling data source
     * @return a new PoolingDataSourceWrapper instance
     */
    public static PoolingDataSourceWrapper setupPoolingDataSource(String datasourceName,
                                                                  Properties driverProperties,
                                                                  Properties poolingProperties) {
        Properties sanitizedDriverProperties = new Properties();
        String driverClass = driverProperties.getProperty("driverClassName");
        DatabaseProvider databaseProvider = DatabaseProvider.fromDriverClassName(driverClass);
        for (String propertyName : new String[]{"user", "password"}) {
            sanitizedDriverProperties.put(propertyName, driverProperties.getProperty(propertyName));
        }

        if (databaseProvider == DatabaseProvider.H2) {
            for (String propertyName : new String[]{"url", "driverClassName"}) {
                sanitizedDriverProperties.put(propertyName, driverProperties.getProperty(propertyName));
            }
        } else {
            if (databaseProvider == DatabaseProvider.ORACLE) {
                sanitizedDriverProperties.put("driverType", "thin");
                sanitizedDriverProperties.put("URL", driverProperties.getProperty("url"));
            } else if (databaseProvider == DatabaseProvider.DB2) {
                for (String propertyName : new String[]{"databaseName", "serverName", "portNumber", "url"}) {
                    sanitizedDriverProperties.put(propertyName, driverProperties.getProperty(propertyName));
                }
                sanitizedDriverProperties.put("driverType", "4");
                sanitizedDriverProperties.put("currentSchema", driverProperties.getProperty("defaultSchema"));
                sanitizedDriverProperties.put("ResultSetHoldability", "1");
                sanitizedDriverProperties.put("DowngradeHoldCursorsUnderXa", "true");
            } else if (databaseProvider == DatabaseProvider.MSSQL) {
                for (String propertyName : new String[]{"serverName", "portNumber", "databaseName"}) {
                    sanitizedDriverProperties.put(propertyName, driverProperties.getProperty(propertyName));
                }
                sanitizedDriverProperties.put("URL", driverProperties.getProperty("url"));
            } else if (databaseProvider == DatabaseProvider.MYSQL
                    || databaseProvider == DatabaseProvider.MARIADB
                    || databaseProvider == DatabaseProvider.SYBASE
                    || databaseProvider == DatabaseProvider.POSTGRES
                    || databaseProvider == DatabaseProvider.POSTGRES_PLUS) {
                for (String propertyName : new String[]{"databaseName", "portNumber", "serverName", "url"}) {
                    sanitizedDriverProperties.put(propertyName, driverProperties.getProperty(propertyName));
                }
            } else {
                throw new RuntimeException("Unknown driver class: " + driverClass);
            }
        }

        String xaDataSourceClassName = driverProperties.getProperty("className");
        return new PoolingDataSourceWrapperImpl(datasourceName, xaDataSourceClassName, sanitizedDriverProperties, poolingProperties);
    }
}
