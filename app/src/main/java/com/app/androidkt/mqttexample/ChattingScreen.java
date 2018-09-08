package com.app.androidkt.mqttexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.androidkt.mqtt.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class ChattingScreen extends AppCompatActivity {
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient client;
    private MqttAndroidClient mqttAndroidClient;
    private EditText mMessage;
    private ScrollView mScrollView;
    private Button btnsend;
    TextView messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        messages=(TextView)findViewById(R.id.messages);
        messages.setText("Your now Chatting with "+Constants.PUBLISH_TOPIC);
        mMessage = (EditText) findViewById(R.id.message);
        mScrollView = (ScrollView) findViewById(R.id.textAreaScroller);
        pahoMqttClient = new PahoMqttClient();
        btnsend=(Button)findViewById(R.id.btnSend);
        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);



        mqttAndroidClient = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                if (s.contains(Constants.PUBLISH_TOPIC)){
                    messages.setText(messages.getText() +"\n" + mqttMessage.toString());
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                if (s.contains(Constants.PUBLISH_TOPIC2)){
                    messages.setText(messages.getText()  + mqttMessage.toString() );
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String msg = Constants.PUBLISH_TOPIC2+": "+mMessage.getText().toString().trim();
                if (!msg.isEmpty()) {
                    try {
                        pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                mMessage.setText("");
            }

        });


    }
}
