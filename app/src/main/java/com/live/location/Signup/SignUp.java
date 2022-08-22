package com.live.location.Signup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.live.location.R;
import com.live.location.Activities.FirstActivity;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;
    PreferencesUtil preferencesUtil;
    String regex = "[a-zA-Z0-9 ]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(SignUp.this);

        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        activitySignUpBinding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activitySignUpBinding.editName.getText().toString().isEmpty()) {
                    activitySignUpBinding.btnSignupNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                } else {

                    if (!preferencesUtil.getBoolean("Switch")) {
                        activitySignUpBinding.btnSignupNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activitySignUpBinding.btnSignupNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activitySignUpBinding.btnSignupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunc();
            }
        });

        activitySignUpBinding.btnSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activitySignUpBinding.editName.getText().toString().isEmpty() && activitySignUpBinding.editName.getText().toString().matches(regex)) {

                    preferencesUtil.setString("name", activitySignUpBinding.editName.getText().toString());

                    startActivity(new Intent(SignUp.this, Password.class));
                    finish();
                } else {
                    Toast.makeText(SignUp.this, SignUp.this.getResources().getString(R.string.name), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void backFunc() {
        startActivity(new Intent(SignUp.this, FirstActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        backFunc();
    }
}