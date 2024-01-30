/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.jackson.enumid;

import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.olegcherednik.json.api.enumid.EnumId;

public final class EnumIdModule extends SimpleModule {

    private static final long serialVersionUID = -946898814418994813L;

    public EnumIdModule() {
        addSerializer(EnumId.class, EnumIdSerializer.INSTANCE);
        _deserializers = EnumIdDeserializers.INSTANCE;
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addBeanSerializerModifier(EnumIdSerializerModifier.INSTANCE);
    }

}
