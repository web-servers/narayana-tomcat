/*
   Copyright The Narayana Authors
   SPDX-License-Identifier: Apache-2.0
 */
package io.narayana.db;

abstract class DefaultAllocator extends Allocator {

    @Override
    public DB allocateDB() {
        return allocateDB(0);
    }

    @Override
    public boolean deallocateDB(final DB db) {
        // Intentionally nothing
        return true;
    }

    @Override
    public boolean reallocateDB(final int expiryMinutes, final DB db) {
        // Intentionally nothing
        return true;
    }

    @Override
    public boolean reallocateDB(final DB db) {
        // Intentionally nothing
        return true;
    }

    @Override
    public boolean cleanDB(final DB db) {
        // Intentionally nothing
        return true;
    }
}
