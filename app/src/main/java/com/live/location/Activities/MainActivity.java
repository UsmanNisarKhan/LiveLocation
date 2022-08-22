package com.live.location.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.live.location.Service.LocationWorker;
import com.live.location.Util.Gps;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityMainBinding;
import static com.live.location.Util.Utils.removeCode;

public class MainActivity extends AppCompatActivity {

    Gps gps;
    FirebaseFirestore firebaseFirestore;
    PreferencesUtil preferencesUtil;
    String phn;
    ActivityMainBinding activityMainBinding;
    NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesUtil = new PreferencesUtil(MainActivity.this);
        gps = new Gps(this);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        phn = preferencesUtil.getString("number");

        Data.Builder data = new Data.Builder();
        data.putString("myPhn", removeCode(phn));


        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(LocationWorker.class)
                        .setInputData(data.build())
                        .build();


        WorkManager
                .getInstance(MainActivity.this)
                .enqueue(uploadWorkRequest);


        activityMainBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesUtil.setString("number", "");
                preferencesUtil.setString("existed", "");
                startActivity(new Intent(MainActivity.this, FirstActivity.class));
                finish();
            }
        });



    }

}