/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.narayana.tomcat.jta.integration.app;

import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TestXAResource implements XAResource, XAResourceRecoveryHelper {

    private static final List<String> METHOD_CALLS = new LinkedList<>();

    private Xid xid;

    public static List<String> getMethodCalls() {
        return Collections.unmodifiableList(METHOD_CALLS);
    }

    public static void reset() {
        METHOD_CALLS.clear();
    }

    @Override
    public void start(Xid xid, int i) throws XAException {
        METHOD_CALLS.add("start");
        this.xid = xid;
    }

    @Override
    public void commit(Xid xid, boolean b) throws XAException {
        METHOD_CALLS.add("commit");
        this.xid = null;
    }

    @Override
    public void rollback(Xid xid) throws XAException {
        METHOD_CALLS.add("rollback");
        this.xid = null;
    }

    @Override
    public void end(Xid xid, int i) throws XAException {
        METHOD_CALLS.add("end");
    }

    @Override
    public void forget(Xid xid) throws XAException {
        METHOD_CALLS.add("forget");
        this.xid = null;
    }

    @Override
    public int getTransactionTimeout() throws XAException {
        return 0;
    }

    @Override
    public boolean isSameRM(XAResource xaResource) throws XAException {
        return xaResource instanceof TestXAResource;
    }

    @Override
    public int prepare(Xid xid) throws XAException {
        METHOD_CALLS.add("prepare");
        return 0;
    }

    @Override
    public Xid[] recover(int i) throws XAException {
        if (xid == null) {
            return new Xid[0];
        }

        return new Xid[]{xid};
    }

    @Override
    public boolean setTransactionTimeout(int i) throws XAException {
        return true;
    }

    @Override
    public boolean initialise(String p) throws Exception {
        return true;
    }

    @Override
    public XAResource[] getXAResources() {
        return new XAResource[]{this};
    }
}
