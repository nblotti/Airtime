package ch.nblotti.airtime.rotation.controlledrotation;

import androidx.lifecycle.LiveData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Executors;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.MessageEvent;

public class ControlledRotationRepository {

    private final ControlledRotationDao controlledRotationDao;

    public ControlledRotationRepository(ControlledRotationDao controlledRotationDao) {
        this.controlledRotationDao = controlledRotationDao;
    }


    public LiveData<List<ControlledRotation>> getAll() {
        return controlledRotationDao.getAll();
    }

    public LiveData<List<ControlledRotation>> getAllBySesssionId(long sessionId) {
        return controlledRotationDao.loadAllByIds(sessionId);
    }

    public void deleteById(Long id) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                controlledRotationDao.deleteById(id);
            }
        });


    }

    public void save(ControlledRotation controlledRotation) {


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                long key = controlledRotationDao.insert(controlledRotation);
                EventBus.getDefault().post(MessageEvent.build(MessageEvent.EVENT_TYPE.NEW_CONTROLLED_ROTATION, key));
            }
        });


    }

    public LiveData<ControlledRotation> read(long uid) {
        return controlledRotationDao.read(uid);
    }


    public void updateStatus(Long key, EXERCICE_STATUS stringValue) {


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                controlledRotationDao.updateStatus(key, stringValue);
            }
        });
    }
}
