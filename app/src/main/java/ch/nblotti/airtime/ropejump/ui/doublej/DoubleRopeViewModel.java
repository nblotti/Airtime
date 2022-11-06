package ch.nblotti.airtime.ropejump.ui.doublej;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ch.nblotti.airtime.ropejump.ROPEJUMP_TYPE;
import ch.nblotti.airtime.ropejump.RopeJump;
import ch.nblotti.airtime.ropejump.RopeJumpRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DoubleRopeViewModel extends ViewModel {

    RopeJumpRepository ropeJumpRepository;


    @Inject
    public DoubleRopeViewModel(RopeJumpRepository ropeJumpRepository) {
        this.ropeJumpRepository = ropeJumpRepository;

    }


    public LiveData<List<RopeJump>> getAllRopeJump(long sessionId, ROPEJUMP_TYPE ropejump_type) {

        return ropeJumpRepository.getAllBySesssionIdAndType(sessionId, ropejump_type);
    }

}
