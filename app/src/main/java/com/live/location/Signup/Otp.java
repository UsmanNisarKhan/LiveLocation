package com.live.location.Signup;

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
import com.live.location.Activities.FirstActivity;
import com.live.location.Util.PreferencesUtil;
import com.live.location.R;
import com.live.location.databinding.ActivityOtpBinding;
import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {

    String verificationCode, phoneNo;
    private FirebaseAuth mAuth;
    PreferencesUtil preferencesUtil;
    ActivityOtpBinding activityOtpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new PreferencesUtil(Otp.this);

        activityOtpBinding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(activityOtpBinding.getRoot());

        phoneNo = preferencesUtil.getString("number");

        mAuth = FirebaseAuth.getInstance();

        sendVerificationCode();

        activityOtpBinding.edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpBinding.edit1.getText().toString().length() == 1) {
                    activityOtpBinding.edit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityOtpBinding.edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpBinding.edit2.getText().toString().length() == 1) {
                    activityOtpBinding.edit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityOtpBinding.edit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpBinding.edit3.getText().toString().length() == 1) {
                    activityOtpBinding.edit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityOtpBinding.edit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpBinding.edit4.getText().toString().length() == 1) {
                    activityOtpBinding.edit5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityOtpBinding.edit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpBinding.edit5.getText().toString().length() == 1) {
                    activityOtpBinding.edit6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityOtpBinding.edit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpBinding.edit6.getText().toString().isEmpty()) {
                    activityOtpBinding.btnOtpNext.setCardBackgroundColor(Color.parseColor("#a9a9a9"));
                } else {
                    hideSoftKeyboard(Otp.this);
                    if (!preferencesUtil.getBoolean("Switch")) {
                        activityOtpBinding.btnOtpNext.setCardBackgroundColor(Color.parseColor("#ff9431"));
                    } else {
                        activityOtpBinding.btnOtpNext.setCardBackgroundColor(Color.parseColor("#424242"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityOtpBinding.edit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityOtpBinding.edit2.getText().toString().isEmpty()) {

                        activityOtpBinding.edit1.requestFocus();
                    }

                }
                return false;
            }
        });

        activityOtpBinding.edit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityOtpBinding.edit3.getText().toString().isEmpty()) {

                        activityOtpBinding.edit2.requestFocus();
                    }

                }
                return false;
            }
        });

        activityOtpBinding.edit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityOtpBinding.edit4.getText().toString().isEmpty()) {

                        activityOtpBinding.edit3.requestFocus();
                    }

                }
                return false;
            }
        });

        activityOtpBinding.edit5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityOtpBinding.edit5.getText().toString().isEmpty()) {

                        activityOtpBinding.edit4.requestFocus();
                    }

                }
                return false;
            }
        });


        activityOtpBinding.edit6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (activityOtpBinding.edit6.getText().toString().isEmpty()) {

                        activityOtpBinding.edit5.requestFocus();
                    }


                }
                return false;
            }
        });


        activityOtpBinding.btnOtpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunc();
            }
        });

        activityOtpBinding.btnOtpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activityOtpBinding.edit6.getText().toString().isEmpty()) {

                    String code = activityOtpBinding.edit1.getText().toString() + activityOtpBinding.edit2.getText().toString() + activityOtpBinding.edit3.getText().toString() +
                            activityOtpBinding.edit4.getText().toString() + activityOtpBinding.edit5.getText().toString() + activityOtpBinding.edit6.getText().toString();

                    //verifying the code entered manually
                    verifyCode(code);
                } else {
                    Toast.makeText(Otp.this, Otp.this.getResources().getString(R.string.otp_required), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void backFunc() {
        Intent intent = new Intent(Otp.this, FirstActivity.class);
        startActivity(intent);
        finish();
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
                            activityOtpBinding.edit1.setText(code.charAt(0)+"");
                            activityOtpBinding.edit2.setText(code.charAt(1)+"");
                            activityOtpBinding.edit3.setText(code.charAt(2)+"");
                            activityOtpBinding.edit4.setText(code.charAt(3)+"");
                            activityOtpBinding.edit5.setText(code.charAt(4)+"");
                            activityOtpBinding.edit6.setText(code.charAt(5)+"");

//                            verifyCode(code);                                         // If you want to verify it automatically
                        }

                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Toast.makeText(Otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Otp.this, Otp.this.getResources().getString(R.string.verify), Toast.LENGTH_SHORT).show();
        }

    }

    private void signInUser(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(Otp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Otp.this, SignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(Otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

            backFunc();


    }

}