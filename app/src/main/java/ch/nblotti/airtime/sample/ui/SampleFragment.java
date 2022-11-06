package ch.nblotti.airtime.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import ch.nblotti.airtime.databinding.FragmentSampleBinding;
import ch.nblotti.airtime.sample.Sample;
import ch.nblotti.airtime.sample.SampleRepository;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SampleFragment extends Fragment {

    private FragmentSampleBinding binding;
    private SampleListCustomAdapter sampleListCustomAdapter;

    public SampleViewModel viewModel;

    private Long session_id = 0L;

    @Inject
    SampleRepository sampleRepository;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSampleBinding.inflate(inflater, container, false);

        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();

        binding.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sampleListCustomAdapter = new SampleListCustomAdapter(getActivity());

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
                Sample sample = sampleListCustomAdapter.getItem(viewHolder.getAdapterPosition());
                //TODO remove associated movies
                sampleRepository.deleteById(sample);


            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);


        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        viewModel.getSamples(session_id).observe(getViewLifecycleOwner(), samples -> {


            sampleListCustomAdapter.setSamples(samples);
            sampleListCustomAdapter.notifyDataSetChanged();

        });


        binding.listView.setAdapter(sampleListCustomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SampleFragmentDirections.ActionSampleFragmentToNewVideoFragment action = SampleFragmentDirections.actionSampleFragmentToNewVideoFragment();
                action.setSessionId(session_id);
                NavHostFragment.findNavController(SampleFragment.this).navigate(action);

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