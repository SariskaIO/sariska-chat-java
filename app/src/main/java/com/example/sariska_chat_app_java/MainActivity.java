package com.example.sariska_chat_app_java;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.IOException;
import org.phoenixframework.channels.Channel;
import org.phoenixframework.channels.Envelope;
import org.phoenixframework.channels.IMessageCallback;
import org.phoenixframework.channels.Socket;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.example.sariska_chat_app_java.util.Utils.fetchToken;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText editText;
    private ImageButton btnSend;
    private Socket socket;
    private Channel channel;
    private MessageArrayAdapter listAdapter;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This is where we write the message
        Toolbar myToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(myToolbar);
        editText = (EditText) findViewById(R.id.editText);
        final String[] token = {null};
        final ListView messagesListView = (ListView) findViewById(R.id.messages_view);
        messagesListView.setDivider(null);
        messagesListView.setDividerHeight(0);
        listAdapter = new MessageArrayAdapter(this, android.R.layout.simple_list_item_1);
        messagesListView.setAdapter(listAdapter);
        btnSend = (ImageButton) findViewById(R.id.button_send);
        btnSend.setEnabled(true);
        final String topic = "chat:Chat10Feb";
        Thread thread = new Thread(new Runnable() {  //always create a new thread to use network
            @Override
            public void run() {
                try {
                    token[0] = fetchToken();
                    System.out.println(token[0]);
                    System.out.println("Token Generated");
                    final String socketUrl = "wss://api.sariska.io/api/v1/messaging/websocket/websocket";
                    Uri.Builder url = Uri.parse(socketUrl).buildUpon();
                    url.appendQueryParameter("token", token[0]);
                    try {
                        socket = new Socket(url.build().toString());
                        socket.connect();
                        System.out.println("Socket Connected");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    channel = socket.chan("chat:Chat10Feb", null);
                    try {
                        channel.join()
                                .receive("ignore", new IMessageCallback() {
                                    @Override
                                    public void onMessage(Envelope envelope) {
                                        System.out.println("IGNORE");
                                        System.out.println("IDNORED");
                                    }
                                })
                                .receive("ok", new IMessageCallback() {
                                    @Override
                                    public void onMessage(Envelope envelope) {
                                        System.out.println("JOINED with " + envelope.toString());
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    channel.on("new_message", new IMessageCallback() {
                        @Override
                        public void onMessage(Envelope envelope) {
                            final ChatMessage message;
                            System.out.println("NEW MESSAGE: " + envelope.toString());
                            try {
                                message = objectMapper.treeToValue(envelope.getPayload(), ChatMessage.class);
                                message.getUserId();
                                Log.i(TAG, "MESSAGE: " + message);
                                addToList(message);

                            } catch (JsonProcessingException e) {
                                Log.e(TAG, "Unable to parse message", e);
                            }
                        }
                    });

                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendMessage();
                            editText.setText("");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Error", "Errorororo");
                }
            }
        });
        thread.start();
    }

    private void sendMessage() {
        final String messageBody = editText.getText().toString().trim();
        if (channel != null) {
            System.out.println(messageBody);
            final ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
            node.put("content", messageBody);
            node.put("id", "99266");
            System.out.println(node);
            try {
                channel.push("new_message", node);
                final ChatMessage message = new ChatMessage();
                message.setContent(messageBody);

                addToList(message);
            } catch (IOException e) {
                Log.e(TAG, "Failed to send", e);
            }
        }
    }

    private void addToList(final ChatMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.add(message);
            }
        });
    }

}
