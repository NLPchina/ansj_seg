/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ansj.util.logging;

import org.ansj.util.logging.jdk.JdkESLoggerFactory;
import org.ansj.util.logging.log4j.Log4jESLoggerFactory;
import org.ansj.util.logging.slf4j.Slf4jESLoggerFactory;

/**
 * Factory to get {@link AnsjLogger}s
 */
public abstract class AnsjLoggerFactory {

    private static volatile AnsjLoggerFactory defaultFactory = new JdkESLoggerFactory();

    static {
        try {
            Class<?> loggerClazz = Class.forName("org.apache.log4j.Logger");
            // below will throw a NoSuchMethod failure with using slf4j log4j bridge
            loggerClazz.getMethod("setLevel", Class.forName("org.apache.log4j.Level"));
            defaultFactory = new Log4jESLoggerFactory();
        } catch (Throwable e) {
            // no log4j
            try {
                Class.forName("org.slf4j.Logger");
                defaultFactory = new Slf4jESLoggerFactory();
            } catch (Throwable e1) {
                // no slf4j
            }
        }
    }

    /**
     * Changes the default factory.
     */
    public static void setDefaultFactory(AnsjLoggerFactory defaultFactory) {
        if (defaultFactory == null) {
            throw new NullPointerException("defaultFactory");
        }
        AnsjLoggerFactory.defaultFactory = defaultFactory;
    }


    public static AnsjLogger getLogger(String prefix, String name) {
        return defaultFactory.newInstance(prefix == null ? null : prefix.intern(), name.intern());
    }

    public static AnsjLogger getLogger(String name) {
        return defaultFactory.newInstance(name.intern());
    }

    public static AnsjLogger getRootLogger() {
        return defaultFactory.rootLogger();
    }

    public AnsjLogger newInstance(String name) {
        return newInstance(null, name);
    }

    protected abstract AnsjLogger rootLogger();

    protected abstract AnsjLogger newInstance(String prefix, String name);
}
