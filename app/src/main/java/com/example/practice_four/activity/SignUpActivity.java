package com.example.practice_four.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practice_four.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextUser;
    private EditText mEditTextPassword;
    private EditText mEditTextPasswordConfirm;
    private Button mSignUpButton;

    /**
     * Class the provides access to the Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditTextUser = findViewById(R.id.sign_up_edit_text_user);
        mEditTextPassword = findViewById(R.id.sign_up_edit_text_password);
        mEditTextPasswordConfirm = findViewById(R.id.sign_up_edit_text_password_confirm);
        mSignUpButton = findViewById(R.id.sign_up_button_sign_up);

        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /** Implement the integration with Firebase Authentication */
        String email = mEditTextUser.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String confirmPassword = mEditTextPasswordConfirm.getText().toString();

        /** Create an instance of the Firebase Authentication component */
        mFirebaseAuth = FirebaseAuth.getInstance();

        /** Make sure passwords match */
        if (!confirmPassword.equals(password)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
            return;
        }

        /** Check if the user email and password are valid */
        if (TextUtils.isEmpty(email)) {
            mEditTextUser.setHint("An email is required.");
            mEditTextUser.setHintTextColor(Color.RED);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mEditTextPassword.setHint("Please type the correct password.");
            mEditTextPassword.setHintTextColor(Color.RED);
            return;
        }

        switch (v.getId()){
            case R.id.sign_up_button_sign_up: SignUp(email, password); break;
        }
    }

    private void SignUp(String email, String password){
        /** If the user's input have the correct information, we need to invoke the
         * createUserWithEmailAndPassword method.*/
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.putExtra("email", user.getEmail());
                            Toast.makeText(SignUpActivity.this, "You have successfully signed up", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }
}