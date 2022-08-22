package com.live.location.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.live.location.R;
import com.live.location.Activities.LocationPerm;
import com.live.location.ForgotPass.Forgot_otp;
import com.live.location.Activities.MainActivity;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityLoginPassBinding;
import java.util.HashMap;
import static com.live.location.Util.Utils.removeCode;
import static com.live.location.Util.Utils.sha256;

public class Login_pass extends AppCompatActivity {

    String phn, token;
    PreferencesUtil preferencesUtil;
    int toogle = 0;
    ActivityLoginPassBinding activityLoginPassBinding;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(this);


        activityLoginPassBinding = ActivityLoginPassBinding.inflate(getLayoutInflater());
        setContentView(activityLoginPassBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        phn = preferencesUtil.getString("number");


        activityLoginPassBinding.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (toogle == 0) {

                    activityLoginPassBinding.editPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    activityLoginPassBinding.hide.setImageResource(R.drawable.ic_show);
                    toogle = 1;

                } else {

                    activityLoginPassBinding.editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    activityLoginPassBinding.hide.setImageResource(R.drawable.ic_hide);
                    toogle = 0;

                }

            }
        });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        Log.i("taag", token);

                        HashMap<String, Object> user = new HashMap<>();
                        user.put("Token", token);

                        firebaseFirestore.collection("Member").document(removeCode(phn)).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                preferencesUtil.setString("Token", token);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d("FailureTask", e.getMessage());

                            }
                        });
                    }
                });


        firebaseFirestore.collection("Member").document(removeCode(phn)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getData().get("name").toString();
                activityLoginPassBinding.welcome.setText("Welcome! " + name);

            }
        });


        activityLoginPassBinding.editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityLoginPassBinding.editPass.getText().toString().isEmpty()) {
                    activityLoginPassBinding.btnLogPassNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                } else {


                    if (!preferencesUtil.getBoolean("Switch")) {
                        activityLoginPassBinding.btnLogPassNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activityLoginPassBinding.btnLogPassNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityLoginPassBinding.btnLogPassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunc();
            }
        });

        activityLoginPassBinding.btnLogPassNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityLoginPassBinding.passProg.setVisibility(View.VISIBLE);

                firebaseFirestore.collection("Member").document(removeCode(phn)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String pass = documentSnapshot.getData().get("pass").toString();

                        if (pass.equals(sha256(activityLoginPassBinding.editPass.getText().toString()))) {

                            activityLoginPassBinding.passProg.setVisibility(View.GONE);

                            preferencesUtil.setString("number", phn);
                            preferencesUtil.setString("existed", "true");

                            if (ActivityCompat.checkSelfPermission(Login_pass.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                startActivity(new Intent(Login_pass.this, LocationPerm.class));
                                finish();
                            } else {
                                startActivity(new Intent(Login_pass.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            }


                        } else {
                            activityLoginPassBinding.passProg.setVisibility(View.GONE);
                            Toast.makeText(Login_pass.this, Login_pass.this.getResources().getString(R.string.incorrect), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

        activityLoginPassBinding.forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Member").document(removeCode(phn)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String name = documentSnapshot.getData().get("name").toString();

                        preferencesUtil.setString("number", phn);
                        preferencesUtil.setString("name", name);

                        startActivity(new Intent(Login_pass.this, Forgot_otp.class));
                        finish();


                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backFunc();
    }

    private void backFunc() {
        Intent intent = new Intent(Login_pass.this, Login.class);
        startActivity(intent);
        finish();
    }
}