package ru.olegcherednik.utils.jackson;

/**
 * @author Oleg Cherednik
 * @since 22.12.2020
 */
public class JacksonUtilsException extends RuntimeException {

    private static final long serialVersionUID = -8911497717091041951L;

    public JacksonUtilsException(Throwable cause) {
        super(cause);
    }
}
