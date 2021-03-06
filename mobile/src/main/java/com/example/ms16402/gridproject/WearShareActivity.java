package com.example.ms16402.gridproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WearShareActivity extends AppCompatActivity {

    GoogleApiClient googleApiClient;
    String fileName;
    File file;
    ImageView icone_info;
    String mNodeId;
    TextView text_info;
    TextView text_info_last_file;
    TextView text_name_last_file;

    Animation fadeiInAnimationObject;

    final static String PATH_DEFAULT_FOLDER = Environment.getExternalStorageDirectory() + "/University of Bristol/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_grid);
        Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar_top);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(R.string.app_name);


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.button_mail);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFileForMail();
            }
        });


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        googleApiClient.connect();

        fadeiInAnimationObject = new AlphaAnimation(0f
                ,1f);
        fadeiInAnimationObject.setDuration(1000);

        icone_info = (ImageView) findViewById(R.id.icone_info) ;
        text_info = (TextView) findViewById(R.id.info);

        text_info_last_file = (TextView) findViewById(R.id.info_last_file);
        text_info_last_file.setVisibility(View.INVISIBLE);
        text_name_last_file = (TextView) findViewById(R.id.name_last_file);
        text_name_last_file.setAlpha(0.0f);


        final Handler h = new Handler();
        int delay = 1000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                checkState();
                h.postDelayed(this, 1000);
            }
        }, delay);
        checkState();


    }



    public void chooseFileForMail(){


        if (fileName == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setMessage(R.string.dialog_message_nolastfile)
                    .setTitle(R.string.dialog_title_nolastfile);

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    new FileChooser(WearShareActivity.this).setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(final File file) {
                            sendMail(file.getAbsolutePath());
                        }
                    }).showDialog();
                }

            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // Set other dialog properties
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            // Add the buttons
            builder.setPositiveButton(R.string.send_last_file, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    sendMail(PATH_DEFAULT_FOLDER + fileName);
                }
            });

            builder.setNeutralButton(R.string.choose_file_on_sdcard, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    new FileChooser(WearShareActivity.this).setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(final File file) {
                            sendMail(file.getAbsolutePath());
                        }
                    }).showDialog();
                }
            });
            // Set other dialog properties
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void sendMail(String fileandpath){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"example@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Database File");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Here the database file");
        File file = new File(fileandpath);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }



    //When bluetooth is connected, check if device is connected and display the name
    public void checkIfWearableConnected() {

        retrieveDeviceNode(new Callback() {
            @Override
            public void success(final String displayName) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WatchConnectedMessage(displayName);
                    }
                });
            }

            @Override
            public void failed(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noWatchConnectedMessage();
                    }
                });

            }
        });




    }

    private void retrieveDeviceNode(final Callback callback) {
        final GoogleApiClient client = googleApiClient;

        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    String displayName = nodes.get(0).getDisplayName();
                    callback.success(displayName);

                } else {
                    callback.failed("no wearables found");
                }
            }
        }).start();
    }

    private interface Callback {
        public void success(final String nodeId);
        public void failed(final String message);
    }

    private void checkState()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            icone_info.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
            text_info.setText("Bluetooth is not enabled");
        }
        else {
           checkIfWearableConnected();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    == BluetoothAdapter.STATE_TURNING_OFF ||
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    == BluetoothAdapter.STATE_OFF){
                icone_info.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
                text_info.setText("Bluetooth is not enabled");
            }
            else{
                checkIfWearableConnected();
            }
        }
    };

    private void WatchConnectedMessage(String name)
    {
        icone_info.setImageResource(R.drawable.ic_done_black_48dp);
        text_info.setText("Connected to " + name + ", ready to share");
    }

    private void noWatchConnectedMessage()
    {
        text_info.setText("No watch are connected");
        icone_info.setImageResource(R.drawable.ic_sync_disabled_black_48dp);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Wearable.ChannelApi.addListener(googleApiClient, channelListener);
        Wearable.MessageApi.addListener(googleApiClient, messageListener);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        checkState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mReceiver);
        }catch (IllegalArgumentException e)
        {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private MessageApi.MessageListener messageListener = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
             fileName = new String(messageEvent.getData());
            if (!fileName.equals(""))
            {
                mNodeId = messageEvent.getSourceNodeId();
                String sendFile = "sendFile";
                //Requesting the app to send the file
                Wearable.MessageApi.sendMessage(googleApiClient,messageEvent.getSourceNodeId(),"/mypath", sendFile.getBytes());
            }
        }
    };

    private ChannelApi.ChannelListener channelListener = new ChannelApi.ChannelListener() {
        @Override
        public void onChannelOpened(Channel channel) {
            if (channel.getPath().equals("/mypath")) {

                //Here you can change the destination folder
                file = new File(PATH_DEFAULT_FOLDER, fileName);
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.d("dont work file not", e.getMessage());
                }
                channel.receiveFile(googleApiClient, Uri.fromFile(file), false);
            }
        }

        @Override
        public void onChannelClosed(Channel channel, int i, int i1) {
        }

        @Override
        public void onInputClosed(Channel channel, int i, int i1) {
            String sendFileSuccess = "sendFileSuccess";
            Toast.makeText(WearShareActivity.this, "File Receive", Toast.LENGTH_SHORT).show();
            text_info_last_file.setVisibility(View.VISIBLE);

            Wearable.MessageApi.sendMessage(googleApiClient, mNodeId,"/mypath", sendFileSuccess.getBytes());
            MediaScannerConnection.scanFile(WearShareActivity.this, new String[]{file.getAbsolutePath()}, null, null);
            text_name_last_file.setText(fileName);
            text_name_last_file.setAlpha(0.0f);
            text_name_last_file.animate().alpha(1).setDuration(1000);
        }

        @Override
        public void onOutputClosed(Channel channel, int i, int i1) {

        }
    };




}
