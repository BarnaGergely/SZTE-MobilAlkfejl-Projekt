package com.example.fallcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY = 99;
    private FirebaseAuth mAuth;

    FirebaseFirestore db;
    private CollectionReference mItems;

    private RecyclerView  mRecyclerView;
    private ArrayList<Article>  mItemsData;
    private ArticleAdapter mAdapter;

    private int learnItems = 0;
    private int gridNumber = 1;
    private Integer itemLimit = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            Toast.makeText(this, "Nem vagy bejelentkezve", Toast.LENGTH_LONG).show();
            finish();
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        db = FirebaseFirestore.getInstance();
        mItems = db.collection("articles");

        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ArticleAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        // Get the data.
        queryData();

    }

    public void delete(Article item) {
        DocumentReference ref = mItems.document(item._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + item._getId());
                    Toast.makeText(getApplicationContext(), "Siker",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });

        queryData();
        //mNotificationHelper.cancel();
    }

    public void update(Article item) {

        Intent intent = new Intent(this, EditArticleActivity.class).putExtra("ID", item._getId());
        startActivity(intent);

        queryData();

    }
    private void queryData() {
        mItemsData.clear();
        mItems.orderBy("title").limit(itemLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Article item = document.toObject(Article.class);
                item.setId(document.getId());
                mItemsData.add(item);
            }

            if (mItemsData.size() == 0) {
                initializeData();
                queryData();
            }

            // Notify the adapter of the change.
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] itemsList = {"Alnma", "Répa", "Géza"};
        String[] itemsContent = {"Ez jó", "Mi a csuda", "Van bőr a képeden?"};
        boolean[] itemsState = {true, false, false};


        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsList.length; i++) {
            mItems
                .add(new Article(itemsList[i], itemsContent[i], itemsState[i]))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error adding document", e);
                    }
                });
        }

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navAdd) {
            Log.d(LOG_TAG, "Add clicked!");

            Intent intent = new Intent(this, AddArticleActivity.class).putExtra("SECRET_KEY", SECRET_KEY);
            startActivity(intent);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("learnItems", learnItems);
        editor.apply();

        Log.d(LOG_TAG, "onPause");
    }

}