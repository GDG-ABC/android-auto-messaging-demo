package ecarrara.eng.andautomessagindemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecarrara on 30/01/2015.
 */
public class AutoDemoNotificationManager {

    public static final String NOTIFICATION_GROUP = "group_key_auto_demo";

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private ArrayList<Notification> mNotifications;
    private ArrayList<String> mSummarizedMessages;

    public AutoDemoNotificationManager(Context context) {

        mContext = context;
        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(mContext);
        mNotifications = new ArrayList<Notification>();
        mSummarizedMessages = new ArrayList<String>();

    }

    public void notifyUser(String conversationId, List<String> messages) {
        if(messages.size() > 0) {
            for (String message : messages) {
                mNotifications.add(buildBasicNotification(message, conversationId));
            }

            if(mNotifications.size() > 0) {
                mNotificationManager.cancelAll();
            }

            for (Notification notification : mNotifications) {
                mNotificationManager.notify(0, notification);
            }

            if (mNotifications.size() > 1) {
                mNotificationManager.notify(0, builBasicSummaryNotification());
            }
         }

    }

    private Notification buildBasicNotification(String message, String conversation) {

        mNotificationBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(conversation)
                .setContentText(message)
                .setGroup(NOTIFICATION_GROUP);

        // store the message to use in the summary notification
        mSummarizedMessages.add(message);

        // explicit intent that will be called when the user touches the notification.
        Intent resultIntent = new Intent(mContext, MainActivity.class);

        // artificial back stack to ensure that the user go back to the correct location
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder.setContentIntent(resultPendingIntent);
        return mNotificationBuilder.build();

    }

    private Notification builBasicSummaryNotification() {
        String title = String.format(mContext.getString(R.string.format_messages_pendind),
                mNotifications.size());

        NotificationCompat.InboxStyle notificationStyle = new NotificationCompat.InboxStyle();
        notificationStyle.setBigContentTitle(title);
        for(String message : mSummarizedMessages) {
            notificationStyle.addLine(message);
        }

        mNotificationBuilder
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(notificationStyle)
                .setGroup(NOTIFICATION_GROUP)
                .setGroupSummary(true);

        // explicit intent that will be called when the user touches the notification.
        Intent resultIntent = new Intent(mContext, MainActivity.class);

        // artificial back stack to ensure that the user go back to the correct location
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder.setContentIntent(resultPendingIntent);

        return mNotificationBuilder.build();
    }
}
