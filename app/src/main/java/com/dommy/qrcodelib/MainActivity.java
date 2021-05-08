package com.dommy.qrcodelib;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.util.QrCodeGenerator;
import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ThingSpeakChannel tsChannel;
    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second
    String ds;
    ImageView imgQrcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.postDelayed(new Runnable() {
            public void run() {
                fetchData();
                initView();
                System.out.println("QR generating!"); // Do your work here
                handler.postDelayed(this, delay);
            }
        }, delay);




    }

    public void fetchData() {
        tsChannel = new ThingSpeakChannel(1382053);
        // Set listener for Channel feed update events
        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
                // Show Channel ID and name on the Action Bar


                // Notify last update time of the Channel feed through a Toast message
                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                Toast.makeText(MainActivity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
                //Log.e(null,""+lastUpdate);
            }
        });
        tsChannel.setFeedUpdateListener(new ThingSpeakChannel.FeedEntryUpdateListener() {
            @Override
            public void onFeedUpdated(long channelId, long entryId, Feed feed) {
                 ds = feed.getField1();
                Log.e(null, ""+ds);
                generateQrCode();
            }
        });

        tsChannel.loadChannelFeed();
        tsChannel.loadLastEntryInChannelFeed();

    }



    private void initView() {
        imgQrcode = (ImageView) findViewById(R.id.img_qrcode);
    }


    /**
     *
     */
    private void generateQrCode() {

        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(ds, imgQrcode.getWidth(), imgQrcode.getHeight());
        if (bitmap == null) {
            Toast.makeText(this, "\n" +
                    "Error generating QR code", Toast.LENGTH_SHORT).show();
            imgQrcode.setImageBitmap(null);
        } else {
            imgQrcode.setImageBitmap(bitmap);
        }
    }



}
