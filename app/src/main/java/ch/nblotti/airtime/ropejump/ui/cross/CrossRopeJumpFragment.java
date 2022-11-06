package ch.nblotti.airtime.ropejump.ui.cross;

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

import javax.inject.Inject;

import ch.nblotti.airtime.EXERCICE_STATUS;
import ch.nblotti.airtime.databinding.FragmentCrossRopeJumpBinding;
import ch.nblotti.airtime.databinding.FragmentDoubleRopeJumpBinding;
import ch.nblotti.airtime.ropejump.ROPEJUMP_TYPE;
import ch.nblotti.airtime.ropejump.RopeJump;
import ch.nblotti.airtime.ropejump.RopeJumpRepository;
import ch.nblotti.airtime.ropejump.ui.doublej.DoubleRopeJumpFragmentDirections;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CrossRopeJumpFragment extends Fragment {

    private FragmentCrossRopeJumpBinding binding;
    private CrossRopeJumpCustomAdapter crossRopeJumpCustomAdapter;

    public CrossRopeViewModel viewModel;

    private Long session_id = 0L;


    @Inject
    RopeJumpRepository ropeJumpRepository;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCrossRopeJumpBinding.inflate(inflater, container, false);

        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();


        binding.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        crossRopeJumpCustomAdapter = new CrossRopeJumpCustomAdapter(getActivity());

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
                RopeJump sample = crossRopeJumpCustomAdapter.getItem(viewHolder.getAdapterPosition());
                //TODO remove associated movies
                ropeJumpRepository.deleteById(sample.getUid());


            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);


        viewModel = new ViewModelProvider(this).get(CrossRopeViewModel.class);

        viewModel.getAllRopeJump(session_id, ROPEJUMP_TYPE.CROISES).observe(getViewLifecycleOwner(), samples -> {


            crossRopeJumpCustomAdapter.setSamples(samples);
            crossRopeJumpCustomAdapter.notifyDataSetChanged();

        });


        binding.listView.setAdapter(crossRopeJumpCustomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CrossRopeJumpFragmentDirections.ActionCrossRopeJumpFragmentToNewRopeJumpFragment action = CrossRopeJumpFragmentDirections.actionCrossRopeJumpFragmentToNewRopeJumpFragment();
                action.setSessionId(session_id);
                action.setRopejumpType(ROPEJUMP_TYPE.CROISES.getValue());
                action.setMin(10);
                action.setMax(20);
                NavHostFragment.findNavController(CrossRopeJumpFragment.this).navigate(action);

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