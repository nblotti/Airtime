package ch.nblotti.airtime.rotation.controlledrotation.ui;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotationRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class ControlledRotationDetailViewModel extends ViewModel implements LifecycleEventObserver {


    ;
    ControlledRotationRepository controlledRotationRepository;
    Fragment context;

    private final MutableLiveData<Long> sessionId = new MutableLiveData<>();


    private final LiveData<ControlledRotation> currentControlledRotation =
            Transformations.switchMap(sessionId, (id) -> {
                return controlledRotationRepository.read(id);
            });


    @Inject
    public ControlledRotationDetailViewModel(ControlledRotationRepository controlledRotationRepository) {
        this.controlledRotationRepository = controlledRotationRepository;

    }


    public void setId(long id) {

        sessionId.setValue(id);
    }

    public void save(ControlledRotation controlledRotation) {

        controlledRotationRepository.save(controlledRotation);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        switch (event.getEventType()) {
            case NEW_CONTROLLED_ROTATION:
                sessionId.postValue(event.getuID());

        }

    }

    public void register() {
        EventBus.getDefault().register(this);
    }

    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    public void setStatus(Long key, EXERCICE_STATUS stringValue) {
        controlledRotationRepository.updateStatus(key, stringValue);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

    }

    public LiveData<ControlledRotation> getCurrentControlledRotation() {
        return currentControlledRotation;
    }
}
