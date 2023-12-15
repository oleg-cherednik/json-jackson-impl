package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.OffsetTimeDeserializer;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonOffsetTimeDeserializer extends OffsetTimeDeserializer {

    private static final long serialVersionUID = -3619487239995610840L;

    public JacksonOffsetTimeDeserializer(DateTimeFormatter df) {
        super(df);
    }

    protected JacksonOffsetTimeDeserializer(JacksonOffsetTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected JacksonOffsetTimeDeserializer(JacksonOffsetTimeDeserializer base,
                                            Boolean leniency,
                                            DateTimeFormatter df,
                                            JsonFormat.Shape shape,
                                            Boolean readTimestampsAsNanosOverride) {
        super(base, leniency, df, shape, readTimestampsAsNanosOverride);
    }

    @Override
    protected JacksonOffsetTimeDeserializer withDateFormat(DateTimeFormatter df) {
        return new JacksonOffsetTimeDeserializer(this, _isLenient, df, _shape, _readTimestampsAsNanosOverride);
    }

    @Override
    protected JacksonOffsetTimeDeserializer withLeniency(Boolean leniency) {
        return new JacksonOffsetTimeDeserializer(this, leniency);
    }

    @Override
    protected JacksonOffsetTimeDeserializer _withFormatOverrides(DeserializationContext ctxt,
                                                                 BeanProperty property,
                                                                 JsonFormat.Value formatOverrides) {
        JacksonOffsetTimeDeserializer deser =
                (JacksonOffsetTimeDeserializer) super._withFormatOverrides(ctxt, property, formatOverrides);
        Boolean readTimestampsAsNanosOverride = formatOverrides.getFeature(
                JsonFormat.Feature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

        return Objects.equals(readTimestampsAsNanosOverride, deser._readTimestampsAsNanosOverride)
               ? deser : new JacksonOffsetTimeDeserializer(deser, deser._isLenient, deser._formatter,
                                                           deser._shape, readTimestampsAsNanosOverride);
    }

}
