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


/**
 * A set of utilities around Logging.
 *
 *
 */
public class Loggers {

    public static final String SPACE = " ";

    public static AnsjLogger getLogger(AnsjLogger parentLogger, String s) {
        return AnsjLoggerFactory.getLogger(parentLogger.getPrefix(), getLoggerName(parentLogger.getName() + s));
    }

    public static AnsjLogger getLogger(String s) {
        return AnsjLoggerFactory.getLogger(getLoggerName(s));
    }

    public static AnsjLogger getLogger(Class clazz) {
        return AnsjLoggerFactory.getLogger(getLoggerName(buildClassLoggerName(clazz)));
    }

    public static AnsjLogger getLogger(Class clazz, String... prefixes) {
        return getLogger(buildClassLoggerName(clazz), prefixes);
    }

    public static AnsjLogger getLogger(String name, String... prefixes) {
        String prefix = null;
        if (prefixes != null && prefixes.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String prefixX : prefixes) {
                if (prefixX != null) {
                    if (prefixX.equals(SPACE)) {
                        sb.append(" ");
                    } else {
                        sb.append("[").append(prefixX).append("]");
                    }
                }
            }
            if (sb.length() > 0) {
                sb.append(" ");
                prefix = sb.toString();
            }
        }
        return AnsjLoggerFactory.getLogger(prefix, getLoggerName(name));
    }

    private static String buildClassLoggerName(Class clazz) {
        String name = clazz.getName();
//        if (name.startsWith("org.elasticsearch.")) {
//            name = Classes.getPackageName(clazz);
//        }
        return name;
    }

    private static String getLoggerName(String name) {
        return name;
    }
}
