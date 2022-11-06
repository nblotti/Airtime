package ch.nblotti.airtime.session;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Session {


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "user")
    public String user;


    @ColumnInfo(name = "session_date")
    public Date sessionDate;


    @ColumnInfo(name = "session_average")
    public Double sessionAverage;

    public int getUid() {
        return uid;
    }

    public String getUser() {
        return user;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public Double getSessionAverage() {
        return sessionAverage;
    }

}
