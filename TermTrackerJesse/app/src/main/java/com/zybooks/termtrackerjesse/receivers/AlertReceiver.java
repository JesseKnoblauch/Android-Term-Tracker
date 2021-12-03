package com.zybooks.termtrackerjesse.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.zybooks.termtrackerjesse.R;

public class AlertReceiver extends BroadcastReceiver {
    private String ID = "CHANNEL_ID";
    private String DESC = "Alert Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("NOTIFICATION_TITLE");
        String text = intent.getStringExtra("NOTIFICATION_TEXT");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(ID, DESC, NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(0, builder.build());
    }
}
