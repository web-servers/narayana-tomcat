/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.narayana.tomcat.jta.integration;

import org.jboss.narayana.tomcat.jta.NarayanaJtaServletContextListener;
import org.junit.After;
import org.junit.Before;

public class AbstractUnitCase {

    private NarayanaJtaServletContextListener listener = new NarayanaJtaServletContextListener();

    @Before
    public void setUp() {
        listener.contextInitialized(null);
    }

    @After
    public void tearDown() {
        listener.contextDestroyed(null);
    }
}
