package com.example.fallcode;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "scool_notification_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;
    private Context mContext;


    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "fallCode Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Notifications from FallCode application");

        mNotifyManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(mContext, ListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("FallCode Application")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // TODO: ezzel van valami baj, volt hiba vele kapcsolatban, de el sem olvastam csak automatikus javítást nyomtam, de továbbra sem működik
        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel() {
        mNotifyManager.cancel(NOTIFICATION_ID);
    }
}
