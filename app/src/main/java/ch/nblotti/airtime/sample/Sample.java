package ch.nblotti.airtime.sample;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import ch.nblotti.airtime.sample.ui.SAMPLE_TYPE;

@Entity
public class Sample {


    @ColumnInfo(name = "session_id")
    public long sessionId;

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "sample_type")
    public SAMPLE_TYPE sampleType;

    @ColumnInfo(name = "sample_measure")
    public Float sampleMeasure;

    public long getUid() {
        return uid;
    }


    public Sample() {
    }

    public Sample(long sessionId, long uid, SAMPLE_TYPE sampleType, Float sampleMeasure) {
        this();
        this.sessionId = sessionId;
        this.uid = uid;
        this.sampleType = sampleType;
        this.sampleMeasure = sampleMeasure;
    }
}
