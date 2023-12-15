package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonZonedDateTimeDeserializer extends InstantDeserializer<ZonedDateTime> {

    private static final long serialVersionUID = -5671981244499036058L;

    public JacksonZonedDateTimeDeserializer(DateTimeFormatter df) {
        super(InstantDeserializer.ZONED_DATE_TIME, df);
    }

    protected JacksonZonedDateTimeDeserializer(JacksonZonedDateTimeDeserializer base, DateTimeFormatter df) {
        super(base, df);
    }

    protected JacksonZonedDateTimeDeserializer(JacksonZonedDateTimeDeserializer base, DateTimeFormatter df, Boolean leniency) {
        super(base, df, leniency);
    }

    @Override
    protected JacksonZonedDateTimeDeserializer withDateFormat(DateTimeFormatter df) {
        return df == _formatter ? this : new JacksonZonedDateTimeDeserializer(this, df);
    }

    @Override
    protected JacksonZonedDateTimeDeserializer withLeniency(Boolean leniency) {
        return new JacksonZonedDateTimeDeserializer(this, _formatter, leniency);
    }

    @Override
    protected ZonedDateTime _fromString(JsonParser p, DeserializationContext ctxt, String str) throws IOException {
        ZonedDateTime zonedDateTime = super._fromString(p, ctxt, str);
//        zonedDateTime = zonedDateTime.withZoneSameInstant(JsonSettings.SYSTEM_DEFAULT_ZONE_ID);
        return zonedDateTime;
    }
}
