package com.acme.a3csci3130;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Static firebase references
 */

public class MyApplicationData extends Application {

    public static DatabaseReference firebaseReference;
    public static FirebaseDatabase firebaseDBInstance;

}
