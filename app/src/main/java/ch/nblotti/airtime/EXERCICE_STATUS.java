package ch.nblotti.airtime;

import java.util.Arrays;

public enum EXERCICE_STATUS {

    INSUFFISANT("Insuffisant", 1),
    PASSABLE("Passable", 2),
    SUFFISANT("Suffisant", 3),
    SATISFAISANT("Satisfaisant", 4),
    EXCELLENT("Excellent", 5);


    private final int value;
    private final String stringValue;

    private EXERCICE_STATUS(String stringValue, int value) {
        this.stringValue = stringValue;
        this.value = value;
    }

    public static EXERCICE_STATUS valueOf(int value) {

        for (EXERCICE_STATUS current : EXERCICE_STATUS.values()) {
            if (current.value == value)
                return current;
        }

        throw new IllegalArgumentException("EXERCICE_STATUS");

    }
    public static EXERCICE_STATUS stringValueOf(String value) {

        for (EXERCICE_STATUS current : EXERCICE_STATUS.values()) {
            if (current.stringValue.equals(value))
                return current;
        }

        throw new IllegalArgumentException("EXERCICE_STATUS");

    }

    public int getValue() {
        return value;
    }

    public String getStringValue() {
        return stringValue;
    }
}