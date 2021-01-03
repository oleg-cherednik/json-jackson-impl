package ru.olegcherednik.utils.jackson;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 22.12.2020
 */
public final class MapUtils {

    static <K, V> Map<K, V> of(K k1, V v1) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        return Collections.unmodifiableMap(map);
    }

    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return Collections.unmodifiableMap(map);
    }

    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return Collections.unmodifiableMap(map);
    }

    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return Collections.unmodifiableMap(map);
    }

    private MapUtils() {
    }

}
