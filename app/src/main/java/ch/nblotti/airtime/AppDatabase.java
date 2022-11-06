package ch.nblotti.airtime;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotationDao;
import ch.nblotti.airtime.sample.Sample;
import ch.nblotti.airtime.sample.SampleDao;
import ch.nblotti.airtime.session.Session;
import ch.nblotti.airtime.session.SessionDao;

@Database(entities = {Session.class, Sample.class, ControlledRotation.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SessionDao sessionDao();
    public abstract SampleDao sampleDao();

    public abstract ControlledRotationDao controlledRotationDao();
}
