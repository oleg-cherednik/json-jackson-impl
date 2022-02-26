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
package ru.olegcherednik.jackson.utils.data;

import java.util.Objects;
import java.util.Scanner;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@SuppressWarnings("unused")
public class Data {

    private int intVal;
    private String strVal;
    private String nullVal;

    public Data() {
    }

    public Data(int intVal, String strVal) {
        this.intVal = intVal;
        this.strVal = strVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public String getUnknownValue() {
        return intVal + '_' + strVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public void setUnknownValue(String str) {
        Scanner scan = new Scanner(str);
        scan.useDelimiter("_");
        intVal = scan.nextInt();
        strVal = scan.next();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Data))
            return false;
        Data data = (Data)obj;
        return intVal == data.intVal && Objects.equals(strVal, data.strVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intVal, strVal);
    }

}
