package com.rikkei.tranning.chatapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyAppOFFLINE extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
