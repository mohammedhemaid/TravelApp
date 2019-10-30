package com.mohammedhemaid.travelapp.init;

import androidx.appcompat.app.AppCompatActivity;

import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.ui.MainActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {

    @AfterViews
    public void after() {
        MainActivity_.intent(this).start();
        finish();
    }
}
