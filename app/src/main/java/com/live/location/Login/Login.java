package com.live.location.Login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.live.location.R;
import com.live.location.Activities.FirstActivity;
import com.live.location.Signup.Otp;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityLoginBinding;
import static com.live.location.Util.Utils.removeCode;

public class Login extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    FirebaseFirestore firebaseFirestore;
    PreferencesUtil preferencesUtil;
    String regex = "[0-9 ]+",number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(this);

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        activityLoginBinding.btnLoginNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activityLoginBinding.editPhn.getText().toString().isEmpty() && activityLoginBinding.editPhn.getText().toString().matches(regex)) {

                    activityLoginBinding.loginProg.setVisibility(View.VISIBLE);

                    activityLoginBinding.codePicker.registerCarrierNumberEditText(activityLoginBinding.editPhn);
                    activityLoginBinding.codePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
                        @Override
                        public void onValidityChanged(boolean isValidNumber) {
                            if(isValidNumber)
                            {
                               number = activityLoginBinding.codePicker.getFullNumberWithPlus();
                               firebaseFirestore.collection("Member").document(removeCode(number)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if (documentSnapshot.exists()) {

                                            preferencesUtil.setString("number", number);
                                            preferencesUtil.setString("type", "");
                                            startActivity(new Intent(Login.this, Login_pass.class)
                                                    .putExtra("phn", number));
                                            finish();

                                        } else {

                                            preferencesUtil.setString("number", number);
                                            preferencesUtil.setString("type", "");

                                            activityLoginBinding.loginProg.setVisibility(View.GONE);
                                            startActivity(new Intent(Login.this, Otp.class));
                                            finish();

                                        }

                                    }
                                });

                            }
                            else {

                                activityLoginBinding.loginProg.setVisibility(View.GONE);
                                activityLoginBinding.codePicker.setPhoneNumberValidityChangeListener(null);
                                number = null;
                                Toast.makeText(Login.this, Login.this.getResources().getString(R.string.format), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    activityLoginBinding.loginProg.setVisibility(View.GONE);
                    Toast.makeText(Login.this, Login.this.getResources().getString(R.string.number_required), Toast.LENGTH_SHORT).show();
                }

            }
        });

        activityLoginBinding.editPhn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityLoginBinding.editPhn.getText().toString().isEmpty()) {
                    activityLoginBinding.btnLoginNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                    activityLoginBinding.notFound.setText(Login.this.getResources().getString(R.string.welcome));
                } else {

                    if (!preferencesUtil.getBoolean("Switch")) {
                        activityLoginBinding.btnLoginNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activityLoginBinding.btnLoginNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityLoginBinding.btnLoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunc();
            }
        });

    }

    private void backFunc() {
        startActivity(new Intent(Login.this, FirstActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        backFunc();
    }
}