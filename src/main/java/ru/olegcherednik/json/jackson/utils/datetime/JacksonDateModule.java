package ru.olegcherednik.json.jackson.utils.datetime;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.RequiredArgsConstructor;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Oleg Cherednik
 * @since 09.12.2023
 */
@RequiredArgsConstructor
public class JacksonDateModule extends SimpleModule {

    private static final long serialVersionUID = -7801651470699380868L;

    protected final DateFormat dateFormatter;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(Date.class, new DateSerializer(null, dateFormatter));

        context.addSerializers(serializers);
        context.addKeySerializers(serializers);
    }

}
