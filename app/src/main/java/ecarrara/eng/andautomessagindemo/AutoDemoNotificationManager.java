package ecarrara.eng.andautomessagindemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ecarrara on 30/01/2015.
 */
public class AutoDemoNotificationManager {

    public static final String ACTION_MESSAGE_HEARD =
            "ecarrara.eng.andautomessagindemo.AutoDemoNotificationManager.ACTION_MESSAGE_HEARD";
    public static final String ACTION_MESSAGE_REPLY =
            "ecarrara.eng.andautomessagindemo.AutoDemoNotificationManager.ACTION_MESSAGE_REPLY";

    public static final String NOTIFICATION_GROUP = "group_key_auto_demo";
    public static final String EXTRA_VOICE_REPLY = "voice_reply";
    public static final int CONVERSATION_ID = 1;

    private Context mContext;
    private UiModeManager mUiModeManager;
    private NotificationManagerCompat mNotificationManagerCompat;
    private NotificationCompat.Builder mNotificationBuilder;
    private ArrayList<Notification> mNotifications;
    private ArrayList<String> mSummarizedMessages;

    public AutoDemoNotificationManager(Context context) {

        mContext = context;
        mNotificationManagerCompat = NotificationManagerCompat.from(mContext);
        mNotificationBuilder = new NotificationCompat.Builder(mContext);
        mNotifications = new ArrayList<Notification>();
        mSummarizedMessages = new ArrayList<String>();
        mUiModeManager = (UiModeManager) mContext.getSystemService(Context.UI_MODE_SERVICE);

    }

    public void notifyUser(int conversationId, String conversationSubject,
                           String contactName, String message) {

        Notification notification = buildNotification(
                conversationId,
                conversationSubject,
                contactName,
                message);

        mNotificationManagerCompat.notify(0, notification);
    }

    private boolean isInCarMode() {
        return mUiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR ? true : false;
    }

    private Notification buildNotification(int conversationId, String conversationSubject,
                                           String contactName, String message) {

        prepareBasicNotification(conversationSubject, contactName, message);

        //if(isInCarMode()) {
            // Build a RemoteInput for receiving voice input in a Car Notification
            RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                    .setLabel(conversationSubject)
                    .build();

            // Conversation Read Intent for read action callback
            Intent msgHeardIntent = new Intent()
                    .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                    .setAction("ecarrara.eng.andautomessagindemo.AutoDemoNotificationManager.ACTION_MESSAGE_HEARD")
                    .putExtra("conversation_id", conversationId);

            PendingIntent msgHeardPendingIntent = PendingIntent.getBroadcast(
                    mContext,
                    conversationId,
                    msgHeardIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Conversation Reply Intent for reply callback
            Intent msgReplyIntent = new Intent()
                    .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                    .setAction("ecarrara.eng.andautomessagindemo.AutoDemoNotificationManager.ACTION_MESSAGE_REPLY")
                    .putExtra("conversation_id", conversationId);

            PendingIntent msgReplyPendingIntent = PendingIntent.getBroadcast(
                    mContext,
                    conversationId,
                    msgReplyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Create an unread conversation object to organize a group of messages
            // from a particular sender.
            NotificationCompat.CarExtender.UnreadConversation.Builder unreadConversationBuilder =
                    new NotificationCompat.CarExtender.UnreadConversation.Builder(conversationSubject)
                            .setReadPendingIntent(msgHeardPendingIntent)
                            .setReplyAction(msgReplyPendingIntent, remoteInput);

            final long timestamp = (new Date()).getTime();

            unreadConversationBuilder.addMessage(message);
            unreadConversationBuilder.setLatestTimestamp(timestamp);

            mNotificationBuilder
                    .setWhen(timestamp)
                    .setContentIntent(msgHeardPendingIntent)
                    .extend(new NotificationCompat.CarExtender()
                            .setUnreadConversation(unreadConversationBuilder.build()));
//        } else {
//            // explicit intent that will be called when the user touches the notification.
//            Intent resultIntent = new Intent(mContext, MainActivity.class);
//
//            // artificial back stack to ensure that the user go back to the correct location
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
//            stackBuilder.addParentStack(MainActivity.class);
//            stackBuilder.addNextIntent(resultIntent);
//            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                    0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            mNotificationBuilder.setContentIntent(resultPendingIntent);
//        }

        return mNotificationBuilder.build();
    }

    private void prepareBasicNotification(String conversationSubject,
                                                  String contactName, String message) {
        mNotificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(conversationSubject)
                .setContentText(message)
                .setGroup(NOTIFICATION_GROUP);
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
