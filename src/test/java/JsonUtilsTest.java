import cop.utils.json.JsonUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 13.12.2016
 */
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

    @Test
    public void testReadWriteObject() throws IOException {
        String json = JsonUtils.writeValue(new Data(666, "omen"));
        assertThat(json).isEqualTo("{\"intVal\":666,\"strVal\":\"omen\"}");

        Data actual = JsonUtils.readValue(json, Data.class);
        assertThat(actual.getIntVal()).isEqualTo(666);
        assertThat(actual.getStrVal()).isEqualTo("omen");

        actual = JsonUtils.readValue("{}", Data.class);
        assertThat(actual.getIntVal()).isEqualTo(0);
        assertThat(actual.getStrVal()).isNull();
    }

    @Test
    public void testReadWriteNull() throws IOException {
        assertThat(JsonUtils.writeValue(null)).isNull();
        assertThat(JsonUtils.readValue(null, Object.class)).isNull();
        assertThat(JsonUtils.readList(null, Object.class)).isNull();
        assertThat(JsonUtils.readMap(null)).isNull();
    }

    @Test
    public void testReadWriteList() throws IOException {
        List<Data> expected = new ArrayList<Data>() {{
            add(new Data(555, "victory"));
            add(new Data(666, "omen"));
        }};

        String json = JsonUtils.writeValue(expected);
        assertThat(json).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
        assertThat(JsonUtils.readList(json, Data.class)).isEqualTo(expected);

        assertThat(JsonUtils.writeValue(Collections.emptyList())).isEqualTo("[]");
        assertThat(JsonUtils.readList("[]", Data.class)).isSameAs(Collections.emptyList());
        assertThat(JsonUtils.readList("{}", Data.class)).isEqualTo(Collections.singletonList(new Data()));
    }

    @Test
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

    @Test
    public void testWriteStream() throws IOException {
        try (Writer out = new StringWriter()) {
            JsonUtils.writeValue(new Data(666, "omen"), new WriterOutputStream(out, Charset.forName("UTF-8")));
            assertThat(out.toString()).isEqualTo("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    @Test
    public void testWriteStreamNull() throws IOException {
        try (Writer out = new StringWriter()) {
            JsonUtils.writeValue(null, new WriterOutputStream(out, Charset.forName("UTF-8")));
            assertThat(out.toString()).isEqualTo("null");
        }
    }
}
