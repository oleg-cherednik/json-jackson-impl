package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonOffsetDateTimeDeserializer extends InstantDeserializer<OffsetDateTime> {

    private static final long serialVersionUID = -4594405514078469683L;

    public JacksonOffsetDateTimeDeserializer(DateTimeFormatter df) {
        super(InstantDeserializer.OFFSET_DATE_TIME, df);
    }

    protected JacksonOffsetDateTimeDeserializer(JacksonOffsetDateTimeDeserializer base, DateTimeFormatter df) {
        super(base, df);
    }

    protected JacksonOffsetDateTimeDeserializer(JacksonOffsetDateTimeDeserializer base, DateTimeFormatter df, Boolean leniency) {
        super(base, df, leniency);
    }

    @Override
    protected JacksonOffsetDateTimeDeserializer withDateFormat(DateTimeFormatter df) {
        return df == _formatter ? this : new JacksonOffsetDateTimeDeserializer(this, df);
    }

    @Override
    protected JacksonOffsetDateTimeDeserializer withLeniency(Boolean leniency) {
        return new JacksonOffsetDateTimeDeserializer(this, _formatter, leniency);
    }

}
