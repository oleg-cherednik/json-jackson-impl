package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
public class ZoneIdZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    private static final long serialVersionUID = -2135138754031293296L;

    private final ZoneId zone;

    public ZoneIdZonedDateTimeSerializer(ZoneId zone) {
        this.zone = Optional.ofNullable(zone).orElse(ZoneOffset.UTC);
    }

    protected ZoneIdZonedDateTimeSerializer(ZoneIdZonedDateTimeSerializer base, Boolean useTimestamp, DateTimeFormatter formatter,
            Boolean writeZoneId) {
        this(base, useTimestamp, null, formatter, writeZoneId);
    }

    protected ZoneIdZonedDateTimeSerializer(ZoneIdZonedDateTimeSerializer base, Boolean useTimestamp, Boolean useNanoseconds,
            DateTimeFormatter formatter, Boolean writeZoneId) {
        super(base, useTimestamp, useNanoseconds, formatter, writeZoneId);
        zone = base.zone;
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
        super.serialize(value.withZoneSameInstant(zone), g, provider);
    }

    @Override
    protected ZoneIdZonedDateTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter formatter, JsonFormat.Shape shape) {
        return new ZoneIdZonedDateTimeSerializer(this, useTimestamp, formatter, _writeZoneId);
    }

    @Override
    protected ZoneIdZonedDateTimeSerializer withFeatures(Boolean writeZoneId) {
        return new ZoneIdZonedDateTimeSerializer(this, _useTimestamp, _formatter, writeZoneId);
    }

    @Override
    protected ZoneIdZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new ZoneIdZonedDateTimeSerializer(this, _useTimestamp, writeNanoseconds, _formatter, writeZoneId);
    }
}
