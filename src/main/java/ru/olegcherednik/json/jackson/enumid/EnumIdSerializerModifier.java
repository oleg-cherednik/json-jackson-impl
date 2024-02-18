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

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.module.afterburner.ser.PropertyAccessorCollector;
import com.fasterxml.jackson.module.afterburner.ser.SerializerModifier;
import ru.olegcherednik.json.api.enumid.EnumId;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Oleg Cherednik
 * @since 27.02.2022
 */
public class EnumIdSerializerModifier extends SerializerModifier {

    private static final long serialVersionUID = -1888579980196998551L;

    public static final EnumIdSerializerModifier INSTANCE = new EnumIdSerializerModifier();

    protected EnumIdSerializerModifier() {
        super(null);
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    protected PropertyAccessorCollector findProperties(Class<?> beanClass,
                                                       SerializationConfig config,
                                                       List<BeanPropertyWriter> beanProperties) {
        PropertyAccessorCollector collector = new PropertyAccessorCollector(beanClass);
        ListIterator<BeanPropertyWriter> it = beanProperties.listIterator();

        while (it.hasNext()) {
            BeanPropertyWriter bpw = it.next();

            if (EnumId.class.isAssignableFrom(bpw.getMember().getRawType()))
                it.set(new EnumIdBeanPropertyWriter(bpw));
        }

        return collector;
    }

}
