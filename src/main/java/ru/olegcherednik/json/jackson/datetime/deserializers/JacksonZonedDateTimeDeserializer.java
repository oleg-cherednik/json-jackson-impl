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

package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonZonedDateTimeDeserializer extends InstantDeserializer<ZonedDateTime> {

    private static final long serialVersionUID = -5671981244499036058L;

    public JacksonZonedDateTimeDeserializer(DateTimeFormatter df) {
        super(InstantDeserializer.ZONED_DATE_TIME, df);
    }

    protected JacksonZonedDateTimeDeserializer(JacksonZonedDateTimeDeserializer base, DateTimeFormatter df) {
        super(base, df);
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    protected JacksonZonedDateTimeDeserializer withDateFormat(DateTimeFormatter df) {
        return df == _formatter ? this : new JacksonZonedDateTimeDeserializer(this, df);
    }


}
