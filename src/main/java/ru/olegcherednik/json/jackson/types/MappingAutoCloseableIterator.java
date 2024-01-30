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

package ru.olegcherednik.json.jackson.types;

import com.fasterxml.jackson.databind.MappingIterator;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.api.AutoCloseableIterator;

/**
 * @param <T> Type of iterable object
 * @author Oleg Cherednik
 * @since 03.01.2024
 */
@RequiredArgsConstructor
public class MappingAutoCloseableIterator<T> implements AutoCloseableIterator<T> {

    protected final MappingIterator<T> delegate;

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public T next() {
        return delegate.next();
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }
}