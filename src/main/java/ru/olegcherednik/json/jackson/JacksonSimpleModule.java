package ru.olegcherednik.json.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Oleg Cherednik
 * @since 18.02.2024
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonSimpleModule extends SimpleModule {

    private static final long serialVersionUID = -543871656247679655L;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        addKeySerializers(context);
        addKeyDeserializers(context);

        addSerializers(context);
        addDeserializers(context);
    }

    protected void addKeySerializers(SetupContext context) {
    }

    protected void addKeyDeserializers(SetupContext context) {
    }

    protected void addSerializers(SetupContext context) {
    }

    protected void addDeserializers(SetupContext context) {
    }

    protected static <T> SimpleSerializers createSerializers(JsonSerializer<T> serializer) {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(serializer.handledType(), serializer);
        return serializers;
    }

    protected static <T> SimpleDeserializers createDeserializers(JsonDeserializer<T> deserializer) {
        SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer((Class<T>) deserializer.handledType(), deserializer);
        return deserializers;
    }

}
