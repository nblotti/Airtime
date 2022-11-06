package ch.nblotti.airtime.rotation.controlledrotation.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotationRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ControlledRotationViewModel extends ViewModel {


    ;
    ControlledRotationRepository controlledRotationRepository;


    @Inject
    public ControlledRotationViewModel(ControlledRotationRepository controlledRotationRepository) {
        this.controlledRotationRepository = controlledRotationRepository;

    }


    public LiveData<List<ControlledRotation>> getAllControlledRotation(long sessionId) {

        return controlledRotationRepository.getAllBySesssionId(sessionId);
    }





}
