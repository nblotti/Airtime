package ch.nblotti.airtime.rotation.controlledrotation.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.databinding.FragmentControlledRotationBinding;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotation;
import ch.nblotti.airtime.rotation.controlledrotation.ControlledRotationRepository;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ControlledRotationFragment extends Fragment {

    private FragmentControlledRotationBinding binding;
    private ControlledRotationCustomAdapter controlledRotationCustomAdapter;

    public ControlledRotationViewModel viewModel;

    private Long session_id = 0L;

    @Inject
    ControlledRotationRepository controlledRotationRepository;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentControlledRotationBinding.inflate(inflater, container, false);

        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();

        binding.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        controlledRotationCustomAdapter = new ControlledRotationCustomAdapter(getActivity());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Remove item from backing list here
                if (swipeDir != ItemTouchHelper.RIGHT) {
                    return;
                }
                ControlledRotation sample = controlledRotationCustomAdapter.getItem(viewHolder.getAdapterPosition());
                //TODO remove associated movies
                controlledRotationRepository.deleteById(sample.getUid());


            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);


        viewModel = new ViewModelProvider(this).get(ControlledRotationViewModel.class);

        viewModel.getSamples(session_id).observe(getViewLifecycleOwner(), samples -> {


            controlledRotationCustomAdapter.setSamples(samples);
            controlledRotationCustomAdapter.notifyDataSetChanged();

        });


        binding.listView.setAdapter(controlledRotationCustomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ControlledRotationFragmentDirections.ActionControlledRotationFragmentToNewVideoFragment action = ControlledRotationFragmentDirections.actionControlledRotationFragmentToNewVideoFragment();
                action.setSessionId(session_id);

                NavHostFragment.findNavController(ControlledRotationFragment.this).navigate(action);

            }
        });

        getParentFragmentManager().setFragmentResultListener("measure", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                Long result = bundle.getLong("session_id");
                Float measure = bundle.getFloat("sample_measure");

                ControlledRotationFragmentDirections.ActionControlledRotationFragmentToControlledRotationDetailFragment action = ControlledRotationFragmentDirections.actionControlledRotationFragmentToControlledRotationDetailFragment();
                action.setSessionId(session_id);
                action.setAirTime(measure);
                NavHostFragment.findNavController(ControlledRotationFragment.this).navigate(action);

            }
        });

        EventBus.getDefault().register(this);
        return binding.getRoot();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        Long session;
        switch (event.getEventType()) {
            case UPDATE_CONTROLLED_ROTATION:
                ControlledRotationFragmentDirections.ActionControlledRotationFragmentToControlledRotationDetailFragment action = ControlledRotationFragmentDirections.actionControlledRotationFragmentToControlledRotationDetailFragment();
                action.setControlledRotationId(event.getuID());
                NavHostFragment.findNavController(ControlledRotationFragment.this).navigate(action);
                break;
        }
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