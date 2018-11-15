package org.jboss.narayana.tomcat.jta.integration;

import org.jboss.narayana.tomcat.jta.NarayanaJtaServletContextListener;
import org.junit.After;
import org.junit.Before;

/**
 * Please note that running multiple test cases without the {@link TestSuite} will result in {@link IllegalStateException}
 * thrown by {@link com.arjuna.ats.arjuna.recovery.RecoveryManager#terminate},
 * as the {@link com.arjuna.ats.arjuna.recovery.RecoveryManager} cannot be instantiated and destroyed multiple times
 * in the same JVM.
 */
public class AbstractUnitCase {

    private NarayanaJtaServletContextListener listener;

    @Before
    public void setUp() {
        if (!TestSuite.isRunningInTestSuite()) {
            listener = new NarayanaJtaServletContextListener();
            listener.contextInitialized(null);
        }
    }

    @After
    public void tearDown() {
        if (!TestSuite.isRunningInTestSuite()) {
            listener.contextDestroyed(null);
        }
    }
}
