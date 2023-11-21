package ru.olegcherednik.json.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.jackson_utils.JacksonJsonEngineSupplier;
import ru.olegcherednik.json.JsonEngine;
import ru.olegcherednik.json.JsonEngineFactory;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final JsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    private final JacksonJsonEngineSupplier jsonEngineSupplier =
            JacksonJsonEngineSupplier.builder().build();

    public static JsonEngineFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public JsonEngine createJsonEngine() {
        return jsonEngineSupplier.get();
    }

    @Override
    public JsonEngine createJsonEnginePrettyPrint() {
        return jsonEngineSupplier.getPrettyPrint();
    }
}
