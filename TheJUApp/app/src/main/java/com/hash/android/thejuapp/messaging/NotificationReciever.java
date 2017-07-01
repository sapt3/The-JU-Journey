package com.hash.android.thejuapp.messaging;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;
import static com.hash.android.thejuapp.messaging.MyFirebaseMessagingService.NOTIIFICATION_ID_EXTRA;


public class NotificationReciever extends BroadcastReceiver {
    public static final String ACTION_SHARE = "shareAction";
    public static final String ACTION_BOOKMARK = "bookmarkAction";
    private static final String TAG = NotificationReciever.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra(NOTIIFICATION_ID_EXTRA, 1330);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String action = intent.getAction();
        String key = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.d(TAG, "action:: " + action + "id::" + id);
        Feed feed = intent.getParcelableExtra(INTENT_EXTRA_FEED);

        if (ACTION_SHARE.equals(action)) {
            notificationManager.cancel(id);
            if (feed != null) {
                share(feed, context);
            }

        } else if (ACTION_BOOKMARK.equals(action)) {
            notificationManager.cancel(id);
            String uid = new PreferenceManager(context).getUID();
            try {
                DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("bookmarks").child(key).getRef();
                bookmarksRef.setValue(true);
                Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void share(Feed feed, Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, feed.getHeading() + "\n" + "By " + feed.getAuthor() + "-  \n"
                + "\n\n" + feed.getShortDesc() + "\n" + "To know more download the app at https//www.play.google.com?apps+dasd\n");
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
