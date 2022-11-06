package ch.nblotti.airtime.session;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class SessionRepository {

    private final SessionDao sessionDao;

    public SessionRepository(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }


    public LiveData<List<Session>> getAll() {
        return sessionDao.getAll();
    }

    public void deleteById(Integer id) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                sessionDao.deleteById(id);
            }
        });


    }

    public void save(Session session) {
        sessionDao.insertAll(session);
    }


}
