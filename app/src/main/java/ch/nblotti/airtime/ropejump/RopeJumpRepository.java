package ch.nblotti.airtime.ropejump;

import androidx.lifecycle.LiveData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Executors;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.MessageEvent;

public class RopeJumpRepository {

    private final RopeJumpDao ropeJumpDao;

    public RopeJumpRepository(RopeJumpDao ropeJumpDao) {
        this.ropeJumpDao = ropeJumpDao;
    }


    public LiveData<List<RopeJump>> getAll() {
        return ropeJumpDao.getAll();
    }

    public LiveData<List<RopeJump>> getAllBySesssionIdAndType(long sessionId, ROPEJUMP_TYPE ropejump_type) {
        return ropeJumpDao.loadAllByIdsAndType(sessionId,ropejump_type);
    }

    public void deleteById(Long id) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ropeJumpDao.deleteById(id);
            }
        });


    }

    public void save(RopeJump controlledRotation) {


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                long key = ropeJumpDao.insert(controlledRotation);
                EventBus.getDefault().post(MessageEvent.build(MessageEvent.EVENT_TYPE.NEW_CONTROLLED_ROTATION, key));
            }
        });


    }

    public LiveData<RopeJump> read(long uid) {
        return ropeJumpDao.read(uid);
    }


    public void updateStatus(Long key, EXERCICE_STATUS stringValue) {


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ropeJumpDao.updateStatus(key, stringValue);
            }
        });
    }
}
