package com.aic.khidmanow;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FireBase";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("debugging", "Received Remote Message");
        Log.i("debugging", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + "  Build.VERSION_CODES.O " + Build.VERSION_CODES.O);
        Log.i("debugging", "Remote Message title " + remoteMessage.getNotification().getTitle());
        Log.i("debugging", "Remote Message body " + remoteMessage.getNotification().getBody());
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
//        if (body.length() > 0 && body != null) {
        if (body != null && body.length() > 0) {
            displayCustomNotificationForOrders(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else if (title != null && title.length() > 0)
            displayCustomNotificationForOrders(remoteMessage.getNotification().getTitle(), "");
    }

    private void displayCustomNotificationForOrders(String title, String description) {
        Log.d("msg", "onMessageReceived: " + description);
        Bitmap bitmaps = BitmapFactory.decodeResource(getResources(), R.drawable.myicon);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        String channelId = "KHIDMANOW";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setLargeIcon(bitmaps)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmaps)
                        .bigLargeIcon(null))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setContentText(description).setAutoCancel(true).setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationChannel channel = new NotificationChannel(channelId, "khidmanow channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}

