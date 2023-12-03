package ru.olegcherednik.json.jacksonutils.datetime;

import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonHelper;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.api.ZoneModifier;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@Test
public class DateTimeCombinationTest {

    public void shouldSerializeDateWithAllTypesWithDefaultDateTimeFormat() {
        DateTimeFormatter df1 = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        DateTimeFormatter df3 = df2.withZone(ZoneOffset.UTC);
        JsonHelper.useSettings(JsonSettings.builder()
                                           .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
                                           .instantFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
//                                           .localDateFormatter(DateTimeFormatter.ISO_DATE)
//                                           .localTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"))
//                                           .dateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
//                                           .offsetTimeFormatter(DateTimeFormatter.ISO_OFFSET_TIME)
//                                           .offsetDateTimeFormatter(df2.withZone(ZoneId.systemDefault()))
//                                           .zonedDateTimeFormatter(df2)
                                           .build());
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        Data data = new Data(zonedDateTime);

        System.out.println(Json.prettyPrint().writeValue(data));
    }

    private static final class Data {

        private final Instant instant;
        private final LocalTime localTime;
        private final LocalDate localDate;
        private final LocalDateTime localDateTime;
        private final OffsetTime offsetTime;
        private final OffsetDateTime offsetDateTime;
        private final ZonedDateTime zonedDateTime;
        private final Date date;

        public Data(ZonedDateTime zonedDateTime) {
            instant = zonedDateTime.toInstant();
            localTime = zonedDateTime.toLocalTime();
            localDate = zonedDateTime.toLocalDate();
            localDateTime = zonedDateTime.toLocalDateTime();
            offsetDateTime = zonedDateTime.toOffsetDateTime();
            offsetTime = offsetDateTime.toOffsetTime();
            this.zonedDateTime = zonedDateTime;
            date = Date.from(instant);
        }

    }

    /*
 .addSerializer(Date.class, date)
     */
}
