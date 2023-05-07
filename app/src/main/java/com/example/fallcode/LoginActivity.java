package com.example.fallcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    EditText userNameET;
    EditText passwordET;

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
            Toast.makeText(this, "Be vagy jelentkezve", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
        }

        userNameET = findViewById(R.id.editTextTextEmailAddress);
        passwordET = findViewById(R.id.editTextTextPassword2);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userNameET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();


        Log.d(LOG_TAG, "onPause");
    }

    public void login(View view) {
        String email = userNameET.getText().toString();
        String password = passwordET.getText().toString();

        Log.d(LOG_TAG, "Bejelentkezni próbál: " + email + ", jelszó: " + password);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Sikeres bejelentkezés.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: updateUI(null);
                        }
                    }

                });
    }
}