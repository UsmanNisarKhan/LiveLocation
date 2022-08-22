package com.live.location.ForgotPass;

import static com.live.location.Util.Utils.hideSoftKeyboard;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.annotations.NotNull;
import com.live.location.R;
import com.live.location.Activities.FirstActivity;
import com.live.location.Login.Login_pass;
import com.live.location.Util.PreferencesUtil;
import com.live.location.databinding.ActivityForgotOtpBinding;
import java.util.concurrent.TimeUnit;

public class Forgot_otp extends AppCompatActivity {


    String verificationCode, phoneNo;
    private FirebaseAuth mAuth;
    ActivityForgotOtpBinding activityForgotOtpBinding;
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(this);

        activityForgotOtpBinding = ActivityForgotOtpBinding.inflate(getLayoutInflater());
        setContentView(activityForgotOtpBinding.getRoot());

        phoneNo = preferencesUtil.getString("number");

        mAuth = FirebaseAuth.getInstance();

        sendVerificationCode();

        activityForgotOtpBinding.fEdit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotOtpBinding.fEdit1.getText().toString().length() == 1) {
                    activityForgotOtpBinding.fEdit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotOtpBinding.fEdit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotOtpBinding.fEdit2.getText().toString().length() == 1) {
                    activityForgotOtpBinding.fEdit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotOtpBinding.fEdit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotOtpBinding.fEdit3.getText().toString().length() == 1) {
                    activityForgotOtpBinding.fEdit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotOtpBinding.fEdit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotOtpBinding.fEdit4.getText().toString().length() == 1) {
                    activityForgotOtpBinding.fEdit5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotOtpBinding.fEdit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotOtpBinding.fEdit5.getText().toString().length() == 1) {
                    activityForgotOtpBinding.fEdit6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotOtpBinding.fEdit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityForgotOtpBinding.fEdit6.getText().toString().isEmpty()) {
                    activityForgotOtpBinding.btnFOtpNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                } else {
                    hideSoftKeyboard(Forgot_otp.this);
                    if (!preferencesUtil.getBoolean("Switch")) {
                        activityForgotOtpBinding.btnFOtpNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activityForgotOtpBinding.btnFOtpNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityForgotOtpBinding.btnFOtpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forgot_otp.this, Login_pass.class));
                finish();
            }
        });

        activityForgotOtpBinding.btnFOtpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activityForgotOtpBinding.fEdit6.getText().toString().isEmpty()) {

                    String code = activityForgotOtpBinding.fEdit1.getText().toString() + activityForgotOtpBinding.fEdit2.getText().toString() + activityForgotOtpBinding.fEdit3.getText().toString() +
                            activityForgotOtpBinding.fEdit4.getText().toString() + activityForgotOtpBinding.fEdit5.getText().toString() + activityForgotOtpBinding.fEdit6.getText().toString();

                    //verifying the code entered manually
                    verifyCode(code);
                } else {
                    Toast.makeText(Forgot_otp.this, Forgot_otp.this.getResources().getString(R.string.otp_required), Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityForgotOtpBinding.fEdit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityForgotOtpBinding.fEdit2.getText().toString().isEmpty()) {

                        activityForgotOtpBinding.fEdit1.requestFocus();
                    }

                }
                return false;
            }
        });

        activityForgotOtpBinding.fEdit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityForgotOtpBinding.fEdit3.getText().toString().isEmpty()) {

                        activityForgotOtpBinding.fEdit2.requestFocus();
                    }

                }
                return false;
            }
        });

        activityForgotOtpBinding.fEdit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityForgotOtpBinding.fEdit4.getText().toString().isEmpty()) {

                        activityForgotOtpBinding.fEdit3.requestFocus();
                    }

                }
                return false;
            }
        });

        activityForgotOtpBinding.fEdit5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityForgotOtpBinding.fEdit5.getText().toString().isEmpty()) {

                        activityForgotOtpBinding.fEdit4.requestFocus();
                    }

                }
                return false;
            }
        });


        activityForgotOtpBinding.fEdit6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityForgotOtpBinding.fEdit6.getText().toString().isEmpty()) {

                        activityForgotOtpBinding.fEdit5.requestFocus();
                    }


                }
                return false;
            }
        });

    }

    private void sendVerificationCode() {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNo)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        verificationCode = s;

                    }

                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {

                            activityForgotOtpBinding.fEdit1.setText(code.charAt(0)+"");
                            activityForgotOtpBinding.fEdit2.setText(code.charAt(1)+"");
                            activityForgotOtpBinding.fEdit3.setText(code.charAt(2)+"");
                            activityForgotOtpBinding.fEdit4.setText(code.charAt(3)+"");
                            activityForgotOtpBinding.fEdit5.setText(code.charAt(4)+"");
                            activityForgotOtpBinding.fEdit6.setText(code.charAt(5)+"");
                        }

                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Toast.makeText(Forgot_otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyCode(String code) {

        if (verificationCode != null) {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, code);
            signInUser(phoneAuthCredential);
        } else {
            Toast.makeText(Forgot_otp.this, Forgot_otp.this.getResources().getString(R.string.verify), Toast.LENGTH_SHORT).show();
        }

    }

    private void signInUser(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(Forgot_otp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Forgot_otp.this, Forgot_pass.class);
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(Forgot_otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

            Intent intent = new Intent(Forgot_otp.this, FirstActivity.class);
            startActivity(intent);
            finish();

    }

}