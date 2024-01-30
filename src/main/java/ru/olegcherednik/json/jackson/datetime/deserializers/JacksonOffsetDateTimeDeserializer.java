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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import ru.olegcherednik.json.api.JsonSettings;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonOffsetDateTimeDeserializer extends InstantDeserializer<OffsetDateTime> {

    private static final long serialVersionUID = -4594405514078469683L;

    public JacksonOffsetDateTimeDeserializer(DateTimeFormatter df) {
        super(InstantDeserializer.OFFSET_DATE_TIME, df);
    }

    protected JacksonOffsetDateTimeDeserializer(JacksonOffsetDateTimeDeserializer base, DateTimeFormatter df) {
        super(base, df);
    }

    protected JacksonOffsetDateTimeDeserializer(JacksonOffsetDateTimeDeserializer base,
                                                DateTimeFormatter df,
                                                Boolean leniency) {
        super(base, df, leniency);
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    protected JacksonOffsetDateTimeDeserializer withDateFormat(DateTimeFormatter df) {
        return df == _formatter ? this : new JacksonOffsetDateTimeDeserializer(this, df);
    }

    @Override
    protected JacksonOffsetDateTimeDeserializer withLeniency(Boolean leniency) {
        return new JacksonOffsetDateTimeDeserializer(this, _formatter, leniency);
    }

    @Override
    protected OffsetDateTime _fromLong(DeserializationContext context, long timestamp) {
        ZoneOffset zoneOffset = JsonSettings.SYSTEM_DEFAULT_ZONE_ID.getRules().getOffset(Instant.now());
        return super._fromLong(context, timestamp).withOffsetSameInstant(zoneOffset);
    }

    @Override
    protected OffsetDateTime _fromDecimal(DeserializationContext context, BigDecimal value) {
        ZoneOffset zoneOffset = JsonSettings.SYSTEM_DEFAULT_ZONE_ID.getRules().getOffset(Instant.now());
        return super._fromDecimal(context, value).withOffsetSameInstant(zoneOffset);
    }

}
