package ru.olegcherednik.utils.jackson;

import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
@Test
public class ZonedDateTimeJacksonUtilsTest {

    public void shouldRetrieveJsonUTCZoneWhenWriteZonedDateTimeDefaultSettings() {
        Map<String, ZonedDateTime> map = createData();
        String actual = JacksonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\"," +
                "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveJsonSingaporeZoneWhenWriteZonedDateTimeSingaporeZone() {
        ObjectMapperDecorator jsonUtils = JacksonHelper.createMapperDecorator(
                () -> new JacksonObjectMapperBuilder(ZoneId.of("Asia/Singapore")).get());

        Map<String, ZonedDateTime> map = createData();
        String actual = jsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"}");
    }

    public void shouldRetrieveJsonWithNoZoneChangeWhenWriteZonedDateTimeWithSameZone() {
        ObjectMapperDecorator jsonUtils = JacksonHelper.createMapperDecorator(
                () -> new JacksonObjectMapperBuilder(JacksonObjectMapperBuilder.ZONE_MODIFIER_USE_ORIGINAL).get());

        Map<String, ZonedDateTime> map = createData();
        String actual = jsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}");
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}";
        Map<String, ZonedDateTime> expected = createData();
        Map<String, ZonedDateTime> actual = JacksonUtils.readMap(json, String.class, ZonedDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return MapUtils.of(
                "UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)),
                "Asia/Singapore", ZonedDateTime.parse(str, df.withZone(ZoneId.of("Asia/Singapore"))),
                "Australia/Sydney", ZonedDateTime.parse(str, df.withZone(ZoneId.of("Australia/Sydney"))));
    }

}
