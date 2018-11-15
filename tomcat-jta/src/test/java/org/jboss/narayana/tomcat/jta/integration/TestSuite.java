package org.jboss.narayana.tomcat.jta.integration;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.narayana.tomcat.jta.NarayanaJtaServletContextListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestStandaloneDataSource.class, TestTransactionalDataSourceFactory.class})
public class TestSuite {
    private static final AtomicBoolean USING_TEST_SUITE = new AtomicBoolean();
    private static NarayanaJtaServletContextListener LISTENER = new NarayanaJtaServletContextListener();

    public static boolean isRunningInTestSuite() {
        return USING_TEST_SUITE.get();
    }

    @BeforeClass
    public static void setUp() {
        USING_TEST_SUITE.set(true);
        LISTENER.contextInitialized(null);
    }

    @AfterClass
    public static void tearDown() {
        LISTENER.contextDestroyed(null);
    }
}
