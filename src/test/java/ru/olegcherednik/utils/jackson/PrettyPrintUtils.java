package ru.olegcherednik.utils.jackson;

import java.util.regex.Pattern;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
final class PrettyPrintUtils {

    public static final Pattern WIN_LINE_SEPARATOR = Pattern.compile("\r\n");
    public static final String UNIX_LINE_SEPARATOR = "\n";

    public static String withUnixLineSeparator(String str) {
        return WIN_LINE_SEPARATOR.matcher(str).replaceAll(UNIX_LINE_SEPARATOR);
    }

    private PrettyPrintUtils() {
    }

}
