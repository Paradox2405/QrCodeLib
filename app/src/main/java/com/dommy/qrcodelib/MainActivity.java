package com.dommy.qrcodelib;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.util.QrCodeGenerator;
import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String URL = "https://api.thingspeak.com/channels/1382053/feeds.json?api_key=7Z0GO6RB74ZNT328&results=2";

    private ThingSpeakChannel tsChannel;

    Button btnGenerate; // 生成二维码
    EditText etContent; // 待生成内容
    ImageView imgQrcode; // 二维码图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


           fetchData();

    }

    public void fetchData() {
        tsChannel = new ThingSpeakChannel(1382053,"7Z0GO6RB74ZNT328");
        // Set listener for Channel feed update events
        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
                // Show Channel ID and name on the Action Bar
                long entry = channelFeed.getChannel().getLastEntryId();
               String d = channelFeed.getFeeds().toString();
                Log.e(null,""+d);
                // Notify last update time of the Channel feed through a Toast message
                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                Toast.makeText(MainActivity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
            }
        });

//        tsChannel.setFeedUpdateListener(new ThingSpeakChannel.FeedEntryUpdateListener() {
//            @Override
//            public void onFeedUpdated(long channelId, long entryId, Feed feed) {
//
//                long ent=feed.getEntryId();
//               // String dataa = feed.getField1();
//                Log.e(null,""+ent);
//            }
//        });
        // Fetch the specific Channel feed
        tsChannel.loadChannelFeed();

    }



    private void initView() {


        btnGenerate = (Button) findViewById(R.id.btn_generate);
        btnGenerate.setOnClickListener(this);


        etContent = (EditText) findViewById(R.id.et_content);
        imgQrcode = (ImageView) findViewById(R.id.img_qrcode);
    }


    /**
     * 生成二维码
     */
    private void generateQrCode() {
        if (etContent.getText().toString().equals("")) {
            Toast.makeText(this, "\n" +
                    "Please enter the content of the QR code", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(etContent.getText().toString(), imgQrcode.getWidth(), imgQrcode.getHeight());
        if (bitmap == null) {
            Toast.makeText(this, "\n" +
                    "Error generating QR code", Toast.LENGTH_SHORT).show();
            imgQrcode.setImageBitmap(null);
        } else {
            imgQrcode.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_generate) {
            generateQrCode();
        }
    }


}
