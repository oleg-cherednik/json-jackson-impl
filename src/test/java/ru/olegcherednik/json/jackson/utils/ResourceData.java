package ru.olegcherednik.json.jackson.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 04.11.2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceData {

    public static InputStream getResourceAsInputStream(String name) throws IOException {
        return ResourceData.class.getResourceAsStream(name);
    }

    public static String getResourceAsString(String name) throws IOException {
        try (InputStream in = getResourceAsInputStream(name)) {
            return IOUtils.toString(Objects.requireNonNull(in), StandardCharsets.UTF_8);
        }
    }
}
