package org.jboss.narayana.tomcat.jta.integration;

import java.sql.SQLException;
import java.util.Properties;

import org.jboss.narayana.tomcat.jta.NarayanaJtaServletContextListener;
import org.jboss.narayana.tomcat.jta.integration.utils.DataSourceFactory;
import org.jboss.narayana.tomcat.jta.integration.utils.PoolingDataSourceWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStandaloneDataSource {
    private NarayanaJtaServletContextListener listener = new NarayanaJtaServletContextListener();

    @Before
    public void setUp() {
        listener.contextInitialized(null);
    }

    @After
    public void tearDown() {
        listener.contextDestroyed(null);
    }

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
