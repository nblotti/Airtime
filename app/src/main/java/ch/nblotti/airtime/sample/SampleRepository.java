package ch.nblotti.airtime.sample;

import androidx.lifecycle.LiveData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Executors;

import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.session.Session;

public class SampleRepository {

    private final SampleDao sampleDao;

    public SampleRepository(SampleDao sampleDao) {
        this.sampleDao = sampleDao;
    }


    public LiveData<List<Sample>> getAllBySesssionId(long sessionId) {
        return sampleDao.loadAllByIds(sessionId);
    }

    public void deleteById(Sample sample) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                sampleDao.deleteById(sample.uid);
                EventBus.getDefault().post(MessageEvent.build(MessageEvent.EVENT_TYPE.VIDEO_CHANGE, sample.sessionId));
            }
        });


    }

    public void deleteBySessionId(Integer id) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                sampleDao.deleteBySessionId(id);
            }
        });


    }

    public void save(Sample sample) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                sampleDao.insertAll(sample);
                EventBus.getDefault().post(MessageEvent.build(MessageEvent.EVENT_TYPE.VIDEO_CHANGE, sample.sessionId));
            }
        });
    }


}
