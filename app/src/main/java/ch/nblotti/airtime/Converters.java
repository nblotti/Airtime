package ch.nblotti.airtime;

import androidx.room.TypeConverter;

import java.util.Date;

import ch.nblotti.airtime.ropejump.ROPEJUMP_TYPE;

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

    @TypeConverter
    public static ROPEJUMP_TYPE RopeJumpTypeFromInt(int value) {
        return ROPEJUMP_TYPE.valueOf(value);
    }

    @TypeConverter
    public static int toRopeJumpType(ROPEJUMP_TYPE status) {
        return status == null ? null : status.getValue();
    }


}