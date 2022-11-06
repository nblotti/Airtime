package ch.nblotti.airtime.session.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import ch.nblotti.airtime.databinding.ActivitySessionDetailBinding;

public class SessionDetailActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySessionDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySessionDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("colorcode", "1" );

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

}