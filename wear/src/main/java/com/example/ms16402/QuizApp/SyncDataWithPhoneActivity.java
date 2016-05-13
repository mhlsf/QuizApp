package com.example.ms16402.QuizApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.ActionPage;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ms16402.gridproject.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.io.File;

/**
 * Created by ms16402 on 27/04/2016.
 */
public class SyncDataWithPhoneActivity extends Activity {

    GoogleApiClient googleApiClient;
    final String NAME_FILE = "QuizApp.db";

    Node mNode;

    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_shared);

        final ActionPage actionPage = (ActionPage) findViewById(R.id.actionpage);

        actionPage.setText("Click to send File");

         googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        googleApiClient.connect();


        actionPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running == false){
                    sendFileName();
                    running = true;
                }

            }
        });

        Wearable.MessageApi.addListener(googleApiClient, messageListener);
    }

    private MessageApi.MessageListener messageListener = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(MessageEvent messageEvent) {

            String message = new String(messageEvent.getData());
            if (message.equals("sendFile"))
            {
                sendFile();
            }
            else if (message.equals("sendFileSuccess"))
            {
                Intent intent = new Intent(getBaseContext(), ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                        ConfirmationActivity.SUCCESS_ANIMATION);
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "File Sended");
                startActivity(intent);
                running = false;
            }
        }
    };

    private void sendFile(){

        if (mNode != null)
        {
            PendingResult<ChannelApi.OpenChannelResult> messageFile = Wearable.ChannelApi.openChannel(googleApiClient, mNode.getId(), "/mypath");
            messageFile.setResultCallback(new ResultCallback<ChannelApi.OpenChannelResult>() {
                @Override
                public void onResult(ChannelApi.OpenChannelResult result) {
                    Channel channel = result.getChannel();
                    Log.d("external sotrage", Environment.getExternalStorageDirectory().toString());

                    File file = new File(Environment.getExternalStorageDirectory(), NAME_FILE);

                    channel.sendFile(googleApiClient, Uri.fromFile(file));

                }
            });
        }
    }

    private void sendFileName() {
        // Send the RPC
        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(final NodeApi.GetConnectedNodesResult result) {
                for (int i = 0; i < result.getNodes().size(); i++) {
                    if (result.getNodes().get(i).isNearby())
                    {
                        mNode = result.getNodes().get(i);
                        Log.d("node", result.getNodes().toString());

                        String fileName = NAME_FILE;
                        Wearable.MessageApi.sendMessage(googleApiClient, mNode.getId(),
                                "/mypath", fileName.getBytes());
                    }
                }
            }
        });
    }

}