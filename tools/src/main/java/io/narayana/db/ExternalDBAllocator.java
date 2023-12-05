/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package io.narayana.db;

public class ExternalDBAllocator extends DefaultAllocator {

    @Override
    public DB allocateDB(final int expiryMinutes) {
        final String user = System.getProperty("jdbc.username");
        return new DB.Builder()
                .dsType(System.getProperty("datasource.classname"))
                .dsUsername(user)
                .dsUser(user)
                .dsPassword(System.getProperty("jdbc.password"))
                .dsDbName(System.getProperty("jdbc.db.name"))
                .dsDbPort(System.getProperty("jdbc.db.port"))
                .dsDbHostname(System.getProperty("jdbc.db.server"))
                .dsUrl(System.getProperty("jdbc.url"))
                .dsLoginTimeout("0")
                .dsDriverClassName(System.getProperty("jdbc.driver.class"))
                .dsSchema(System.getProperty("jdbc.schema"))
                .tdsType("javax.sql.XADataSource")
                .dbDriverArtifact(System.getProperty("jdbc.driver.jar"))
                .build();
    }
}
