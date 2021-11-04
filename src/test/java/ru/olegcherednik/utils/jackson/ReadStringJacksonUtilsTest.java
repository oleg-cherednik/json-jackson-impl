package ru.olegcherednik.utils.jackson;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
public class ReadStringJacksonUtilsTest {

    @BeforeClass
    public static void init() {
        try {
            Constructor<JacksonUtils> constructor = JacksonUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch(Exception ignored) {
        }
    }

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JacksonUtils.readValue((String)null, Object.class)).isNull();
        assertThat(JacksonUtils.readList((String)null, Object.class)).isNull();
        assertThat(JacksonUtils.readMap((String)null)).isNull();
        assertThat(JacksonUtils.readMap((String)null, String.class, String.class)).isNull();
    }

    public void shouldRetrieveDeserializedObjectWhenReadJson() {
        Data expected = new Data(666, "omen");
        Data actual = JacksonUtils.readValue("{\"intVal\":666,\"strVal\":\"omen\"}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyJson() {
        Data expected = new Data();
        Data actual = JacksonUtils.readValue("{}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveDeserializedListWhenReadJsonAsList() {
        String json = "[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]";
        List<Data> actual = JacksonUtils.readList(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveEmptyListWhenReadEmptyJsonAsList() {
        assertThat(JacksonUtils.readList("{}", Data.class)).isSameAs(Collections.emptyList());
        assertThat(JacksonUtils.readList("[]", Data.class)).isSameAs(Collections.emptyList());
    }

    public void shouldRetrieveMapWhenReadJsonAsMap() {
        String json = "{\"sample\":[\"one, two\",\"three\"],\"order\":{\"key1\":\"val1\",\"key2\":\"val2\"}}";
        Map<String, ?> actual = JacksonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual.get("sample")).isEqualTo(ListUtils.of("one, two", "three"));
        assertThat(actual.get("order")).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadJsonAsMap() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
        Map<String, ?> actual = JacksonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual).isExactlyInstanceOf(LinkedHashMap.class);
        assertThat(actual).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveDataMapWhenReadJsonAsMapWithStringKeyAndGivenValueType() {
        String json = "{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"},\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}";
        Map<String, Data> actual = JacksonUtils.readMap(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("victory", "omen");
        assertThat(actual.get("victory")).isEqualTo(new Data(555, "victory"));
        assertThat(actual.get("omen")).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveStringValueMapWhenReadJsonAsMapWithStringKeyAndType() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
        Map<String, String> actual = JacksonUtils.readMap(json, String.class);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("key1", "key2");
        assertThat(actual).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveIntegerValueMapWhenReadJsonAsMapWithIntegerKeyAndGivenValueType() {
        String json = "{\"1\":{\"intVal\":555,\"strVal\":\"victory\"},\"2\":{\"intVal\":666,\"strVal\":\"omen\"}}";
        Map<Integer, Data> actual = JacksonUtils.readMap(json, Integer.class, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly(1, 2);
        assertThat(actual.get(1)).isEqualTo(new Data(555, "victory"));
        assertThat(actual.get(2)).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyMapWhenReadEmptyJsonAsMap() {
        assertThat(JacksonUtils.readMap("{}")).isSameAs(Collections.emptyMap());
        assertThat(JacksonUtils.readMap("[]")).isSameAs(Collections.emptyMap());
        assertThat(JacksonUtils.readMap("{}", String.class, Data.class)).isSameAs(Collections.emptyMap());
        assertThat(JacksonUtils.readMap("[]", String.class, Data.class)).isSameAs(Collections.emptyMap());
    }

    public void shouldThrowJacksonUtilsExceptionWhenReadIncorrectJson() {
        assertThatThrownBy(() -> JacksonUtils.readValue("incorrect", Data.class)).isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readList("incorrect", Data.class)).isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap("incorrect")).isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap("incorrect", String.class, Data.class)).isExactlyInstanceOf(JacksonUtilsException.class);
    }

}
