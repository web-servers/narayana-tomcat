/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.narayana.tomcat.jta;

import com.arjuna.ats.jta.common.jtaPropertyManager;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * Object factory to create instances of {@link javax.transaction.TransactionManager}.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionManagerFactory implements ObjectFactory {

    /**
     * User internal factory method to instantiate new or reuse existing instance of
     * {@link javax.transaction.TransactionManager}.
     *
     * @param obj
     * @param name
     * @param nameCtx
     * @param environment
     * @return instance of {@link javax.transaction.TransactionManager} or {@code null} if instantiation has failed.
     */
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) {
        return jtaPropertyManager.getJTAEnvironmentBean().getTransactionManager();
    }

}
