package com.live.location.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.location.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.live.location.Util.PreferencesUtil;

public class LocationPerm extends AppCompatActivity {


    PreferencesUtil preferencesUtil;
    ImageView btnLocation;
    TextView text;
    Button btn_next,allow;
    String[] per = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(LocationPerm.this);
        setContentView(R.layout.activity_loc);

        btnLocation = findViewById(R.id.btn_loc_back);
        btn_next = findViewById(R.id.btn_loc);
        allow = findViewById(R.id.allow);

        btnLocation.setVisibility(View.GONE);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permissions.check(LocationPerm.this, per, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


                            allow.setVisibility(View.VISIBLE);
                            btn_next.setVisibility(View.GONE);


                                    allow.setVisibility(View.GONE);
                                    btn_next.setVisibility(View.VISIBLE);


                                    Permissions.check(LocationPerm.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION, null, new PermissionHandler() {
                                        @Override
                                        public void onGranted() {


                                            Intent intent = new Intent(LocationPerm.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        }
                                    });


                        }
                        else {

                                Intent intent = new Intent(LocationPerm.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                        }
                    }
                });

            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(LocationPerm.this, FirstActivity.class);
        startActivity(intent);
        finish();
    }

}