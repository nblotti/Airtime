package ch.nblotti.airtime.ropejump;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ch.nblotti.airtime.EXERCICE_STATUS;

@Entity
public class RopeJump {


    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "session_id")
    public long sessionId;

    @ColumnInfo(name = "type")
    public ROPEJUMP_TYPE type;

    @ColumnInfo(name = "status")
    public EXERCICE_STATUS status;

    @ColumnInfo(name = "number")
    public int number;

    @ColumnInfo(name = "points")
    public float points;

    public RopeJump() {

    }

    public RopeJump(long sessionId, ROPEJUMP_TYPE type, EXERCICE_STATUS status, int number, float points) {
        this.sessionId = sessionId;
        this.type = type;
        this.status = status;
        this.number = number;
        this.points = points;
    }

    public long getUid() {
        return uid;
    }

    public long getSessionId() {
        return sessionId;
    }

    public EXERCICE_STATUS getStatus() {
        return status;
    }

    public ROPEJUMP_TYPE getType() {
        return type;
    }

    public int getNumber() {
        return this.number;
    }

    public float getPoints() {
        return points;
    }
}
