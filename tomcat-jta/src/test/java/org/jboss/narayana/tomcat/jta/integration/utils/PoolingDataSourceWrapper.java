/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.narayana.tomcat.jta.integration.utils;

import javax.sql.DataSource;

/**
 * Wrapper for an XA data source with pooling capabilities.
 */
public interface PoolingDataSourceWrapper extends DataSource {

    /**
     * Closes the data source; as a result, the data source will stop providing connections and will be unregistered
     * from JNDI context.
     */
    void close();

    /**
     * @return the data source JNDI name
     */
    String getUniqueName();

    /**
     * @return name of underlying XADataSource class
     */
    String getClassName();
}
