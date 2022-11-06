package ch.nblotti.airtime;

import java.util.Arrays;

public enum EXERCICE_STATUS {

    INSUFFISANT("Insuffisant", 1, 0F),
    PASSABLE("Passable", 2, 0.25F),
    SUFFISANT("Suffisant", 3, 0F),
    SATISFAISANT("Satisfaisant", 4, 0.5F),
    EXCELLENT("Excellent", 5, 0F);


    private final int value;
    private final String stringValue;
    private final float points;

    private EXERCICE_STATUS(String stringValue, int value, float points) {
        this.stringValue = stringValue;
        this.value = value;
        this.points = points;
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

    public float getPoints() {
        return points;
    }
}