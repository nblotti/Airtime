package ch.nblotti.airtime.ropejump.ui.doublej;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.databinding.FragmentDoubleRopeJumpBinding;
import ch.nblotti.airtime.ropejump.ROPEJUMP_TYPE;
import ch.nblotti.airtime.ropejump.RopeJump;
import ch.nblotti.airtime.ropejump.RopeJumpRepository;
import ch.nblotti.airtime.ropejump.ui.simplej.SimpleRopeJumpFragmentDirections;
import ch.nblotti.airtime.rotation.controlledrotation.ui.ControlledRotationFragmentDirections;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DoubleRopeJumpFragment extends Fragment {

    private FragmentDoubleRopeJumpBinding binding;
    private DoubleRopeJumpCustomAdapter doubleRopeJumpCustomAdapter;

    public DoubleRopeViewModel viewModel;

    private Long session_id = 0L;


    @Inject
    RopeJumpRepository ropeJumpRepository;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDoubleRopeJumpBinding.inflate(inflater, container, false);

        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();


        binding.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doubleRopeJumpCustomAdapter = new DoubleRopeJumpCustomAdapter(getActivity());

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
                RopeJump sample = doubleRopeJumpCustomAdapter.getItem(viewHolder.getAdapterPosition());
                //TODO remove associated movies
                ropeJumpRepository.deleteById(sample.getUid());


            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);


        viewModel = new ViewModelProvider(this).get(DoubleRopeViewModel.class);

        viewModel.getAllRopeJump(session_id, ROPEJUMP_TYPE.DOUBLE).observe(getViewLifecycleOwner(), samples -> {


            doubleRopeJumpCustomAdapter.setSamples(samples);
            doubleRopeJumpCustomAdapter.notifyDataSetChanged();

        });


        binding.listView.setAdapter(doubleRopeJumpCustomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DoubleRopeJumpFragmentDirections.ActionDoubleRopeJumpFragmentToNewRopeJumpFragment action = DoubleRopeJumpFragmentDirections.actionDoubleRopeJumpFragmentToNewRopeJumpFragment();
                action.setSessionId(session_id);
                action.setRopejumpType(ROPEJUMP_TYPE.DOUBLE.getValue());
                action.setMin(10);
                action.setMax(24);
                NavHostFragment.findNavController(DoubleRopeJumpFragment.this).navigate(action);

            }
        });


        getParentFragmentManager().setFragmentResultListener("ropeJump", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                Long sessionId = bundle.getLong("session_id");
                Integer measure = bundle.getInt("number");
                EXERCICE_STATUS result = EXERCICE_STATUS.valueOf(bundle.getInt("result"));
                ROPEJUMP_TYPE type = ROPEJUMP_TYPE.valueOf(bundle.getInt("type"));

                RopeJump ropeJump = new RopeJump(sessionId, type, result, measure, result.getPoints());
                ropeJumpRepository.save(ropeJump);


            }
        });


        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}