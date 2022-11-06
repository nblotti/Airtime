package ch.nblotti.airtime.video.ui;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.common.util.concurrent.ListenableFuture;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import ch.nblotti.airtime.MessageEvent;
import ch.nblotti.airtime.R;
import ch.nblotti.airtime.databinding.FragmentNewVideoBinding;
import ch.nblotti.airtime.sample.SampleRepository;
import ch.nblotti.airtime.sample.ui.SampleFragmentArgs;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewVideoFragment extends Fragment implements TextureView.SurfaceTextureListener {
    //implements TextureView.SurfaceTextureListener {


    public static final String TAG = "FirstFragment";
    private static final int FPS = 30;
    public static final int NEXT_STEP = Integer.valueOf(1000 / FPS);

    private MediaPlayer mediaPlayer;
    private int start_pos = 0;

    private boolean recording;


    private FragmentNewVideoBinding binding;
    // private MediaPlayer mediaPlayer;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private VideoCapture videoCapture;


    @Inject
    SampleRepository sampleRepository;
    Long session_id = 0L;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void prepareCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, getExecutor());
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(getActivity());
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNewVideoBinding.inflate(inflater, container, false);
        session_id = SampleFragmentArgs.fromBundle(getArguments()).getSessionId();
        EventBus.getDefault().register(this);


        binding.recordstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manageRecording();


            }
        });


        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            mediaPlayer.seekTo(0, MediaPlayer.SEEK_CLOSEST);
                        else
                            mediaPlayer.seekTo((int) 0);


                    }
                }).start();


            }
        });

        binding.forwardSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int pos = mediaPlayer.getCurrentPosition() + NEXT_STEP <= mediaPlayer.getDuration() ? mediaPlayer.getCurrentPosition() + NEXT_STEP : mediaPlayer.getDuration();
                new Thread(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            mediaPlayer.seekTo(pos, MediaPlayer.SEEK_CLOSEST);
                        else
                            mediaPlayer.seekTo((int) pos);


                    }
                }).start();
            }
        });

        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    public void run() {
                        mediaPlayer.pause();
                    }
                }).start();
            }
        });

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    public void run() {
                        mediaPlayer.start();
                    }
                }).start();
            }
        });

        binding.backSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int pos = mediaPlayer.getCurrentPosition() - NEXT_STEP > 0 ? mediaPlayer.getCurrentPosition() - NEXT_STEP : 0;
                new Thread(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            mediaPlayer.seekTo(pos, MediaPlayer.SEEK_CLOSEST);
                        else
                            mediaPlayer.seekTo((int) pos);


                    }
                }).start();
            }
        });


        binding.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            mediaPlayer.seekTo(mediaPlayer.getDuration(), MediaPlayer.SEEK_CLOSEST);
                        else
                            mediaPlayer.seekTo((int) mediaPlayer.getDuration());


                    }
                }).start();


            }
        });


        binding.save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (binding.difference.getText() == null || binding.difference.getText().equals("0"))
                    return;


                Bundle result = new Bundle();
                result.putLong("session_id", session_id);
                result.putFloat("sample_measure", Float.valueOf(binding.difference.getText().toString()));
                getParentFragmentManager().setFragmentResult("measure", result);
                NavController navController = NavHostFragment.findNavController(NewVideoFragment.this);
                navController.popBackStack();

            }
        });


        return binding.getRoot();

    }

    private void manageRecording() {

        if (!recording) {
            recording = true;
            recordVideo();

        } else {
            recording = false;
            saveVideo();
        }
    }

    private void setUIStateAsRecording() {

        binding.start.setEnabled(Boolean.FALSE);
        binding.forwardSlow.setEnabled(Boolean.FALSE);
        binding.pause.setEnabled(Boolean.FALSE);
        binding.play.setEnabled(Boolean.FALSE);
        binding.backSlow.setEnabled(Boolean.FALSE);
        binding.end.setEnabled(Boolean.FALSE);
        binding.seekBar.setEnabled(Boolean.FALSE);

        binding.ms.setVisibility(View.GONE);
        binding.ms1.setVisibility(View.GONE);
        binding.previewView.setVisibility(View.VISIBLE);
        binding.textureView.setVisibility(View.GONE);
        binding.save.setVisibility(View.GONE);
        binding.recordstop.setVisibility(View.VISIBLE);

    }

    private void setUIStateAsVisualizing() {
        binding.previewView.setVisibility(View.GONE);
        binding.textureView.setVisibility(View.VISIBLE);
        binding.save.setVisibility(View.VISIBLE);
        binding.recordstop.setVisibility(View.GONE);

        binding.ms.setVisibility(View.VISIBLE);
        binding.ms1.setVisibility(View.VISIBLE);
        binding.start.setEnabled(Boolean.TRUE);
        binding.forwardSlow.setEnabled(Boolean.TRUE);
        binding.pause.setEnabled(Boolean.TRUE);
        binding.play.setEnabled(Boolean.TRUE);
        binding.backSlow.setEnabled(Boolean.TRUE);
        binding.end.setEnabled(Boolean.TRUE);
        binding.seekBar.setEnabled(Boolean.TRUE);
    }

    @SuppressLint("RestrictedApi")
    private void saveVideo() {
        videoCapture.stopRecording();

    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(FPS)
                .build();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);


    }

    @SuppressLint({"RestrictedApi", "MissingPermission"})
    private void recordVideo() {
        if (videoCapture != null) {


            long timeStamp = System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");


            videoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(
                            getActivity().getContentResolver(),
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                    ).build(),
                    getExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            doSomeOperations(outputFileResults.getSavedUri());
                            setUIStateAsVisualizing();

                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            setUIStateAsVisualizing();
                        }
                    }

            );

            startTimer(R.color.pink3_background);


        }

    }


    public final void startTimer(int color) {
        final TimerTask tt = new TimerTask() {

            @Override
            public void run() {


                if (binding != null) {
                    binding.recordstop.setBackgroundColor(ContextCompat.getColor(getContext(), color));

                    int a = binding.recordstop.getText() == null || binding.recordstop.getText().toString().isEmpty() ? 0 : Integer.valueOf(binding.recordstop.getText().toString());
                    binding.recordstop.setText(String.valueOf(++a));
                    startTimer(color == R.color.pink2_background ? R.color.pink3_background : R.color.pink2_background);
                }
            }

        };
        new

                Timer().

                schedule(tt, 1000);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        switch (event.getEventType()) {
            case VIDEO_CLICK:

                setDifference();
                break;
        }
    }

    public void setDifference() {

        if (binding.difference.getText() == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.save.setEnabled(Boolean.FALSE);
            }
        });
        if (binding.difference.getText().equals("0")) {
            return;
        }


        Float sampleMeasure = Float.valueOf(binding.difference.getText().toString());
        NewVideoFragment.this.start_pos = mediaPlayer.getCurrentPosition();
        binding.difference.setText("0");


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediaPlayer = new MediaPlayer();


        binding.textureView.setSurfaceTextureListener(this);

        setUIStateAsRecording();
        prepareCamera();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        binding = null;
    }


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {


        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        // mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {


                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        Log.i(TAG, String.format("POSITION : %d", mediaPlayer.getCurrentPosition()));
                        binding.position.setText(String.valueOf(mediaPlayer.getCurrentPosition()));
                        binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());

                        int diff = mediaPlayer.getCurrentPosition() - NewVideoFragment.this.start_pos;
                        diff = diff <= 0 ? 0 : diff;

                        if (diff == 0)
                            binding.save.setEnabled(Boolean.FALSE);
                        else
                            binding.save.setEnabled(Boolean.TRUE);

                        binding.difference.setText(String.valueOf(diff));

                    }
                });

                binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    Boolean tracking = false;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {


                        new Thread(new Runnable() {
                            public void run() {
                                if (tracking)
                                    mediaPlayer.seekTo(progress);

                            }
                        }).start();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        tracking = true;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        tracking = false;
                    }

                });
                mediaPlayer.seekTo(0);
                binding.seekBar.incrementProgressBy(1);
                binding.seekBar.setMax(mediaPlayer.getDuration());
            }
        });

    }

    protected void doSomeOperations(Uri vid) {


        String videoPath = getRealPathFromURI(vid);
        try {
            mediaPlayer.setDataSource(videoPath);

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i,
                                            int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }


}