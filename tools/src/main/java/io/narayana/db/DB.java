/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package io.narayana.db;

import java.util.Properties;

/**
 * @author <a href="mailto:karm@redhat.com">Michal Karm Babacek</a>
 */
public class DB {
    public final String dsType;
    public final String dsUsername;
    public final String dsUser;
    public final String dsPassword;
    public final String dsDbName;
    public final String dsDbPort;
    public final String dsDbHostname;
    public final String dsUrl;
    public final String dsLoginTimeout;
    public final String dsFactory;
    public final String dsDriverClassName;
    public final String dsSchema;
    public final String tdsType;
    public final String tdsUrl;
    public final String tdsDriverClassName;
    public final String dbDriverArtifact;
    public final String heartBeatStatement;
    public final Properties allocationProperties;

    public static class Builder {
        private String dsType;
        private String dsUsername;
        private String dsUser;
        private String dsPassword;
        private String dsDbName;
        private String dsDbPort;
        private String dsDbHostname;
        private String dsUrl;
        private String dsLoginTimeout;
        private String dsFactory;
        private String dsDriverClassName;
        public  String dsSchema;
        private String tdsType;
        private String tdsUrl;
        private String tdsDriverClassName;
        private String dbDriverArtifact;
        private String heartBeatStatement;
        private Properties allocationProperties;

        Builder dsType(String dsType) {
            this.dsType = dsType;
            return this;
        }

        Builder dsUsername(String dsUsername) {
            this.dsUsername = dsUsername;
            return this;
        }

        Builder dsUser(String dsUser) {
            this.dsUser = dsUser;
            return this;
        }

        Builder dsPassword(String dsPassword) {
            this.dsPassword = dsPassword;
            return this;
        }

        Builder dsDbName(String dsDbName) {
            this.dsDbName = dsDbName;
            return this;
        }

        Builder dsDbPort(String dsDbPort) {
            this.dsDbPort = dsDbPort;
            return this;
        }

        Builder dsDbHostname(String dsDbHostname) {
            this.dsDbHostname = dsDbHostname;
            return this;
        }

        Builder dsUrl(String dsUrl) {
            this.dsUrl = dsUrl;
            return this;
        }

        Builder dsLoginTimeout(String dsLoginTimeout) {
            this.dsLoginTimeout = dsLoginTimeout;
            return this;
        }

        Builder dsFactory(String dsFactory) {
            this.dsFactory = dsFactory;
            return this;
        }

        Builder dsDriverClassName(String dsDriverClassName) {
            this.dsDriverClassName = dsDriverClassName;
            return this;
        }

        Builder dsSchema(String dsSchema) {
            this.dsSchema = dsSchema;
            return this;
        }

        Builder tdsType(String tdsType) {
            this.tdsType = tdsType;
            return this;
        }

        Builder tdsUrl(String tdsUrl) {
            this.tdsUrl = tdsUrl;
            return this;
        }

        Builder tdsDriverClassName(String tdsDriverClassName) {
            this.tdsDriverClassName = tdsDriverClassName;
            return this;
        }

        Builder dbDriverArtifact(String dbDriverArtifact) {
            this.dbDriverArtifact = dbDriverArtifact;
            return this;
        }

        Builder heartBeatStatement(String heartBeatStatement) {
            this.heartBeatStatement = heartBeatStatement;
            return this;
        }

        Builder allocationProperties(Properties allocationProperties) {
            this.allocationProperties = allocationProperties;
            return this;
        }

        DB build() {
            return new DB(this);
        }
    }

    private DB(Builder builder) {
        this.dsType = builder.dsType;
        this.dsUsername = builder.dsUsername;
        this.dsUser = builder.dsUser;
        this.dsPassword = builder.dsPassword;
        this.dsDbName = builder.dsDbName;
        this.dsDbPort = builder.dsDbPort;
        this.dsDbHostname = builder.dsDbHostname;
        this.dsUrl = builder.dsUrl;
        this.dsLoginTimeout = builder.dsLoginTimeout;
        this.dsFactory = builder.dsFactory;
        this.dsDriverClassName = builder.dsDriverClassName;
        this.dsSchema = builder.dsSchema;
        this.tdsType = builder.tdsType;
        this.tdsUrl = builder.tdsUrl;
        this.tdsDriverClassName = builder.tdsDriverClassName;
        this.dbDriverArtifact = builder.dbDriverArtifact;
        this.heartBeatStatement = builder.heartBeatStatement;
        this.allocationProperties = builder.allocationProperties;
    }
}
