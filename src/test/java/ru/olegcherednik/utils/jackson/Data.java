package ru.olegcherednik.utils.jackson;

import java.util.Objects;
import java.util.Scanner;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@SuppressWarnings("unused")
class Data {

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
