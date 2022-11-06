package ch.nblotti.airtime.rotation.controlledrotation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import ch.nblotti.airtime.EXERCICE_STATUS;

@Entity
public class ControlledRotation {


    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "session_id")
    public long sessionId;

    @ColumnInfo(name = "status")
    public EXERCICE_STATUS status;

    @ColumnInfo(name = "air_time")
    public Float airTime;

    public long getUid() {
        return uid;
    }

    public long getSessionId() {
        return sessionId;
    }

    public EXERCICE_STATUS getStatus() {
        return status;
    }

    public Float getAirTime() {
        return airTime;
    }
}
