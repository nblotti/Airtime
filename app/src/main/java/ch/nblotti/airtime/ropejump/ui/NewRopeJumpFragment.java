package ch.nblotti.airtime.ropejump.ui;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.databinding.FragmentNewRopeJumpBinding;
import ch.nblotti.airtime.ropejump.ROPEJUMP_TYPE;
import ch.nblotti.airtime.ropejump.ui.simpleropejump.SimpleRopeJumpFragmentArgs;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewRopeJumpFragment extends Fragment {

    private FragmentNewRopeJumpBinding binding;

    public NewRopeViewModel viewModel;

    private Long session_id = 0L;

    private boolean reset = Boolean.FALSE;


    private ROPEJUMP_TYPE ropejumpType = ROPEJUMP_TYPE.SIMPLE;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNewRopeJumpBinding.inflate(inflater, container, false);


        session_id = SimpleRopeJumpFragmentArgs.fromBundle(getArguments()).getSessionId();
        ropejumpType = ROPEJUMP_TYPE.valueOf(SimpleRopeJumpFragmentArgs.fromBundle(getArguments()).getRopejumpType());


        viewModel = new ViewModelProvider(this).get(NewRopeViewModel.class);

        binding.exerciceResult.setSelection(2);
        binding.exerciceResult.setEnabled(false);

        binding.exerciceNumber.setMinValue(0);
        binding.exerciceNumber.setMaxValue(70);
        binding.exerciceNumber.setValue(50);
        binding.exerciceNumber.setWrapSelectorWheel(true);
        binding.exerciceNumber.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < 35) {
                    binding.exerciceResult.setSelection(0);
                } else if (newVal <= 54) {
                    binding.exerciceResult.setSelection(1);
                } else {
                    binding.exerciceResult.setSelection(2);
                }
            }
        });

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle result = new Bundle();
                result.putLong("session_id", session_id);
                result.putInt("number", binding.exerciceNumber.getValue());
                result.putInt("result", EXERCICE_STATUS.stringValueOf(binding.exerciceResult.getSelectedItem().toString()).getValue());
                result.putInt("type", ropejumpType.getValue());
                getParentFragmentManager().setFragmentResult("ropeJump", result);
                NavController navController = NavHostFragment.findNavController(NewRopeJumpFragment.this);
                navController.popBackStack();


            }
        });

        binding.doneStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = Boolean.FALSE;
                binding.doneStart.setVisibility(View.GONE);
                binding.doneButton.setVisibility(View.GONE);
                binding.doneReset.setVisibility(View.VISIBLE);
                startTimer(15);
            }
        });

        binding.doneReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doReset();
            }
        });
        return binding.getRoot();

    }

    private void doReset() {
        binding.doneReset.setVisibility(View.GONE);
        binding.doneStart.setVisibility(View.VISIBLE);
        binding.doneButton.setVisibility(View.VISIBLE);
        binding.exerciceSeconds.setText(String.valueOf(this.ropejumpType.getTime()));
        reset = Boolean.TRUE;
    }

    public final void startTimer(int current) {

        int situation = current - 1;


        final TimerTask tt = new TimerTask() {

            @Override
            public void run() {

                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 5000);


                if (binding != null && !reset) {
                    binding.exerciceSeconds.setText(String.valueOf(situation));

                    if (situation <= 3 && situation > 0)
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_LOW_L, 500);

                    if (situation != 0)
                        startTimer(situation);
                    else {
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_LOW_L, 1000);
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                doReset();

                            }
                        });

                    }

                }
            }

        };

        if (!reset)
            new

                    Timer().

                    schedule(tt, 1000);

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        binding = null;
    }

}