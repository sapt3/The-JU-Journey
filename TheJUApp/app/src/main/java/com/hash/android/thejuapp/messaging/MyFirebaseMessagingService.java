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
import com.hash.android.thejuapp.FacebookLogin;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.R;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;
import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;
import static com.hash.android.thejuapp.messaging.NotificationReciever.ACTION_BOOKMARK;
import static com.hash.android.thejuapp.messaging.NotificationReciever.ACTION_SHARE;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String NOTIIFICATION_ID_EXTRA = "notificationIdextra";
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    PreferenceManager mPrefs;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d("Message", from);
        Map<String, String> data = remoteMessage.getData();

        String type = data.get("type");
        mPrefs = new PreferenceManager(this);
        switch (Integer.parseInt(type)) {
            case 0:
                if (mPrefs.getPrefsUserNotificationNews()) {
                    String heading = data.get("heading");
                    String time = data.get("time");
                    String author = data.get("author");
                    String imageURL = data.get("imageURL");
                    String longDesc = data.get("longDesc");
                    String shortDesc = data.get("shortDesc");
                    String key = data.get("key");
                    String logo = data.get("logo");
                    String club = data.get("club");
                    String uids = data.get("contact");
                    Feed feed = new Feed(time, imageURL, author, heading, shortDesc, longDesc, logo, club, uids);
                    sendNotification(shortDesc, heading, feed, key, imageURL);
                } else {
                    Log.i(TAG, "Notification disabled");
                }
                break;

            case 1:
                int priority = Integer.parseInt(data.get("priority"));
                String title = data.get("title");
                boolean isClassEnabled = mPrefs.getPrefsUserNotificationClass();
                boolean isDepartmentEnabled = mPrefs.getPrefsUserNotificationDepartment();
                boolean isUniversityEnabled = mPrefs.getPrefsUserNotificationUniversity();
                long timeOfExpiry = Long.parseLong(data.get("timeOfExpiry"));

                switch (priority) {
                    case 0:
                        if (isClassEnabled) sendUpdate(priority, title, timeOfExpiry);
                        else Log.d(TAG, "Notification disabled");
                        break;

                    case 1:
                        if (isDepartmentEnabled) sendUpdate(priority, title, timeOfExpiry);
                        else Log.d(TAG, "Notification disabled");
                        break;

                    case 3:
                        if (isUniversityEnabled) sendUpdate(priority, title, timeOfExpiry);
                        else Log.d(TAG, "Notification disabled");
                        break;

                    default:

                }
                break;

        }


    }

    private void sendUpdate(int priority, String title, long timeOfExpiry) {
        PreferenceManager mPrefs = new PreferenceManager(this);
        int id = mPrefs.getNotificationId();
        int pendingIntentId = mPrefs.getPendingIntentId();
        Intent i = new Intent(getApplicationContext(), FacebookLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(DetailsFeedActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(i);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String visiblity = "";
        switch (priority) {
            case 0:
                visiblity = "Class Updates";
                break;
            case 1:
                visiblity = "Department Updates";
                break;
            case 2:
                visiblity = "Campus Updates";
                break;

            case 3:
                visiblity = "University Updates";
                break;
            default:
                return;
        }
        NotificationCompat.Builder notificationBuilded = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.artboard)
                .setContentTitle(visiblity)
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(sound)
                .setVisibility(VISIBILITY_PUBLIC)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilded.build());


    }

    private void sendNotification(String body, String title, Feed f, String key, String imageURL) {

        PreferenceManager mPrefs = new PreferenceManager(this);
        int id = mPrefs.getNotificationId();
        int pendingIntentId = new PreferenceManager(this).getPendingIntentId();

        Log.d("notificationCreated::", "id::" + id);
        Intent i = new Intent(getApplicationContext(), DetailsFeedActivity.class);
        i.putExtra(INTENT_EXTRA_FEED, f);
        i.putExtra(Intent.EXTRA_TEXT, key);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsFeedActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
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
