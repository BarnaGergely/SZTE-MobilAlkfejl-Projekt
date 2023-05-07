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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddArticleActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    EditText titleET;
    EditText contentET;

    private FirebaseAuth mAuth;

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        mAuth = FirebaseAuth.getInstance();

        titleET = findViewById(R.id.editTextTextAddTitle);
        contentET = findViewById(R.id.editTextTextAddContent);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    public void addArticle(View view) {
        String title = titleET.getText().toString();
        String content = contentET.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Article article = new Article(title, content, false);
        boolean sucsess = false;
        // Add a new document with a generated ID
        db.collection("articles")
                .add(article)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Siker",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddArticleActivity.this, ListActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Nem sikerült",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("title", titleET.getText().toString());
        editor.putString("content", contentET.getText().toString());
        editor.apply();

        Log.d(LOG_TAG, "onPause");
    }
}