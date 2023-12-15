package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.key.Jsr310NullKeySerializer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
@RequiredArgsConstructor
public class JacksonJsr310KeyDeserializer<T> extends KeyDeserializer {

    protected final Class<T> type;
    protected final TemporalQuery<T> query;
    protected final DateTimeFormatter df;

    public static JacksonJsr310KeyDeserializer<Instant> instant(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(Instant.class, Instant::from, df);
    }

    public static JacksonJsr310KeyDeserializer<LocalDate> localDate(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(LocalDate.class, LocalDate::from, df);
    }

    public static JacksonJsr310KeyDeserializer<LocalTime> localTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(LocalTime.class, LocalTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<LocalDateTime> localDateTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(LocalDateTime.class, LocalDateTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<OffsetTime> offsetTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(OffsetTime.class, OffsetTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<OffsetDateTime> offsetDateTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(OffsetDateTime.class, OffsetDateTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<ZonedDateTime> zonedDateTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(ZonedDateTime.class, ZonedDateTime::from, df);
    }

    @Override
    @SuppressWarnings("deprecation")
    public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        if (Jsr310NullKeySerializer.NULL_KEY.equals(key))
            return null;

        try {
            return df.parse(key, query);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, e, key);
        }
    }

    @SuppressWarnings("NewMethodNamingConvention")
    protected Object _handleDateTimeException(DeserializationContext ctxt,
                                              DateTimeException e0,
                                              String key) throws IOException {
        try {
            return ctxt.handleWeirdKey(type, key, "Failed to deserialize %s: (%s) %s",
                                       type.getName(), e0.getClass().getName(), e0.getMessage());

        } catch (JsonMappingException e) {
            e.initCause(e0);
            throw e;
        } catch (IOException e) {
            if (null == e.getCause())
                e.initCause(e0);
            throw JsonMappingException.fromUnexpectedIOE(e);
        }
    }
}

