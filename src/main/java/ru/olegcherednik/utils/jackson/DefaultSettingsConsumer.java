package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author Oleg Cherednik
 * @since 23.12.2020
 */
public class DefaultSettingsConsumer implements Consumer<ObjectMapper> {

    public static final DefaultSettingsConsumer INSTANCE = new DefaultSettingsConsumer();

    protected static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSSZ";

    protected DefaultSettingsConsumer() {
    }

    @Override
    public void accept(ObjectMapper mapper) {
        mapper.registerModule(createJavaTimeModule());
        mapper.registerModule(new AfterburnerModule());

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }

    private static JavaTimeModule createJavaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
//        module.addSerializer(ZonedDateTime.class,
//                new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS").withZone(ZoneOffset.UTC)));
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

//        module.addSerializer(Date.class, new LocalDateSerializer());

        return module;
    }

    private static final class LocalDateSerializer extends DateSerializer {

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (_asTimestamp(provider)) {
                gen.writeNumber(_timestamp(value));
            } else if (_customFormat != null) {
                // 21-Feb-2011, tatu: not optimal, but better than alternatives:
                synchronized (_customFormat) {
                    gen.writeString(_customFormat.format(value));
                }
            } else {
                provider.defaultSerializeDateValue(value, gen);
            }
        }
    }
}
