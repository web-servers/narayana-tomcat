/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package io.narayana.db;

/**
 * @author <a href="mailto:karm@redhat.com">Michal Karm Babacek</a>
 */
public class H2Allocator extends DefaultAllocator {

    H2Allocator() {
        // Use getInstance
    }

    @Override
    public DB allocateDB(final int expiryMinutes) {
        final String versionComH2database = getProp("version.com.h2database");
        return new DB.Builder()
                .dsType("org.h2.jdbcx.JdbcDataSource")
                .dsUsername("sa")
                .dsUser("sa")
                .dsPassword("sa")
                .dsDbName("testdb")
                .dsUrl("jdbc:h2:mem:testdb;TRACE_LEVEL_FILE=3;TRACE_LEVEL_SYSTEM_OUT=3")
                .dsLoginTimeout("0")
                .dsFactory("org.h2.jdbcx.JdbcDataSourceFactory")
                .tdsType("javax.sql.XADataSource")
                .dbDriverArtifact("com.h2database:h2:" + versionComH2database)
                .build();
    }
}
