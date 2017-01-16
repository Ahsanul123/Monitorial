package com.sunanda.monitorial;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sunanda.monitorial.mModel.Student_info;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback{


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker marker = null;
    Marker m = null;
    LocationRequest mLocationRequest;
    private static final int REQUEST_LOCATION = 1;
    MapView mapView;
    View mview;
    private String id,name;
    private String roll = null;
    Double llat;
    Double llng;

    private DatabaseReference mDatabase;
    private DatabaseReference newPost;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mFirebaseReference;
    ChildEventListener mChildEventListener;

    private DatabaseReference newDatabase;
    private DatabaseReference newSend;
    private Intent intent;
    private BroadcastReceiver broadcastReceiver;
    private DatabaseReference newDatabaseDelete;
    String lat , llong;


    private String   new_roll;
    public LocationFragment() { }

    List<LocationMap> locationMaps = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mview = inflater.inflate(R.layout.fragment_location, container, false);

//        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
//
//        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//            buildAlertMessageNoGps();
//        }


        mFirebaseReference = FirebaseDatabase.getInstance().getReference().child("Map");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Map");

        newPost = mDatabase.push();
        intent = getActivity().getIntent();
        id = intent.getStringExtra("KEY");
        name=intent.getStringExtra("NAME");
        roll=intent.getStringExtra("ROLL");




        Student_info student_info = Student_info.getInstance();
        new_roll = student_info.getRoll();






        if(new_roll != null)

        {
            newDatabaseDelete  = FirebaseDatabase.getInstance().getReference().child("Track").child(roll);


            long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
            com.google.firebase.database.Query oldItems = newDatabaseDelete.orderByChild("timestamp").endAt(cutoff);




            oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                        itemSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }













        if(!runtime_permissions())
            enable_buttons();

        return mview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mview.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String lat = (String) intent.getExtras().get("latitude");
                    String lng = (String) intent.getExtras().get("longitude");
                    //textView.append("\n" +intent.getExtras().get("coordinates"));
                    LatLng ll = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 12);
                    mGoogleMap.animateCamera(update);

                    if (marker != null) {
                        marker.remove();
                        marker = null;
                    }
                    MarkerOptions options = new MarkerOptions()
                            .title("I'm Here")
                            .position(new LatLng(ll.latitude, ll.longitude));
                    marker = mGoogleMap.addMarker(options);

                }
            };
        }
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
           // getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }

    private void enable_buttons() {
        Intent i =new Intent(getActivity(),GPS_Service.class);
        i.putExtra("name", name);
        i.putExtra("roll", roll);
        getActivity().startService(i);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;




    }





    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, you must enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }
}

//22.900182, 89.502292
//22.89794018, 89.51005697
//