package ch.nblotti.airtime;

import android.content.Context;
import android.content.Intent;

import androidx.room.Room;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.inject.Singleton;

import ch.nblotti.airtime.ropejump.RopeJump;
import ch.nblotti.airtime.ropejump.RopeJumpDao;
import ch.nblotti.airtime.ropejump.RopeJumpRepository;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotationDao;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotationRepository;
import ch.nblotti.airtime.sample.SampleDao;
import ch.nblotti.airtime.sample.SampleRepository;
import ch.nblotti.airtime.session.SessionDao;
import ch.nblotti.airtime.session.SessionRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class AirTimeModule {

    private static String DATABASE_NAME = "Airtime";
    private static String pattern = "dd.MM.yyyy";


    @Provides
    @Singleton
    static AppDatabase provideSessionDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    static RopeJumpDao provideRopeJumpDao(AppDatabase appDatabase) {
        return appDatabase.ropeJumpDao();
    }


    

    @Provides
    @Singleton
    static SessionDao provideSessionDao(AppDatabase appDatabase) {
        return appDatabase.sessionDao();
    }

    @Provides
    @Singleton
    static SessionRepository provideSessionRepository(SessionDao sessionDao) {
        return new SessionRepository(sessionDao);
    }

    @Provides
    @Singleton
    static ControlledRotationDao provideControlledRotationDao(AppDatabase appDatabase) {
        return appDatabase.controlledRotationDao();
    }

    @Provides
    @Singleton
    static ControlledRotationRepository provideControlledRotationRepository(ControlledRotationDao controlledRotationDao) {
        return new ControlledRotationRepository(controlledRotationDao);
    }

    @Provides
    @Singleton
    static RopeJumpRepository provideRopeJumpRepository(RopeJumpDao ropeJumpDao) {
        return new RopeJumpRepository(ropeJumpDao);
    }



    @Provides
    @Singleton
    static SampleDao provideSampleDao(AppDatabase appDatabase) {
        return appDatabase.sampleDao();
    }

    @Provides
    @Singleton
    static SampleRepository provideSampleRepository(SampleDao sampleDao) {
        return new SampleRepository(sampleDao);
    }

    @Provides
    @Singleton
    static SimpleDateFormat provideSimpleDateFormat() {
        return new SimpleDateFormat(pattern);
    }


    @Provides
    @Singleton
    static DecimalFormat provideDoubleFormat() {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        return df;
    }

    @Provides
    @Singleton
    static ApplicationController provideApplicationController(SessionDao sessionDao, SampleDao sampleDao) {
        return new ApplicationController(sessionDao, sampleDao);
    }
}
