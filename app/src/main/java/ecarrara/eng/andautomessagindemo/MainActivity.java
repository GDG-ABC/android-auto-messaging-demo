package ecarrara.eng.andautomessagindemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String CONVERSATION_ID = "the_conversation";

        private ListView mMessagesListView;
        private EditText mNewMessageEditText;
        private Button mSendButton;

        private ArrayAdapter<String> mMessageListHistoryAdapter;
        List<String> mMessageListHistory;

        private AutoDemoNotificationManager mAutoDemoNotificationManager;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mAutoDemoNotificationManager = new AutoDemoNotificationManager(getActivity());

            mMessageListHistory = new ArrayList<String>();
            mMessageListHistoryAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_messages,
                    R.id.list_item_message_text_view,
                    mMessageListHistory);

            mMessagesListView  = (ListView) rootView.findViewById(R.id.messages_list_view);
            mMessagesListView.setAdapter(mMessageListHistoryAdapter);

            mNewMessageEditText = (EditText) rootView.findViewById(R.id.new_message_edit_text);

            mSendButton = (Button) rootView.findViewById(R.id.send_button);
            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = mNewMessageEditText.getText().toString();
                    mMessageListHistoryAdapter.add(message);
                    mAutoDemoNotificationManager.notifyUser(CONVERSATION_ID, mMessageListHistory);
                    mNewMessageEditText.setText("");
                }
            });

            return rootView;
        }
    }
}
