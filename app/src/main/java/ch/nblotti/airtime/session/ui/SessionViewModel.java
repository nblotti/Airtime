package ch.nblotti.airtime.session.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import ch.nblotti.airtime.session.Session;
import ch.nblotti.airtime.session.SessionRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SessionViewModel extends ViewModel {


    LiveData<List<Session>> session;
    SessionRepository sessionRepository;


    @Inject
    public SessionViewModel(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        this.session = sessionRepository.getAll();
    }

    public LiveData<List<Session>> getSession() {
        return session;
    }


    public void deleteSessionById(int itemId) {
                sessionRepository.deleteById(itemId);

    }
}
