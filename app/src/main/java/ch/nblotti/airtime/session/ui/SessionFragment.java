package ch.nblotti.airtime.session.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.databinding.FragmentSessionBinding;
import ch.nblotti.airtime.sample.SampleRepository;
import ch.nblotti.airtime.session.Session;
import ch.nblotti.airtime.session.SessionRepository;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SessionFragment extends Fragment {

    private FragmentSessionBinding binding;
    private SessionListCustomAdapter activityListCutomAdapter;

    public SessionViewModel viewModel;
    @Inject
    public SimpleDateFormat simpleDateFormat;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    SampleRepository sampleRepository;

    @Inject
    DecimalFormat doubleFormat;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        Long session;
        switch (event.getEventType()) {
            case JR_S_CLICK:


                break;

            case JR_D_CLICK:
                break;
            case JR_X_CLICK:
                break;

            case R_M_CLICK:
                break;

            case R_C_CLICK:
                session = event.getuID();
                SessionFragmentDirections.ActionSessionFragmentToControlledRotationFragment S2CRAction = SessionFragmentDirections.actionSessionFragmentToControlledRotationFragment();
                S2CRAction.setSessionId(session);
                NavHostFragment.findNavController(SessionFragment.this).navigate(S2CRAction);

                   break;

            case R_D_CLICK:
                session = event.getuID();
                SessionFragmentDirections.ActionSessionFragmentToSampleFragment S2SAction = SessionFragmentDirections.actionSessionFragmentToSampleFragment();
                S2SAction.setSessionId(session);
                NavHostFragment.findNavController(SessionFragment.this).navigate(S2SAction);

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSessionBinding.inflate(inflater, container, false);


        binding.listView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        activityListCutomAdapter = new SessionListCustomAdapter(getActivity(), simpleDateFormat, doubleFormat);

        viewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        viewModel.getSession().observe(getViewLifecycleOwner(), session -> {
            activityListCutomAdapter.setAnimalNames(session);
            activityListCutomAdapter.notifyDataSetChanged();
        });

        EventBus.getDefault().register(this);
        binding.listView.setAdapter(activityListCutomAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SessionDetailActivity.class);
                someActivityResultLauncher.launch(intent);
            }
        });


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
                int value = Integer.valueOf(((SessionListCustomAdapter.ViewHolder) viewHolder).session_id.getText().toString());
                sampleRepository.deleteBySessionId(value);
                sessionRepository.deleteById(value);

            }
        });
        itemTouchHelper.attachToRecyclerView(binding.listView);
        return binding.getRoot();

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


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Session session = new Session();

                session.sessionDate = new Date();
                session.user = "NBL";
                session.sessionAverage = 0d;

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        sessionRepository.save(session);
                    }
                });


                int a = 2;
            }
        }
    });


}