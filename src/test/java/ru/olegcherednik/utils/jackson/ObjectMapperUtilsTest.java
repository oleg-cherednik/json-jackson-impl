package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
@Test
public class ObjectMapperUtilsTest {

    @AfterMethod
    public void clear() {
        ObjectMapperUtils.setMapperBuilder(null);
    }

    public void shouldUseNewBuilderWhenSetNotNullBuilderToObjectMapperHolder() {
        Map<String, ZonedDateTime> map = createData();
        assertThat(JacksonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"}");
        assertThat(JacksonUtils.prettyPrint().writeValue(map)).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T13:57:14.225Z\"" + System.lineSeparator() +
                '}');

        ObjectMapperUtils.setMapperBuilder(() -> new JacksonObjectMapperBuilder(ZoneId.of("Asia/Singapore")).get());
        assertThat(JacksonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"}");
        assertThat(JacksonUtils.prettyPrint().writeValue(map)).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"" + System.lineSeparator() +
                '}');
    }

    public void shouldNotRebuildMapperWhenSetSameBuilder() {
        ObjectMapper expectedMapper = ObjectMapperUtils.mapper();
        ObjectMapper expectedPrettyPrintMapper = ObjectMapperUtils.prettyPrintMapper();

        ObjectMapperUtils.setMapperBuilder(ObjectMapperUtils.DEFAULT_MAPPER_BUILDER);
        assertThat(ObjectMapperUtils.mapper()).isSameAs(expectedMapper);
        assertThat(ObjectMapperUtils.prettyPrintMapper()).isSameAs(expectedPrettyPrintMapper);
    }

    private static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return MapUtils.of("UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)));
    }
}
