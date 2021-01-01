package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;

import java.io.IOException;
import java.text.DateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/**
 * @author Oleg Cherednik
 * @since 24.07.2017
 */
public class ZonedDateTimeDeserializer extends JSR310DateTimeDeserializerBase<ZonedDateTime> {

    private static final long serialVersionUID = -8798893923746924324L;

    public ZonedDateTimeDeserializer() {
        this(ISO_ZONED_DATE_TIME);
    }

    public ZonedDateTimeDeserializer(DateTimeFormatter df) {
        super(ZonedDateTime.class, df);
    }

    protected ZonedDateTimeDeserializer(ZonedDateTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected ZonedDateTimeDeserializer(ZonedDateTimeDeserializer base, JsonFormat.Shape shape) {
        super(base, shape);
    }

    // ========== JSR310DateTimeDeserializerBase ==========

    @Override
    protected ZonedDateTimeDeserializer withDateFormat(DateTimeFormatter dtf) {
        return new ZonedDateTimeDeserializer(dtf);
    }

    @Override
    protected ZonedDateTimeDeserializer withLeniency(Boolean leniency) {
        return new ZonedDateTimeDeserializer(this, leniency);
    }

    @Override
    protected ZonedDateTimeDeserializer withShape(JsonFormat.Shape shape) {
        return new ZonedDateTimeDeserializer(this, shape);
    }

    // ========== JsonDeserializer ==========

    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        String str = parser.getText().trim();

        if (!parser.hasTokenId(JsonTokenId.ID_STRING) || str.isEmpty())
            return null;

        DateFormat df = ctxt.getConfig().getDateFormat();

        ZonedDateTime res = deserializeAsZonedDateTime(str);
        Date obj = deserializeAsDate(parser, ctxt);

        return ZonedDateTime.ofInstant(obj.toInstant(), ZoneId.systemDefault());
    }

    private static Date deserializeAsDate(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JavaType type = ctxt.getTypeFactory().constructType(Date.class);
        JsonDeserializer<Object> des = ctxt.findRootValueDeserializer(type);
        return (Date)des.deserialize(parser, ctxt);
    }


    private ZonedDateTime deserializeAsZonedDateTime(String str) {
        try {
            return ZonedDateTime.parse(str, _formatter);
        } catch(DateTimeParseException ignored) {
            return null;
        }
    }

}
