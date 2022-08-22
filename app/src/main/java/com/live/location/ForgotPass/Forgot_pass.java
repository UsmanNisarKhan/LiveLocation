package com.live.location.ForgotPass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.live.location.R;
import com.live.location.Activities.LocationPerm;
import com.live.location.Login.Login_pass;
import com.live.location.Activities.MainActivity;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityForgotPassBinding;
import java.util.HashMap;
import static com.live.location.Util.Utils.removeCode;
import static com.live.location.Util.Utils.sha256;

public class Forgot_pass extends AppCompatActivity {

    PreferencesUtil preferencesUtil;
    int toogle = 0;
    FirebaseFirestore firebaseFirestore;
    String regex = "[a-zA-Z0-9 ]+";
    ActivityForgotPassBinding activityForgotPassBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(this);

        activityForgotPassBinding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(activityForgotPassBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        activityForgotPassBinding.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (toogle == 0) {
                    activityForgotPassBinding.editFpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    activityForgotPassBinding.hide.setImageResource(R.drawable.ic_show);

                    toogle = 1;
                } else {

                    activityForgotPassBinding.editFpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    activityForgotPassBinding.hide.setImageResource(R.drawable.ic_hide);
                    toogle = 0;

                }
            }
        });

        activityForgotPassBinding.editFpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotPassBinding.editFpass.getText().toString().isEmpty()) {
                    activityForgotPassBinding.btnFpassNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                } else {

                    if (!preferencesUtil.getBoolean("Switch")) {
                        activityForgotPassBinding.btnFpassNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activityForgotPassBinding.btnFpassNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotPassBinding.btnFpassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunc();
            }
        });

        activityForgotPassBinding.btnFpassNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activityForgotPassBinding.editFpass.getText().toString().isEmpty()) {

                    if (activityForgotPassBinding.editFpass.getText().toString().length() > 5) {

                        if (activityForgotPassBinding.editFpass.getText().toString().matches(regex)) {

                            String phoneNo = preferencesUtil.getString("number");

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("pass", sha256(activityForgotPassBinding.editFpass.getText().toString()));

                            firebaseFirestore.collection("Member").document(removeCode(phoneNo)).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    if (ActivityCompat.checkSelfPermission(Forgot_pass.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                        startActivity(new Intent(Forgot_pass.this, LocationPerm.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(Forgot_pass.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        finish();
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d("FailureTask", e.getMessage());

                                }
                            });
                        } else {
                            Toast.makeText(Forgot_pass.this, Forgot_pass.this.getResources().getString(R.string.valid)+"+(*<>.,))", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(Forgot_pass.this, Forgot_pass.this.getResources().getString(R.string.length), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(Forgot_pass.this, Forgot_pass.this.getResources().getString(R.string.password_required), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void backFunc() {
        startActivity(new Intent(Forgot_pass.this, Login_pass.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        backFunc();
    }
}