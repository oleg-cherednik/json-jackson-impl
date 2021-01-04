package ru.olegcherednik.utils.jackson;

import org.testng.annotations.Test;

import java.io.IOException;
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
public class ZonedDateTimeJsonUtilsTest {

    public void shouldRetrieveJsonUTCZoneWhenWriteZonedDateTimeDefaultSettings() throws IOException {
        Map<String, ZonedDateTime> map = createData();
        String actual = JsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\"," +
                "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveJsonSingaporeZoneWhenWriteZonedDateTimeSingaporeZone() throws IOException {
        ObjectMapperDecorator jsonUtils = ObjectMapperUtils.createMapperDecorator(
                () -> new JacksonObjectMapperBuilder(ZoneId.of("Asia/Singapore")).get());

        Map<String, ZonedDateTime> map = createData();
        String actual = jsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"}");
    }

    public void shouldRetrieveJsonWithNoZoneChangeWhenWriteZonedDateTimeWithSameZone() throws IOException {
        ObjectMapperDecorator jsonUtils = ObjectMapperUtils.createMapperDecorator(
                () -> new JacksonObjectMapperBuilder(JacksonObjectMapperBuilder.WITH_SAME_ZONE).get());

        Map<String, ZonedDateTime> map = createData();
        String actual = jsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}");
    }

    public void shouldRetrievePrettyPrintJsonUTCZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
        Map<String, ZonedDateTime> map = createData();
        String actual = JsonUtils.prettyPrint().writeValue(map);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T13:57:14.225Z\"," + System.lineSeparator() +
                "  \"Asia/Singapore\" : \"2017-07-23T05:57:14.225Z\"," + System.lineSeparator() +
                "  \"Australia/Sydney\" : \"2017-07-23T03:57:14.225Z\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonSingaporeZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
        ObjectMapperDecorator jsonUtils = ObjectMapperUtils.createPrettyPrintMapperDecorator(
                () -> new JacksonObjectMapperBuilder(ZoneId.of("Asia/Singapore")).get());

        Map<String, ZonedDateTime> map = createData();
        String actual = jsonUtils.writeValue(map);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," + System.lineSeparator() +
                "  \"Asia/Singapore\" : \"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," + System.lineSeparator() +
                "  \"Australia/Sydney\" : \"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}";
        Map<String, ZonedDateTime> expected = createData();
        Map<String, ZonedDateTime> actual = JsonUtils.readMap(json, String.class, ZonedDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    private static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return MapUtils.of(
                "UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)),
                "Asia/Singapore", ZonedDateTime.parse(str, df.withZone(ZoneId.of("Asia/Singapore"))),
                "Australia/Sydney", ZonedDateTime.parse(str, df.withZone(ZoneId.of("Australia/Sydney"))));
    }

}
