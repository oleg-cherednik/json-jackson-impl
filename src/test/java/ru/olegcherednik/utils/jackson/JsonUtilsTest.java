package ru.olegcherednik.utils.jackson;

import org.apache.commons.io.output.WriterOutputStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 13.12.2016
 */
@Test
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
        assertThat(JsonUtils.readMap(null)).isNull();
        assertThat(JsonUtils.readMap(null, String.class, String.class)).isNull();
        assertThat(JsonUtils.writeValue(null)).isNull();
    }

    public void shouldRetrieveDeserializedObjectWhenReadJson() {
        Data actual = JsonUtils.readValue("{\"intVal\":666,\"strVal\":\"omen\"}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.getIntVal()).isEqualTo(666);
        assertThat(actual.getStrVal()).isEqualTo("omen");
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyJson() {
        Data actual = JsonUtils.readValue("{}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.getIntVal()).isEqualTo(0);
        assertThat(actual.getStrVal()).isNull();
    }

    public void shouldRetrieveDeserializedListWhenReadJsonAsList() {
        String json = "[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]";
        List<Data> actual = JsonUtils.readList(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveEmptyListWhenReadEmptyJsonAsList() {
        assertThat(JsonUtils.readList("{}", Data.class)).isSameAs(Collections.emptyList());
        assertThat(JsonUtils.readList("[]", Data.class)).isSameAs(Collections.emptyList());
    }

    public void shouldRetrieveLinkedHashMapWhenReadJsonAsMap() {
        String json = "{\"sample\":[\"one, two\",\"three\"],\"order\":{\"key1\":\"val1\",\"key2\":\"val2\"}}";
        Map<String, ?> actual = JsonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual).isExactlyInstanceOf(LinkedHashMap.class);
        assertThat(actual).containsOnlyKeys("sample", "order");
        assertThat(actual.get("sample")).isEqualTo(ListUtils.of("one, two", "three"));
        assertThat(actual.get("order")).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueLinkedHashMapWhenReadJsonAsMap() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
        Map<String, ?> actual = JsonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual).isExactlyInstanceOf(LinkedHashMap.class);
        assertThat(actual).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveDataLinkedHashMapWhenReadJsonAsMapWithTypes() {
        String json = "{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"},\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}";
        Map<String, Data> actual = JsonUtils.readMap(json, String.class, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isExactlyInstanceOf(LinkedHashMap.class);
        assertThat(actual.keySet()).containsExactlyInAnyOrder("victory", "omen");
        assertThat(actual.get("victory")).isEqualTo(new Data(555, "victory"));
        assertThat(actual.get("omen")).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveStringValueLinkedHashMapWhenReadJsonAsMapWithTypes() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
        Map<String, String> actual = JsonUtils.readMap(json, String.class, String.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isExactlyInstanceOf(LinkedHashMap.class);
        assertThat(actual).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveEmptyMapWhenReadEmptyJsonAsMap() {
        assertThat(JsonUtils.readMap("{}")).isSameAs(Collections.emptyMap());
        assertThat(JsonUtils.readMap("[]")).isSameAs(Collections.emptyMap());
        assertThat(JsonUtils.readMap("{}", String.class, Data.class)).isSameAs(Collections.emptyMap());
        assertThat(JsonUtils.readMap("[]", String.class, Data.class)).isSameAs(Collections.emptyMap());
    }

    public void shouldRetrieveJsonWhenWriteObject() {
        Data data = new Data(555, "victory");
        String actual = JsonUtils.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"intVal\":555,\"strVal\":\"victory\"}");
    }

    public void shouldRetrieveJsonWhenWriteMapObject() {
        Map<String, Data> map = MapUtils.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        String actual = JsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"},\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}");
    }

    public void shouldRetrieveJsonWhenWriteListObject() {
        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
        String actual = JsonUtils.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
    }

    public void shouldRetrieveEmptyJsonWhenWriteEmptyCollection() {
        assertThat(JsonUtils.writeValue(Collections.emptyList())).isEqualTo("[]");
        assertThat(JsonUtils.writeValue(Collections.emptyMap())).isEqualTo("{}");
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            Data data = new Data(666, "omen");
            JsonUtils.writeValue(data, new WriterOutputStream(out, StandardCharsets.UTF_8));
            assertThat(out.toString()).isEqualTo("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    public void shouldWriteNullJsonWhenWriteNullToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            JsonUtils.writeValue(null, new WriterOutputStream(out, StandardCharsets.UTF_8));
            assertThat(out.toString()).isEqualTo("null");
        }
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteObjectWithPrettyPrint() {
        Data data = new Data(555, "victory");
        String actual = JsonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"intVal\" : 555," + System.lineSeparator() +
                "  \"strVal\" : \"victory\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteMapObjectWithPrettyPrint() {
        Map<String, Data> data = MapUtils.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        String actual = JsonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"victory\" : {" + System.lineSeparator() +
                "    \"intVal\" : 555," + System.lineSeparator() +
                "    \"strVal\" : \"victory\"" + System.lineSeparator() +
                "  }," + System.lineSeparator() +
                "  \"omen\" : {" + System.lineSeparator() +
                "    \"intVal\" : 666," + System.lineSeparator() +
                "    \"strVal\" : \"omen\"" + System.lineSeparator() +
                "  }" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteListObjectWithPrettyPrint() {
        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
        String actual = JsonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[ {" + System.lineSeparator() +
                "  \"intVal\" : 555," + System.lineSeparator() +
                "  \"strVal\" : \"victory\"" + System.lineSeparator() +
                "}, {" + System.lineSeparator() +
                "  \"intVal\" : 666," + System.lineSeparator() +
                "  \"strVal\" : \"omen\"" + System.lineSeparator() +
                "} ]");
    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            Data data = new Data(666, "omen");
            JsonUtils.prettyPrint().writeValue(data, new WriterOutputStream(out, StandardCharsets.UTF_8));
            assertThat(out.toString()).isEqualTo('{' + System.lineSeparator() +
                    "  \"intVal\" : 666," + System.lineSeparator() +
                    "  \"strVal\" : \"omen\"" + System.lineSeparator() +
                    '}');
        }
    }

    public void shouldWriteNullJsonWhenWriteNullWithPrettyPrintToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            JsonUtils.prettyPrint().writeValue(null, new WriterOutputStream(out, StandardCharsets.UTF_8));
            assertThat(out.toString()).isEqualTo("null");
        }
    }

    public void shouldThrowJacksonUtilsExceptionWhenReadIncorrectJson() {
        assertThatThrownBy(() -> JsonUtils.readValue("incorrect", Data.class)).isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JsonUtils.readList("incorrect", Data.class)).isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JsonUtils.readMap("incorrect")).isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JsonUtils.readMap("incorrect", String.class, Data.class)).isExactlyInstanceOf(JacksonUtilsException.class);
    }

    @SuppressWarnings({ "FieldMayBeFinal", "unused" })
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
