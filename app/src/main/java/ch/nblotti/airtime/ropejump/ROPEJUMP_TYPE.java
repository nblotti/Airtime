package ch.nblotti.airtime.ropejump;

public enum ROPEJUMP_TYPE {

    SIMPLE("Simple", 1, 15),
    DOUBLE("Double", 2, 10),
    CROISES("Croisés", 3, 10);


    private final int value;
    private final String stringValue;
    private final int time;

    private ROPEJUMP_TYPE(String stringValue, int value, int time) {
        this.stringValue = stringValue;
        this.value = value;
        this.time = time;
    }

    public static ROPEJUMP_TYPE valueOf(int value) {

        for (ROPEJUMP_TYPE current : ROPEJUMP_TYPE.values()) {
            if (current.value == value)
                return current;
        }

        throw new IllegalArgumentException("ROPEJUMP_TYPE");

    }

    public static ROPEJUMP_TYPE stringValueOf(String value) {

        for (ROPEJUMP_TYPE current : ROPEJUMP_TYPE.values()) {
            if (current.stringValue.equals(value))
                return current;
        }

        throw new IllegalArgumentException("ROPEJUMP_TYPE");

    }

    public int getValue() {
        return value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getTime() {
        return time;
    }
}