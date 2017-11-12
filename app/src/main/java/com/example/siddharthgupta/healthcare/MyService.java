package com.example.siddharthgupta.healthcare;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddharth gupta on 16-04-2017.
 */

public class MyService extends Service implements SensorEventListener {

    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;
    private SensorManager sensorMgr;
//    private RelativeLayout background;
//    private TextView text;
//    private boolean isShaked = false;

   /* NotificationCompat.Builder mBuilder;
    NotificationCompat.InboxStyle inboxStyle;
    int sendto=0;
*/
    List<String> items,ivalues;
    String message = "EMERGENCY MEDICAL CHECK UP NEEDED";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("service ","started");
        Toast.makeText(getApplicationContext(),"service started",Toast.LENGTH_LONG);
        if(sensorMgr == null){
            sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
            boolean accelSupported = sensorMgr.registerListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

            if (!accelSupported) {
                // on accelerometer on this device
                sensorMgr.unregisterListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            }
        }

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z)
                        / diffTime * 2500;
                if (speed > SHAKE_THRESHOLD) {
                    // yes, this is a shake action! Do something about it!
                    /*isShaked = true;
                    background.setBackgroundColor(Color.RED);
                    text.setText("SHAKED!!! click for try again");
                    text.setTextColor(Color.YELLOW);
                    */
                    sendSMS();
//                    addNotification();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }


    public void sendSMS()
    {
  //       sendto=0;
        items = new ArrayList<>();
        ivalues = new ArrayList<>();

        SharedPreferences sharedPreferences=MyService.this.getSharedPreferences("ContactDetails",MODE_PRIVATE);
        int no_contact_added=sharedPreferences.getInt("NoContacts",0);
        Log.e("NOContacts : ",""+no_contact_added);
        for(int i=0;i<no_contact_added;i++){
            String namei="name"+i;
            String numberi="number"+i;
            ivalues.add(sharedPreferences.getString(numberi,""));
            items.add(sharedPreferences.getString(namei,""));
            Log.e(items.get(0),":"+ivalues.get(i));
           // Log.e(namei,name);
            //Log.e(numberi,phoneNo);
            //((TestAdapter)mRecyclerView.getAdapter()).addItems(1);
        }

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

    //    inboxStyle= new NotificationCompat.InboxStyle();
      //  inboxStyle.setBigContentTitle("Emergency message send to:");

        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
//                        inboxStyle.addLine(items.get(sendto)+":"+ivalues.get(sendto));
//                        sendto++;
//                        mBuilder.setStyle(inboxStyle);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
//                        sendto++;
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
//                        sendto++;
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
//                        sendto++;
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
//                        sendto++;
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();

        for(int ij=0;ij<items.size();ij++) {
            sms.sendTextMessage(ivalues.get(ij), null, message, sentPI, deliveredPI);
        }
        if (no_contact_added>0){
            addNotification();
        }

        //if (sendto==items.size())addNotification();

        //finish();
    }

    private void addNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.healthcarelogo)
                        .setContentTitle("Health Care")
                        .setContentText("Emergency messages send");

        Intent notificationIntent = new Intent(this, MyService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        builder.setNumber(items.size());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Emergengy messages send to:");

        for (int i=0; i < items.size(); i++) {
            String substr=items.get(i);
            if (items.get(i).length()>15){
                substr=items.get(i).substring(0,12)+"...";
            }
            inboxStyle.addLine(substr+":"+ivalues.get(i));
        }
        builder.setStyle(inboxStyle);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(getBaseContext().NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
