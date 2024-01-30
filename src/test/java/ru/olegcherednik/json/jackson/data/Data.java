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

package ru.olegcherednik.json.jackson.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Scanner;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@SuppressWarnings("unused")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Data {

    public static final Data EMPTY = new Data();
    public static final Data VICTORY = new Data(555, "victory");
    public static final Data OMEN = new Data(666, "omen");

    @Getter
    @Setter
    private int intVal;
    @Getter
    @Setter
    private String strVal;
    private String nullVal;

    public Data(int intVal, String strVal) {
        this.intVal = intVal;
        this.strVal = strVal;
    }

    public String getUnknownValue() {
        return intVal + '_' + strVal;
    }

    public void setUnknownValue(String str) {
        try (Scanner scan = new Scanner(str)) {
            scan.useDelimiter("_");
            intVal = scan.nextInt();
            strVal = scan.next();
        }
    }

}

