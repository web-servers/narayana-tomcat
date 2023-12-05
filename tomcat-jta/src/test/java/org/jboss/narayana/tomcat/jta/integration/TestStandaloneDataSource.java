/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.narayana.tomcat.jta.integration;

import java.sql.SQLException;
import java.util.Properties;

import org.jboss.narayana.tomcat.jta.integration.utils.DataSourceFactory;
import org.jboss.narayana.tomcat.jta.integration.utils.PoolingDataSourceWrapper;
import org.junit.Test;

public class TestStandaloneDataSource extends AbstractUnitCase {

    @Test
    public void testStandaloneDataSourceConnect() {
        Properties driverProperties = new Properties();
        driverProperties.put("user", "sa");
        driverProperties.put("password", "");
        driverProperties.put("url", "jdbc:h2:mem:jbpm-db;MVCC=true");
        driverProperties.put("driverClassName", "org.h2.Driver");
        driverProperties.put("className", "org.h2.jdbcx.JdbcDataSource");

        PoolingDataSourceWrapper pds = DataSourceFactory.setupPoolingDataSource("custom-ds", driverProperties);
        try {
            pds.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            pds.close();
        }
    }
}
