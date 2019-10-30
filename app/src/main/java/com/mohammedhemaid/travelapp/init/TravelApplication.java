package com.mohammedhemaid.travelapp.init;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class TravelApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
