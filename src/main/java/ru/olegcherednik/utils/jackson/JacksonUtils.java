package ru.olegcherednik.utils.jackson;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class JacksonUtils {

    private static final ObjectMapperDecorator DELEGATE = new ObjectMapperDecorator(ObjectMapperUtils::mapper);
    private static final ObjectMapperDecorator PRETTY_PRINT_DELEGATE = new ObjectMapperDecorator(ObjectMapperUtils::prettyPrintMapper);

    // ---------- read ----------

    public static <V> V readValue(String json, Class<V> valueClass) {
        return print().readValue(json, valueClass);
    }

    public static <V> List<V> readList(String json, Class<V> valueClass) {
        return print().readList(json, valueClass);
    }

    public static Map<String, ?> readMap(String json) {
        return print().readMap(json);
    }

    public static <V> Map<String, V> readMap(String json, Class<V> valueClass) {
        return print().readMap(json, valueClass);
    }

    public static <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(json, keyClass, valueClass);
    }

    // ---------- write ----------

    public static <V> String writeValue(V obj) {
        return print().writeValue(obj);
    }

    public static <V> void writeValue(V obj, OutputStream out) {
        print().writeValue(obj, out);
    }

    // ---------- InputStream ----------

    public static <V> V readValue(InputStream in, Class<V> valueClass) {
        return print().readValue(in, valueClass);
    }

    public static <V> List<V> readList(InputStream in, Class<V> valueClass) {
        return print().readList(in, valueClass);
    }

    public static <V> Iterator<V> readListLazy(InputStream in, Class<V> valueClass) {
        return print().readListLazy(in, valueClass);
    }

    public static Map<String, ?> readMap(InputStream in) {
        return print().readMap(in);
    }

    public static <V> Map<String, V> readMap(InputStream in, Class<V> valueClass) {
        return print().readMap(in, valueClass);
    }

    public static <K, V> Map<K, V> readMap(InputStream in, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(in, keyClass, valueClass);
    }

    // ---------- print ----------

    public static ObjectMapperDecorator print() {
        return DELEGATE;
    }

    public static ObjectMapperDecorator prettyPrint() {
        return PRETTY_PRINT_DELEGATE;
    }

    private JacksonUtils() {
    }

}
