package com.sigma.sanunez.firebaseauthentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";
    private EditText Textemail;
    private EditText TextPassword;
    private EditText TextPassword2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email, pass, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Textemail = (EditText) findViewById(R.id.editText5);
        TextPassword = (EditText) findViewById(R.id.editText7);
        TextPassword2 = (EditText) findViewById(R.id.editText8);

        findViewById(R.id.button2).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(Signup.this, "Thank's For Signing Up", Toast.LENGTH_SHORT).show();
                    toMain();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    private void toMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = Textemail.getText().toString();
        String password = TextPassword.getText().toString();
        String password2 = TextPassword2.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Textemail.setError("Required.");
            valid = false;
        } else {
            Textemail.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            TextPassword.setError("Required.");
            valid = false;
        } else {
            TextPassword.setError(null);
        }
        if (TextUtils.isEmpty(password2)) {
            TextPassword2.setError("Required.");
            valid = false;
        } else {
            TextPassword2.setError(null);
        }

        if(TextUtils.equals(password, password2)) {
            TextPassword.setError(null);
            TextPassword2.setError(null);
        } else {
            TextPassword.setError("Passwords must match.");
            TextPassword2.setError("Passwords must match.");
            valid = false;
        }
        return valid;
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Signup.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }

                    }
                });
        // [END sign_in_with_email]
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button2) {
            email = Textemail.getText().toString();
            pass = TextPassword.getText().toString();
            createAccount(email,pass);
        }

    }
}
