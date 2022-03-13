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
package ru.olegcherednik.jackson.utils.serializers;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import ru.olegcherednik.jackson.utils.JacksonObjectMapperBuilder;

import java.text.DateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 13.03.2022
 */
public class ZoneIdDateSerializer extends DateSerializer {

    private static final long serialVersionUID = -8039490528924955839L;

    public ZoneIdDateSerializer(UnaryOperator<ZoneId> zoneModifier) {
        super(null, dateFormat(zoneModifier));
    }

    protected static DateFormat dateFormat(UnaryOperator<ZoneId> zoneModifier) {
        zoneModifier = Optional.ofNullable(zoneModifier).orElse(JacksonObjectMapperBuilder.ZONE_MODIFIER_USE_ORIGINAL);
        return new ZoneStdDateFormat(TimeZone.getTimeZone(zoneModifier.apply(ZoneOffset.UTC)), Locale.ENGLISH);
    }

    public static class ZoneStdDateFormat extends StdDateFormat {

        private static final long serialVersionUID = 598417271543587336L;

        public ZoneStdDateFormat(TimeZone timeZone, Locale locale) {
            super(timeZone, locale, null, true);
        }

        protected String format(TimeZone tz, Locale loc, Date date) {
            StringBuffer buf = new StringBuffer();
            super._format(tz, loc, date, buf);
            return buf.toString();
        }

        @Override
        protected void _format(TimeZone tz, Locale loc, Date date, StringBuffer buffer) {
            String str = format(tz, loc, date);

            if (str.endsWith("+00:00")) {
                int plusPos = str.lastIndexOf('+');
                buffer.append(str, 0, plusPos);
                buffer.append('Z');
            } else
                buffer.append(str);
        }

        @Override
        public StdDateFormat clone() {
            return new ZoneStdDateFormat(_timezone, _locale);
        }

    }

}
