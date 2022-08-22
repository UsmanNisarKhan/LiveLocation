package com.live.location.Signup;

import static com.live.location.Util.Utils.removeCode;
import static com.live.location.Util.Utils.sha256;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.live.location.Activities.LocationPerm;
import com.live.location.R;
import com.live.location.Activities.FirstActivity;
import com.live.location.Activities.MainActivity;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityPasswordBinding;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

public class Password extends AppCompatActivity {

    PreferencesUtil preferencesUtil;
    String phoneNo, name, pass, token, regex = "[a-zA-Z0-9 ]+";
    FirebaseFirestore firebaseFirestore;
    int toogle = 0;
    ActivityPasswordBinding activityPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(Password.this);


        activityPasswordBinding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityPasswordBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        activityPasswordBinding.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (toogle == 0) {

                    activityPasswordBinding.editPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    activityPasswordBinding.hide.setImageResource(R.drawable.ic_show);
                    toogle = 1;

                } else {

                    activityPasswordBinding.editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    activityPasswordBinding.hide.setImageResource(R.drawable.ic_hide);
                    toogle = 0;

                }

            }
        });

        activityPasswordBinding.editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityPasswordBinding.editPass.getText().toString().isEmpty()) {
                    activityPasswordBinding.btnPassNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                } else {
                    if (!preferencesUtil.getBoolean("Switch")) {
                        activityPasswordBinding.btnPassNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activityPasswordBinding.btnPassNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityPasswordBinding.btnPassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunc();
            }
        });

        activityPasswordBinding.btnPassNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activityPasswordBinding.editPass.getText().toString().isEmpty()) {

                    if(activityPasswordBinding.editPass.getText().toString().length() > 5) {

                        if (activityPasswordBinding.editPass.getText().toString().matches(regex)) {

                            preferencesUtil.setString("existed", "true");

                            phoneNo = preferencesUtil.getString("number");
                            name = preferencesUtil.getString("name");
                            pass = preferencesUtil.getString("pass");
                            token = preferencesUtil.getString("Token");

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("number", phoneNo);
                            user.put("name", name);
                            user.put("pass", sha256(activityPasswordBinding.editPass.getText().toString()));
                            user.put("Token", token);
                            user.put("remove", removeCode(phoneNo));
                            user.put("lat", 0.0);
                            user.put("lng", 0.0);

                            firebaseFirestore.collection("Member").document(removeCode(phoneNo)).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    startActivity(new Intent(Password.this, LocationPerm.class));
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d("FailureTask", e.getMessage());

                                }
                            });
                        } else {
                            Toast.makeText(Password.this, Password.this.getResources().getString(R.string.valid)+"+(*<>.,))", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Password.this, Password.this.getResources().getString(R.string.length), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(Password.this, Password.this.getResources().getString(R.string.password_required), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        backFunc();
    }

    private void backFunc() {
        startActivity(new Intent(Password.this, FirstActivity.class));
        finish();
    }

}