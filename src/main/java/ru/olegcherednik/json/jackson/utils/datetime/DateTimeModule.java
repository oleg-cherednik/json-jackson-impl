package ru.olegcherednik.json.jackson.utils.datetime;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 09.12.2023
 */
@RequiredArgsConstructor
public class DateTimeModule extends SimpleModule {

    private static final long serialVersionUID = -7801651470699380868L;

    protected final DateTimeFormatter instantFormatter;
    protected final DateTimeFormatter localDateFormatter;
    protected final DateTimeFormatter localTimeFormatter;
    protected final DateTimeFormatter localDateTimeFormatter;
    protected final DateTimeFormatter offsetTimeFormatter;
    protected final DateTimeFormatter offsetDateTimeFormatter;
    protected final UnaryOperator<ZoneId> zoneModifier;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        JacksonInstantSerializer instant = JacksonInstantSerializer.INSTANCE.with(instantFormatter, zoneModifier);

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(Instant.class, instant);
        serializers.addSerializer(LocalDate.class, JacksonLocalDateSerializer.INSTANCE.with(localDateFormatter));
        serializers.addSerializer(LocalTime.class, JacksonLocalTimeSerializer.INSTANCE.with(localTimeFormatter));
        serializers.addSerializer(LocalDateTime.class, JacksonLocalDateTimeSerializer.INSTANCE
                .with(localDateTimeFormatter));
        serializers.addSerializer(OffsetTime.class,
                                  JacksonOffsetTimeSerializer.INSTANCE.with(offsetTimeFormatter, zoneModifier));
        serializers.addSerializer(OffsetDateTime.class, JacksonOffsetDateTimeSerializer.INSTANCE
                .with(offsetDateTimeFormatter, zoneModifier));
        serializers.addSerializer(ZonedDateTime.class, JacksonZonedDateTimeSerializer.INSTANCE
                .with(offsetDateTimeFormatter, zoneModifier));
        serializers.addSerializer(Date.class, new JacksonDateSerializer(instant));

        context.addSerializers(serializers);
        context.addKeySerializers(serializers);
    }

}
