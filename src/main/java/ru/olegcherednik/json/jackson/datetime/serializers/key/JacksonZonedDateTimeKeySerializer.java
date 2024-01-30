/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.jackson.datetime.serializers.key;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.01.2024
 */
public class JacksonZonedDateTimeKeySerializer extends JacksonKeySerializer<ZonedDateTime> {

    private static final long serialVersionUID = -1513348976637073488L;

    public JacksonZonedDateTimeKeySerializer(DateTimeFormatter df) {
        super(ZonedDateTime.class, df, dt -> dt.toInstant().toEpochMilli(),
              ZonedDateTime::toEpochSecond, ZonedDateTime::getNano);
    }

}