package ru.olegcherednik.utils.jackson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 22.12.2020
 */
public final class ListUtils {

    public static <T> List<T> of(T... elements) {
        if (elements == null || elements.length == 0)
            return Collections.emptyList();
        return Collections.unmodifiableList(Arrays.stream(elements).collect(Collectors.toList()));
    }

    private ListUtils() {
    }

}
