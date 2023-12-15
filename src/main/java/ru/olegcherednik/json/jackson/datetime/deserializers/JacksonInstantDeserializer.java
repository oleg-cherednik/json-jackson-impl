package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonInstantDeserializer extends InstantDeserializer<Instant> {

    private static final long serialVersionUID = 6304027484218980135L;

    public JacksonInstantDeserializer(DateTimeFormatter df) {
        super(InstantDeserializer.INSTANT, df);
    }

    protected JacksonInstantDeserializer(JacksonInstantDeserializer base, DateTimeFormatter df) {
        super(base, df);
    }

    protected JacksonInstantDeserializer(JacksonInstantDeserializer base, DateTimeFormatter df, Boolean leniency) {
        super(base, df, leniency);
    }

    @Override
    protected JacksonInstantDeserializer withDateFormat(DateTimeFormatter df) {
        return df == _formatter ? this : new JacksonInstantDeserializer(this, df);
    }

    @Override
    protected JacksonInstantDeserializer withLeniency(Boolean leniency) {
        return new JacksonInstantDeserializer(this, _formatter, leniency);
    }

}
