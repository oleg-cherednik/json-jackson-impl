import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 13.12.2016
 */
class Data {
    private int intVal;
    private String strVal;

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

    // ========== Object ==========

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
