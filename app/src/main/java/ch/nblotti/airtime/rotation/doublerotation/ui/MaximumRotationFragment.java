package ch.nblotti.airtime.rotation.doublerotation.ui;

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

import javax.inject.Inject;

import ch.nblotti.airtime.databinding.FragmentMaximumRotationBinding;
import ch.nblotti.airtime.sample.Sample;
import ch.nblotti.airtime.sample.SampleRepository;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import ch.nblotti.airtime.sample.ui.SampleFragmentDirections;
import ch.nblotti.airtime.sample.ui.SampleViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MaximumRotationFragment extends Fragment {

    private FragmentMaximumRotationBinding binding;
    private MaximumRotationCustomAdapter maximumRotationListCustomAdapter;

    public SampleViewModel viewModel;

    private Long session_id = 0L;

    @Inject
    SampleRepository sampleRepository;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMaximumRotationBinding.inflate(inflater, container, false);

        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();

        binding.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        maximumRotationListCustomAdapter = new MaximumRotationCustomAdapter(getActivity());

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
                Sample sample = maximumRotationListCustomAdapter.getItem(viewHolder.getAdapterPosition());
                //TODO remove associated movies
                sampleRepository.deleteById(sample);


            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);


        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        viewModel.getSamples(session_id).observe(getViewLifecycleOwner(), samples -> {


            maximumRotationListCustomAdapter.setSamples(samples);
            maximumRotationListCustomAdapter.notifyDataSetChanged();

        });


        binding.listView.setAdapter(maximumRotationListCustomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SampleFragmentDirections.ActionSampleFragmentToNewVideoFragment action = SampleFragmentDirections.actionSampleFragmentToNewVideoFragment();
                action.setSessionId(session_id);

                NavHostFragment.findNavController(MaximumRotationFragment.this).navigate(action);

            }
        });

        getParentFragmentManager().setFragmentResultListener("measure", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("session_id");
                Integer measure = bundle.getInt("sample_measure");
                // Do something with the result
                int a = 2;
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