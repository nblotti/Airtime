package ch.nblotti.airtime.sample.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.nblotti.airtime.sample.Sample;
import ch.nblotti.airtime.sample.SampleRepository;
import ch.nblotti.airtime.session.Session;
import ch.nblotti.airtime.session.SessionRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SampleViewModel extends ViewModel {


    ;
    SampleRepository samplesRepository;


    @Inject
    public SampleViewModel(SampleRepository samplesRepository) {
        this.samplesRepository = samplesRepository;

    }


    public LiveData<List<Sample>> getSamples(long sessionId) {

        return samplesRepository.getAllBySesssionId(sessionId);
    }

}
