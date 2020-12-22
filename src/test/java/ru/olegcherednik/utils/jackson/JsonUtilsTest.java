package ru.olegcherednik.utils.jackson;

import org.apache.commons.io.output.WriterOutputStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 13.12.2016
 */
@Test
@SuppressWarnings("serial")
public class JsonUtilsTest {

    @BeforeClass
    public static void init() {
        try {
            Constructor<JsonUtils> constructor = JsonUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch(Exception ignored) {
        }
    }

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JsonUtils.readValue(null, Object.class)).isNull();
        assertThat(JsonUtils.readList(null, Object.class)).isNull();
        assertThat(JsonUtils.writeValue(null)).isNull();
        assertThat(JsonUtils.readMap(null)).isNull();
    }

    public void shouldRetrieveDeserializedObjectWhenReadJson() {
        Data actual = JsonUtils.readValue("{\"intVal\":666,\"strVal\":\"omen\"}", Data.class);
        assertThat(actual.getIntVal()).isEqualTo(666);
        assertThat(actual.getStrVal()).isEqualTo("omen");
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyJson() {
        Data actual = JsonUtils.readValue("{}", Data.class);
        assertThat(actual.getIntVal()).isEqualTo(0);
        assertThat(actual.getStrVal()).isNull();
    }

    public void shouldRetrieveDeserializedListWhenReadJsonAsList() throws IOException {
        String json = "[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]";
        List<Data> actual = JsonUtils.readList(json, Data.class);
        assertThat(actual).isEqualTo(ListUtils.of(
                new Data(555, "victory"),
                new Data(666, "omen")));
    }

    public void shouldRetrieveEmptyListWhenReadEmptyJsonAsList() {
        assertThat(JsonUtils.readList("[]", Data.class)).isSameAs(Collections.emptyList());
    }

    public void shouldRetrieveListWithOneEmptyElementWhenReadEmptyJsonObjectAsList() {
        assertThat(JsonUtils.readList("{}", Data.class)).isEqualTo(ListUtils.of(new Data()));
    }

//    public void testReadWriteObject() throws IOException {
//        String json = JsonUtils.writeValue(new Data(666, "omen"));
//        assertThat(json).isEqualTo("{\"intVal\":666,\"strVal\":\"omen\"}");
//
//        Data actual = JsonUtils.readValue(json, Data.class);
//        assertThat(actual.getIntVal()).isEqualTo(666);
//        assertThat(actual.getStrVal()).isEqualTo("omen");
//
//        actual = JsonUtils.readValue("{}", Data.class);
//        assertThat(actual.getIntVal()).isEqualTo(0);
//        assertThat(actual.getStrVal()).isNull();
//    }

//    public void testReadWriteList() throws IOException {
//        List<Data> expected = new ArrayList<Data>() {{
//            add(new Data(555, "victory"));
//            add(new Data(666, "omen"));
//        }};
//
//        String json = JsonUtils.writeValue(expected);
//        assertThat(json).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
//        assertThat(JsonUtils.readList(json, Data.class)).isEqualTo(expected);
//
//        assertThat(JsonUtils.writeValue(Collections.emptyList())).isEqualTo("[]");
//        assertThat(JsonUtils.readList("[]", Data.class)).isSameAs(Collections.emptyList());
//        assertThat(JsonUtils.readList("{}", Data.class)).isEqualTo(Collections.singletonList(new Data()));
//    }

    public void testReadWriteMap() throws IOException {
        Map<String, Object> expected = new LinkedHashMap<String, Object>() {{
            put("sample", Arrays.asList("one, two", "three"));
            put("order", new LinkedHashMap<String, Object>() {{
                put("key1", "val1");
                put("key2", "val2");
            }});
        }};

        String json = JsonUtils.writeValue(expected);
        assertThat(json).isEqualTo("{\"sample\":[\"one, two\",\"three\"],\"order\":{\"key1\":\"val1\",\"key2\":\"val2\"}}");
        assertThat(JsonUtils.readMap(json)).isEqualTo(expected);

        assertThat(JsonUtils.writeValue(Collections.emptyMap())).isEqualTo("{}");
        assertThat(JsonUtils.readMap("{}")).isSameAs(Collections.emptyMap());
        assertThat(JsonUtils.readMap("[]")).isSameAs(Collections.emptyMap());
    }

    public void testWriteStream() throws IOException {
        try (Writer out = new StringWriter()) {
            JsonUtils.writeValue(new Data(666, "omen"), new WriterOutputStream(out, StandardCharsets.UTF_8));
            assertThat(out.toString()).isEqualTo("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    public void testWriteStreamNull() throws IOException {
        try (Writer out = new StringWriter()) {
            JsonUtils.writeValue(null, new WriterOutputStream(out, StandardCharsets.UTF_8));
            assertThat(out.toString()).isEqualTo("null");
        }
    }

//    public void testReadWriteDate() throws IOException {
//        Date expected = new Date(1500796634225L);
//        String json = JsonUtils.writeValue(expected);
//        assertThat(json).isEqualTo("\"2017-07-23T07:57:14.225+0000\"");
//        assertThat(JsonUtils.readValue(json, Date.class)).isEqualTo(expected);
//    }

//    public void testReadWriteZonedDateTime() throws IOException {
//        String str = "2017-07-23T07:57:14.225";
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
//        ZonedDateTime utc = ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC));
//        ZonedDateTime singapore = ZonedDateTime.parse(str,
//                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneId.of("Asia/Singapore")));
//        ZonedDateTime australia = ZonedDateTime.parse(str,
//                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneId.of("Australia/Sydney")));
//
//        String json = JsonUtils.writeValue(utc);
//        assertThat(json).isEqualTo("\"2017-07-23T07:57:14.225Z\"");
//        assertThat(JsonUtils.readValue(json, ZonedDateTime.class)).isEqualTo(utc);
//
//        json = JsonUtils.writeValue(singapore);
//        assertThat(json).isEqualTo("\"2017-07-23T07:57:14.225Z\"");
//        assertThat(JsonUtils.readValue(json, ZonedDateTime.class)).isEqualTo(singapore);
//    }

//    public void testWriteDateReadZonedDateTime() throws IOException {
//        Date expected = new Date();
//        String json = JsonUtils.writeValue(expected);
////        assertThat(json).isEqualTo("\"2017-07-23T07:57:14.225+0000\"");
//        // "2017-07-24T07:36:21.129+0000"
//
//        ZonedDateTime actual = JsonUtils.readValue(json, ZonedDateTime.class);
//        int a = 0;
//        a++;
//
////        assertThat(JsonUtils.readValue(json, Date.class)).isEqualTo(expected);
//    }

    private static final class Data {

        private int intVal;
        private String strVal;

        public Data() {
        }

        public Data(int intVal, String strVal) {
            this.intVal = intVal;
            this.strVal = strVal;
        }

        public int getIntVal() {
            return intVal;
        }

        public String getStrVal() {
            return strVal;
        }

        // ========== Object ==========

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Data))
                return false;
            Data data = (Data)obj;

            return intVal == data.intVal && Objects.equals(strVal, data.strVal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(intVal, strVal);
        }
    }
}
