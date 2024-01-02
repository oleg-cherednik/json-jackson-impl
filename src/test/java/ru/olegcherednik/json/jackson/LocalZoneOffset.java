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

package ru.olegcherednik.json.jackson;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * @author Oleg Cherednik
 * @since 02.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocalZoneOffset {

    public static final ZoneOffset UTC = ZoneOffset.UTC.getRules().getOffset(Instant.now());
    public static final ZoneOffset ASIA_SINGAPORE = LocalZoneId.ASIA_SINGAPORE.getRules().getOffset(Instant.now());
    public static final ZoneOffset AUSTRALIA_SYDNEY = LocalZoneId.AUSTRALIA_SYDNEY.getRules().getOffset(Instant.now());

}
