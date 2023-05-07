package com.example.fallcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /*
    * TODO: még egy animáció
    * DONE: On pause mentés minden intentbe
    * */

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY = 99;
    private FirebaseAuth mAuth;
    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView simpleTextView = (TextView) findViewById(R.id.textView2);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            simpleTextView.setText("Be vagy jelentkezve " + user.getDisplayName() + " , " + user.getEmail());
        } else {
            simpleTextView.setText("Nem vagy bejelentkezve.");
        }


        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            //finish();
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    public void login(View view) {
        // átlépés másik activity-be
        Intent intent = new Intent(this, LoginActivity.class).putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void register(View view) {
        // átlépés másik activity-be
        Intent intent = new Intent(this, RegisterActivity.class).putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navLogout) {
            Log.d(LOG_TAG, "Logout clicked!");
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.navSettings) {
            Log.d(LOG_TAG, "Settings clicked!");
            setAlarmManager();
            return true;
        } else if (itemId == R.id.navLogin) {
            Log.d(LOG_TAG, "Login clicked");
            Intent intent = new Intent(this, LoginActivity.class).putExtra("SECRET_KEY", SECRET_KEY);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.navRegister) {
            Log.d(LOG_TAG, "Register clicked");
            Intent intent = new Intent(this, RegisterActivity.class).putExtra("SECRET_KEY", SECRET_KEY);
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

    public void articles(View view) {
        // átlépés másik activity-be
        Intent intent = new Intent(this, ListActivity.class).putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    private void setAlarmManager() {
        long repeatInterval = 5000; // AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent);

        Toast.makeText(this, "Értesítések bekapcsolva", Toast.LENGTH_LONG).show();

        // TODO: Értesítések megjavítása

        //mAlarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PREF_KEY", PREF_KEY);
        editor.apply();


        Log.d(LOG_TAG, "onPause");
    }
}