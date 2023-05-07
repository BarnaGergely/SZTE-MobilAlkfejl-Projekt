package com.example.fallcode;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;

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

public class EditArticleActivity extends AppCompatActivity {

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    private static final String LOG_TAG = MainActivity.class.getName();
    EditText titleET;
    EditText contentET;
    //EditText idET;

    String id;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        mAuth = FirebaseAuth.getInstance();

        titleET = findViewById(R.id.editTextTextEditTitle);
        contentET = findViewById(R.id.editTextTextEditContent);
       //idET = findViewById(R.id.editTextTextEditId);

        id = getIntent().getStringExtra("ID");

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    public void editArticle(View view) {
        String title = titleET.getText().toString();
        String content = contentET.getText().toString();
        //String id = idET.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference washingtonRef = db.collection("articles").document(id);

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update(
                        "title", title,
                        "content", content
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(getApplicationContext(), "Siker",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditArticleActivity.this, ListActivity.class).putExtra("ID", 0);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error updating document", e);
                        Toast.makeText(getApplicationContext(), "Hiba",
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