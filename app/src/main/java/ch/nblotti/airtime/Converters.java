package ch.nblotti.airtime;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter
    public static EXERCICE_STATUS ExerciceStatusFromInt(int value) {
        return EXERCICE_STATUS.valueOf(value);
    }

    @TypeConverter
    public static int toExerciceStatus(EXERCICE_STATUS status) {
        return status == null ? null : status.getValue();
    }


}