package com.example.fallcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY = 99;

    private FirebaseAuth mAuth;

    EditText editTextUsername;
    EditText editTextTextEmail;
    EditText editTextTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key != 99) {
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
            Toast.makeText(this, "Be vagy jelentkezve", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextTextEmail = findViewById(R.id.editTextTextEmail);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        //TODO: updateUI(currentUser);
    }

    public void register(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            String username = editTextUsername.getText().toString();
            String email = editTextTextEmail.getText().toString();
            String pwd = editTextTextPassword.getText().toString();

            Log.i(LOG_TAG, "Login: " + username + " , " + pwd + " , " + email);

            mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Sikeresen létrejött a felhasználó");
                        Toast.makeText(getApplicationContext(), "Sikeresen létrejött a felhasználó", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Log.d(LOG_TAG, "Hiba a felhasználó létrehozásakor");
                        Toast.makeText(getApplicationContext(), "Hiba a felhasználó létrehozásakor", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(LOG_TAG, "createUserWithEmail:success");
                                Toast.makeText(getApplicationContext(), "Sikeresen létrejött a felhasználó", Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //TODO: updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //TODO: updateUI(null);
                            }
                        }
                    });
        } else {
            Log.d(LOG_TAG, "Már van felhasználód.");
            Toast.makeText(getApplicationContext(), "Már van felhasználód", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", editTextUsername.getText().toString());
        editor.putString("password", editTextTextPassword.getText().toString());
        editor.putString("email",  editTextTextEmail.getText().toString());

        editor.apply();


        Log.d(LOG_TAG, "onPause");
    }
}