package com.example.siddharthgupta.healthcare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button refreshbtn;
    GIFView hbgif;
    TextView refreshtv;
    int time=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getBaseContext(), MyService.class));

        hbgif= (GIFView) findViewById(R.id.hbgif);
        refreshtv=(TextView)findViewById(R.id.refreshtext);
        refreshbtn=(Button)findViewById(R.id.refresh_button);
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshbtn.setVisibility(View.INVISIBLE);
                refreshtv.setVisibility(View.INVISIBLE);
                hbgif.setVisibility(View.VISIBLE);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        check();
                    }
                };
                Handler handler =new Handler();
                handler.postDelayed(runnable,time);
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
//                check();
            }
        };
        Handler handler =new Handler();
        handler.postDelayed(runnable,time);

    }
    void check(){
        if(checkConnection()){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            Handler handler =new Handler();
            handler.postDelayed(runnable,time);
        }
        else{
            hbgif.setVisibility(View.INVISIBLE);
            refreshbtn.setVisibility(View.VISIBLE);
            refreshtv.setVisibility(View.VISIBLE);

            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            refreshbtn.startAnimation(myAnim);
        }
    }

    boolean checkConnection(){
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] mnetworks = connectivitymanager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network netInfo : mnetworks) {
                networkInfo = connectivitymanager.getNetworkInfo(netInfo);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)){
                    return true;
                }

            }
        }
        else {
            if (connectivitymanager != null) {
            NetworkInfo[] networkInfo = connectivitymanager.getAllNetworkInfo();
            for (NetworkInfo netInfo : networkInfo) {

                if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    if (netInfo.isConnected()) {
                        return true;
                    }
                }

            }
            }
        }
        return false;
    }
}
