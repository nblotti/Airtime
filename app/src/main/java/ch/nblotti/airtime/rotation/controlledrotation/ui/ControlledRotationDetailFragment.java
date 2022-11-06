package ch.nblotti.airtime.rotation.controlledrotation.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.text.DecimalFormat;

import javax.inject.Inject;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.databinding.FragmentControlledRotationDetailBinding;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;
import ch.nblotti.airtime.video.ui.NewVideoFragment;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ControlledRotationDetailFragment extends Fragment {


    private FragmentControlledRotationDetailBinding binding;

    public ControlledRotationDetailViewModel viewModel;


    @Inject
    DecimalFormat doubleFormat;


    public ControlledRotationDetailFragment() {


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentControlledRotationDetailBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ControlledRotationDetailViewModel.class);


        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(ControlledRotationDetailFragment.this);
                navController.popBackStack();

            }
        });

        viewModel.getCurrentControlledRotation().observe(getViewLifecycleOwner(), session -> {
            if (session == null)
                return;
            binding.airTime.setText(session.getAirTime().toString());
            binding.controlledRotationId.setText(String.valueOf(session.getUid()));
            setSpinnerSelection(session.getStatus());

        });


        binding.exerciceResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!binding.controlledRotationId.getText().toString().isEmpty() )
                    viewModel.setStatus(Long.valueOf(binding.controlledRotationId.getText().toString()), EXERCICE_STATUS.stringValueOf(binding.exerciceResult.getAdapter().getItem(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return binding.getRoot();
    }

    private void setSpinnerSelection(EXERCICE_STATUS status) {

        int position = (((ArrayAdapter) binding.exerciceResult.getAdapter()).getPosition(status.getStringValue()));
        binding.exerciceResult.setSelection(position);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.register();

        long currentRotationId = ControlledRotationFragmentArgs.fromBundle(getArguments()).getControlledRotationId();
        if (currentRotationId != 0) {
            viewModel.setId(currentRotationId);
        } else {
            ControlledRotation cr = new ControlledRotation();
            cr.airTime = ControlledRotationFragmentArgs.fromBundle(getArguments()).getAirTime();
            cr.sessionId = ControlledRotationFragmentArgs.fromBundle(getArguments()).getSessionId();
            cr.status = EXERCICE_STATUS.SUFFISANT;
            viewModel.controlledRotationRepository.save(cr);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.unRegister();
    }
}