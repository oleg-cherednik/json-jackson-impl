package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DateFormat;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonDateDeserializer extends DateDeserializers.DateDeserializer {

    private static final long serialVersionUID = -8000633867087095319L;

    public static final JacksonDateDeserializer INSTANCE = new JacksonDateDeserializer();

    public static JacksonDateDeserializer with(DateFormat df) {
        return new JacksonDateDeserializer(INSTANCE, df, INSTANCE._formatString);
    }

    protected JacksonDateDeserializer(DateDeserializers.DateDeserializer base,
                                      DateFormat df,
                                      String formatString) {
        super(base, df, formatString);
    }

    @Override
    protected JacksonDateDeserializer withDateFormat(DateFormat df, String formatString) {
        return new JacksonDateDeserializer(this, df, formatString);
    }

}
