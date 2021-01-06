package ru.olegcherednik.utils.jackson;

import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
@Test
public class LocalDateTimeJacksonUtilsTest {

    public void shouldRetrieveJsonWhenWriteZonedDateTime() throws IOException {
        Map<String, LocalDateTime> map = createData();
        String actual = JacksonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"local\":\"2017-07-23T13:57:14.225\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteZonedDateTimeMapWithPrettyPrint() {
        Map<String, LocalDateTime> map = createData();
        String actual = JacksonUtils.prettyPrint().writeValue(map);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"local\" : \"2017-07-23T13:57:14.225\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"local\":\"2017-07-23T13:57:14.225\"}";
        Map<String, LocalDateTime> expected = createData();
        Map<String, LocalDateTime> actual = JacksonUtils.readMap(json, String.class, LocalDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    private static Map<String, LocalDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return MapUtils.of(
                "local", LocalDateTime.parse(str, df));
    }

}
