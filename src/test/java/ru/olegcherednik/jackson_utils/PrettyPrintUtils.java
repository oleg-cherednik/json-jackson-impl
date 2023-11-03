/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.jackson_utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class PrettyPrintUtils {

    public static final Pattern WIN_LINE_SEPARATOR = Pattern.compile("\r\n");
    public static final String UNIX_LINE_SEPARATOR = "\n";

    public static String withUnixLineSeparator(String str) {
        return WIN_LINE_SEPARATOR.matcher(str).replaceAll(UNIX_LINE_SEPARATOR);
    }

}
