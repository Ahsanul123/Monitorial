package com.sunanda.monitorial;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by filipp on 6/16/2016.
 */
public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;


    private DatabaseReference newDatabase;
    private DatabaseReference newSend;
    Marker marker = null;

    private String name;
    private String roll;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String sharedPrefId     = "MyAppPreference";
        final SharedPreferences prefs = getSharedPreferences(sharedPrefId, 0);

        name = prefs.getString("name", null);
        roll = prefs.getString("roll", null);

        Log.d("addMarker", ""+name+" "+roll+"onStart");

//        String roll = (String) intent.getStringExtra("roll");
//        String name = (String) intent.getStringExtra("name");


        backgroundLocationListener();

        return START_STICKY;
    }

    private void backgroundLocationListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");

                i.putExtra("latitude",""+location.getLatitude());
                i.putExtra("longitude", ""+location.getLongitude());


                if(roll != null)
                {
                    newDatabase = FirebaseDatabase.getInstance().getReference().child("Track").child(roll);
                    newSend =newDatabase.push();

                    Log.d("addMarker", roll+" "+name+"inside");

                    newSend.child("name").setValue(name);
                    newSend.child("latitude").setValue(Double.toString(location.getLatitude()));
                    newSend.child("longitude").setValue(Double.toString(location.getLongitude()));

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    newSend.child("time").setValue(currentDateTimeString);
                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();

                    newSend.child("timestamp").setValue(ts);
                }

                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,9000,0,listener);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
