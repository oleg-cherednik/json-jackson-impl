/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.jackson_utils;

import org.testng.annotations.Test;
import ru.olegcherednik.jackson_utils.ByteBufferInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 27.02.2022
 */
@Test
public class ByteBufferInputStreamTest {

    public void shouldReadByteFromInputStreamWhenByteBufferBehind() throws IOException {
        try (InputStream in = new ByteBufferInputStream(ByteBuffer.wrap(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }))) {
            assertThat(in.read()).isZero();
            assertThat(in.read()).isEqualTo(1);

            byte[] oneByte = new byte[1];
            assertThat(in.read(oneByte)).isEqualTo(1);
            assertThat(oneByte).containsExactly(2);

            assertThat(in.read(oneByte)).isEqualTo(1);
            assertThat(oneByte).containsExactly(3);

            byte[] twoBytes = new byte[2];
            assertThat(in.read(twoBytes)).isEqualTo(2);
            assertThat(twoBytes).containsExactly(4, 5);

            assertThat(in.read(twoBytes)).isEqualTo(2);
            assertThat(twoBytes).containsExactly(6, 7);

            byte[] fourBytes = new byte[4];
            assertThat(in.read(fourBytes, 2, fourBytes.length)).isEqualTo(2);
            assertThat(fourBytes).containsExactly(0, 0, 8, 9);

            assertThat(in.read()).isEqualTo(-1);
            assertThat(in.read(oneByte)).isEqualTo(-1);
            assertThat(in.read(twoBytes)).isEqualTo(-1);
            assertThat(in.read(fourBytes, 2, fourBytes.length)).isEqualTo(-1);
        }
    }

}
