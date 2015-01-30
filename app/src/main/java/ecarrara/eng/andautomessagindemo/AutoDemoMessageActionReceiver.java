package ecarrara.eng.andautomessagindemo;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by ecarrara on 30/01/2015.
 */
public class AutoDemoMessageActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final String currentAction = intent.getAction();

        if(currentAction.equals("ecarrara.eng.andautomessagindemo.AutoDemoNotificationManager.ACTION_MESSAGE_HEARD")) {
            int conversationId = intent.getIntExtra("conversation_id", -1);
            Toast.makeText(context, "The conversation id " + conversationId + " was heard.",
                    Toast.LENGTH_LONG).show();
        } else if (currentAction.equals("ecarrara.eng.andautomessagindemo.AutoDemoNotificationManager.ACTION_MESSAGE_REPLY")) {
            int conversationId = intent.getIntExtra("conversation_id", -1);
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if(remoteInput != null) {
                String message = remoteInput.getCharSequence(AutoDemoNotificationManager.EXTRA_VOICE_REPLY).toString();
                Toast.makeText(context, "Reply: " + message,
                        Toast.LENGTH_LONG).show();
            }
        }

    }
}
