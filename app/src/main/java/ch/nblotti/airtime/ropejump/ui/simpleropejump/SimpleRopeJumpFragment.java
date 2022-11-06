package ch.nblotti.airtime.ropejump.ui.simpleropejump;

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

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.databinding.FragmentSimpleRopeJumpBinding;
import ch.nblotti.airtime.ropejump.ROPEJUMP_TYPE;
import ch.nblotti.airtime.ropejump.RopeJump;
import ch.nblotti.airtime.ropejump.RopeJumpRepository;
import ch.nblotti.airtime.rotation.controlledrotation.ui.ControlledRotationFragment;
import ch.nblotti.airtime.rotation.controlledrotation.ui.ControlledRotationFragmentDirections;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SimpleRopeJumpFragment extends Fragment {

    private FragmentSimpleRopeJumpBinding binding;
    private SimpleRopeJumpCustomAdapter simpleRopeJumpCustomAdapter;

    public SimpleRopeViewModel viewModel;

    private Long session_id = 0L;


    @Inject
    RopeJumpRepository ropeJumpRepository;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSimpleRopeJumpBinding.inflate(inflater, container, false);

        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();


        binding.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        simpleRopeJumpCustomAdapter = new SimpleRopeJumpCustomAdapter(getActivity());

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
                RopeJump sample = simpleRopeJumpCustomAdapter.getItem(viewHolder.getAdapterPosition());
                //TODO remove associated movies
                ropeJumpRepository.deleteById(sample.getUid());


            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);


        viewModel = new ViewModelProvider(this).get(SimpleRopeViewModel.class);

        viewModel.getAllRopeJump(session_id, ROPEJUMP_TYPE.SIMPLE).observe(getViewLifecycleOwner(), samples -> {


            simpleRopeJumpCustomAdapter.setSamples(samples);
            simpleRopeJumpCustomAdapter.notifyDataSetChanged();

        });


        binding.listView.setAdapter(simpleRopeJumpCustomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleRopeJumpFragmentDirections.ActionSimpleRopeJumpFragmentToNewRopeJumpFragment action = SimpleRopeJumpFragmentDirections.actionSimpleRopeJumpFragmentToNewRopeJumpFragment();
                action.setSessionId(session_id);
                action.setRopejumpType(ROPEJUMP_TYPE.SIMPLE.getValue());
                NavHostFragment.findNavController(SimpleRopeJumpFragment.this).navigate(action);

            }
        });


        EventBus.getDefault().register(this);


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        Long session;
        switch (event.getEventType()) {
            case UPDATE_CONTROLLED_ROTATION:
                ControlledRotationFragmentDirections.ActionControlledRotationFragmentToControlledRotationDetailFragment action = ControlledRotationFragmentDirections.actionControlledRotationFragmentToControlledRotationDetailFragment();
                action.setControlledRotationId(event.getuID());
                NavHostFragment.findNavController(SimpleRopeJumpFragment.this).navigate(action);
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