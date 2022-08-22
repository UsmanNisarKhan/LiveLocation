package com.live.location.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.live.location.Login.Login;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity {

    ActivityFirstBinding activityFirstBinding;
    PreferencesUtil preferencesUtil;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(FirstActivity.this);

        activityFirstBinding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(activityFirstBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        if (!preferencesUtil.getString("number").equals("") && !preferencesUtil.getString("existed").equals("")) {

            if (ActivityCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(FirstActivity.this, LocationPerm.class));
                finish();
            } else {
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                finish();
            }
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.i("tag", token);

                        preferencesUtil.setString("Token", token);
                    }
                });


        activityFirstBinding.btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FirstActivity.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();

            }
        });


    }


    @Override
    public void onBackPressed() {

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

}