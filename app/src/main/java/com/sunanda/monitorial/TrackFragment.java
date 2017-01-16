package com.sunanda.monitorial;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrackFragment extends Fragment implements OnMapReadyCallback {

    View mview;
    String roll;
    String searchRoll;
    Intent intent;
    private String urlTopass;

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker marker = null;
    MapView mapView;

    Polyline line;
    Context context;
    LatLng startLatLng = new LatLng(22.89794018, 89.51005697);
    LatLng endLatLng = new LatLng(22.89776228, 89.50690269);

//    22.8951135, 89.51087236
//    22.89542977, 89.51061487
//    22.89357165, 89.51168776

    ArrayList<LatLng> markerPoints;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mFirebaseReference;

    List<LocationMap> locationMaps = new ArrayList<>();

    public TrackFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        context = getActivity();

        Bundle bundle = this.getArguments();

        searchRoll = bundle.getString("Roll");

        mFirebaseReference = FirebaseDatabase.getInstance().getReference().child("Track").child(searchRoll);

        mFirebaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> map= (Map<String,Object>) dataSnapshot.getValue();
                String name = String.valueOf(map.get("name"));
                String lng = String.valueOf(map.get("longitude"));
                String lat = String.valueOf(map.get("latitude"));
                LocationMap location = new LocationMap(Double.parseDouble(lat), Double.parseDouble(lng),name, searchRoll);
                locationMaps.add(location);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_track, container, false);

        //mFirebaseReference = FirebaseDatabase.getInstance().getReference().child("Track");

        context = getActivity();

//        Bundle bundle = this.getArguments();
//        searchRoll = bundle.getString("Roll");
//        mFirebaseReference = FirebaseDatabase.getInstance().getReference().child("Track").child(searchRoll);
//        Toast.makeText(getActivity(), "Roll: " +searchRoll, Toast.LENGTH_LONG).show();
//        Query queryRef = mFirebaseReference.child("roll").equalTo(roll);




        return mview;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context=getActivity();
        mapView = (MapView) mview.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

//        LocationMap l = locationMaps.get(0);
//        LocationMap e = locationMaps.get(1);
//
//        startLatLng = new LatLng(l.getLat(), l.getLng());
//        endLatLng = new LatLng(e.getLat(), e.getLng());


        //TODO: This is done: AsyncTask don't normally run in thread but here we use runOnUiThread under this activity
        //TODO: addChildEventListener works for the whole time
        mFirebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText(getActivity(),Integer.toString(locationMaps.size()),Toast.LENGTH_SHORT).show();
                for(int i=0; i<locationMaps.size()-1; i++) {
                    LocationMap l = locationMaps.get(i);
                    LocationMap e = locationMaps.get(i + 1);

                    startLatLng = new LatLng(l.getLat(), l.getLng());
                    endLatLng = new LatLng(e.getLat(), e.getLng());
                    Log.d("addMarker", ""+startLatLng.latitude+" "+startLatLng.longitude);

                    mGoogleMap.addMarker(new MarkerOptions().position(startLatLng));
                    urlTopass = makeURL(startLatLng.latitude,
                            startLatLng.longitude, endLatLng.latitude,
                            endLatLng.longitude);

                    new connectAsyncTask(urlTopass).execute();

                    CameraUpdate up = CameraUpdateFactory.newLatLngZoom(startLatLng, 12);
                    mGoogleMap.animateCamera(up);
                }
                mGoogleMap.addMarker(new MarkerOptions().position(endLatLng));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    public String makeURL(double sourcelat, double sourcelog, double destlat,
                          double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }


    public void drawPath(String result) {
        if (line != null) {
            //mGoogleMap.clear();
        }
//        mGoogleMap.addMarker(new MarkerOptions().position(endLatLng));
//        mGoogleMap.addMarker(new MarkerOptions().position(startLatLng));
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = mGoogleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
    }

}

//String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());