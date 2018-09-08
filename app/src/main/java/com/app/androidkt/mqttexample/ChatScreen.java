package com.app.androidkt.mqttexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.androidkt.mqtt.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ChatScreen extends AppCompatActivity {

    private MqttAndroidClient client;
    private String TAG = "MainActivity1";
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;

    private EditText firstuser, seconduser;
    private Button startchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        pahoMqttClient = new PahoMqttClient();

        firstuser = (EditText) findViewById(R.id.firstuser);
        startchat = (Button) findViewById(R.id.startchat);
        seconduser = (EditText) findViewById(R.id.seconduser);
        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);
        startchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((firstuser.getText().toString().length() <= 0 )){
                    /**
                     *   You can Toast a message here that the Username is Empty
                     **/

                    firstuser.setError( "Can't Chat with nameless person" );


                }else if(seconduser.getText().toString().length() <= 0)
                {
                    seconduser.setError("Can't Chat with nameless person");
                }




                else {
                    Constants.PUBLISH_TOPIC = seconduser.getText().toString().trim();
                    String topic2 = seconduser.getText().toString().trim();


                    String topic = firstuser.getText().toString().trim();
                    Constants.PUBLISH_TOPIC2 = firstuser.getText().toString().trim();
                    if (!topic.isEmpty()) {
                        try {
                            pahoMqttClient.subscribe(client, topic, 1);
                            pahoMqttClient.subscribe(client, topic2, 1);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent in1 = new Intent(ChatScreen.this, ChattingScreen.class);
                    startActivity(in1);
                }
            }


        });





    }
}
