package com.hash.android.thejuapp.messaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.R;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;
import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;
import static com.hash.android.thejuapp.messaging.NotificationReciever.ACTION_BOOKMARK;
import static com.hash.android.thejuapp.messaging.NotificationReciever.ACTION_SHARE;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String NOTIIFICATION_ID_EXTRA = "notificationIdextra";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d("Message", from);

        Map<String, String> data = remoteMessage.getData();
        String heading = data.get("heading");
        String time = data.get("time");
        String author = data.get("author");
        String imageURL = data.get("imageURL");
        String longDesc = data.get("longDesc");
        String shortDesc = data.get("shortDesc");
        String key = data.get("key");

        Feed feed = new Feed(time, imageURL, author, heading, shortDesc, longDesc);

        sendNotification(shortDesc, heading, feed, key, imageURL);
    }

    private void sendNotification(String body, String title, Feed f, String key, String imageURL) {

        int id = new PreferenceManager(this).getNotificationId();
        int pendingIntentId = new PreferenceManager(this).getPendingIntentId();

        Log.d("notificationCreated::", "id::" + id);
        Intent i = new Intent(getApplicationContext(), DetailsFeedActivity.class);
        i.putExtra(INTENT_EXTRA_FEED, f);
        i.putExtra(Intent.EXTRA_TEXT, key);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack
        stackBuilder.addParentStack(DetailsFeedActivity.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(i);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 ,i , PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        try {
            Bitmap theBitmap = Glide.
                    with(this).
                    load(imageURL).
                    asBitmap().
                    fitCenter().
                    into(720, 480). // Width and height
                    get();

            Intent bookmarkIntent = new Intent();
            bookmarkIntent.putExtra(NOTIIFICATION_ID_EXTRA, id);
            bookmarkIntent.setAction(ACTION_BOOKMARK);
            bookmarkIntent.putExtra(INTENT_EXTRA_FEED, f);
            bookmarkIntent.putExtra(Intent.EXTRA_TEXT, key);
            PendingIntent bookmarkPendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, bookmarkIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent shareIntent = new Intent();
            shareIntent.putExtra(NOTIIFICATION_ID_EXTRA, id);
            shareIntent.setAction(ACTION_SHARE);
            shareIntent.putExtra(INTENT_EXTRA_FEED, f);
            shareIntent.putExtra(Intent.EXTRA_TEXT, key);
            PendingIntent sharePendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(theBitmap)
                            .setSummaryText(f.getShortDesc()))
                    .setContentIntent(resultPendingIntent)
                    .setPriority(PRIORITY_HIGH)
                    .addAction(new NotificationCompat.Action(R.drawable.notification_red, "BOOKMARK", bookmarkPendingIntent))
                    .addAction(new NotificationCompat.Action(R.drawable.notification_red, "SHARE", sharePendingIntent))
                    .build();


            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }
}
