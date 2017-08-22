package com.hash.android.thejuapp.messaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.EventsDetailsActivity;
import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.Utils.PreferenceManager;
import com.hash.android.thejuapp.WelcomeActivity;

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

                switch (priority) {
                    case 0:
                        if (isClassEnabled) sendUpdate(priority, title);
                        else Log.i(TAG, "Notification disabled");
                        break;

                    case 1:
                        if (isDepartmentEnabled) sendUpdate(priority, title);
                        else Log.i(TAG, "Notification disabled");
                        break;

                    case 3:
                        if (isUniversityEnabled) sendUpdate(priority, title);
                        else Log.i(TAG, "Notification disabled");
                        break;

                    default:

                }
                break;

            case 2:
                //COMPLETED: Add check for notifications enabled.
                if (mPrefs.getPrefsUserEvents()) {
                    String club = data.get("club");
                    String contact = data.get("contact");
                    long endDate = Long.parseLong(data.get("endDate"));
                    String event = data.get("event");
                    String longDesc = data.get("longDesc");
                    String organisation = data.get("organisation");
                    long startDate = Long.parseLong(data.get("startDate"));
                    Event finalEvent = new Event(startDate, endDate, organisation, longDesc, event, club, contact);
                    String key = data.get("key");
                    sendEvent(finalEvent, key);

                } else {
                    Log.i(TAG, "Notification disabled");
                }
                break;
        }


    }

    private void sendEvent(Event finalEvent, String key) {
        PreferenceManager mPrefs = new PreferenceManager(this);
        int id = mPrefs.getNotificationId();
        int pendingIntentId = new PreferenceManager(this).getPendingIntentId();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser() != null && new PreferenceManager(this).isFlowCompleted()) {
            i = new Intent(getApplicationContext(), EventsDetailsActivity.class);
            i.putExtra("KEY", key);
            i.putExtra("EVENT", finalEvent);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsFeedActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(pendingIntentId, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilded = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_name_close)
                .setContentTitle(finalEvent.organisation + ": " + finalEvent.event + ".")
                .setContentText("Click to know more.")
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.icon))
                .setSound(sound)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setPriority(PRIORITY_HIGH)
                .setVisibility(VISIBILITY_PUBLIC)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilded.build());

    }

    private void sendUpdate(int priority, String title) {
        PreferenceManager mPrefs = new PreferenceManager(this);
        int id = mPrefs.getNotificationId();
        int pendingIntentId = mPrefs.getPendingIntentId();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, pendingIntentId, i, PendingIntent.FLAG_ONE_SHOT);

        // Gets a PendingIntent containing the entire back stack

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String visiblity;
        switch (priority) {
            case 0:
                visiblity = "Class Updates";
                break;
            case 1:
                visiblity = "Department Updates";
                break;
            case 2:
                visiblity = "Faculty Updates";
                break;
            case 3:
                visiblity = "University Updates";
                break;
            default:
                visiblity = "Updates";
        }
        NotificationCompat.Builder notificationBuilded = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_name_close)
                .setContentTitle(visiblity)
                .setContentText(title)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.icon))
                .setSound(sound)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setPriority(PRIORITY_HIGH)
                .setVisibility(VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilded.build());


    }

    private void sendNotification(String body, String title, Feed f, String key, String imageURL) {

        PreferenceManager mPrefs = new PreferenceManager(this);
        int id = mPrefs.getNotificationId();
        int pendingIntentId = new PreferenceManager(this).getPendingIntentId();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser() != null && new PreferenceManager(this).isFlowCompleted()) {
            i = new Intent(getApplicationContext(), DetailsFeedActivity.class);
            i.putExtra(INTENT_EXTRA_FEED, f);
            i.putExtra(Intent.EXTRA_TEXT, key);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsFeedActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(imageURL)) {
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
                    .setSmallIcon(R.drawable.ic_action_name_close)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.drawable.icon))
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
        } else {

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


            NotificationCompat.Builder notificationBuilded = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_action_name_close)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.drawable.icon))
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setPriority(PRIORITY_HIGH)
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setContentIntent(resultPendingIntent)
                    .addAction(new NotificationCompat.Action(R.drawable.notification_red, "BOOKMARK", bookmarkPendingIntent))
                    .addAction(new NotificationCompat.Action(R.drawable.notification_red, "SHARE", sharePendingIntent));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notificationBuilded.build());



        }
    }
}
