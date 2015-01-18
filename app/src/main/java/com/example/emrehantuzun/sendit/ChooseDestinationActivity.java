package com.example.emrehantuzun.sendit;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseDestinationActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ImageButton btn;
    private Marker marker;
    MapView mapview;
    LocationManager locationManager;
    Criteria criteria;
    String provider;
    HttpClient client;
    HttpPost post;

    Double targetLat;
    Double targetLng;



    String fee = "";
    String currency = "";
    String duration = "";
    String quote_id = "";

    private class GetQuote extends AsyncTask<Void, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(Void... params) {

            try {
                post.setHeader("Authorization", getB64Auth("c1338e55-3d4b-47a4-8b4c-43f2c519e94a", ""));
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                Location currentLocation = locationManager.getLastKnownLocation(provider);
                String currentLocationString = Double.toString(currentLocation.getLatitude()) + " "
                        + Double.toString(currentLocation.getLongitude());
                pairs.add(new BasicNameValuePair("pickup_address",  currentLocationString));
                String targetLocationString = Double.toString(targetLat) + " " + Double.toString(targetLng);
                pairs.add(new BasicNameValuePair("dropoff_address", targetLocationString));
                post.setEntity(new UrlEncodedFormEntity(pairs));

                // Execute HTTP Post Request
                HttpResponse response = client.execute(post);
                return response;
            } catch (Exception e) {
                return null;
            }
        }

        private String getB64Auth (String login, String pass) {
            String source=login+":"+pass;
            String ret="Basic "+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
            return ret;
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            try {
                String responseString = EntityUtils.toString(response.getEntity());

                JSONObject responseObject = new JSONObject(responseString);

                fee = Double.toString(responseObject.getInt("fee") / 100.0).replaceAll("\\.?0*$", "");
                currency = responseObject.getString("currency");
                currency = currency.replace("usd", "$");
                duration = responseObject.getString("duration");
                quote_id = responseObject.getString("id");

                showQuote();
            } catch (Exception e) {
            }
        }
    }

    private void showQuote() {
        CharSequence text = currency + fee + ",  " + duration + " minutes";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        setContentView(R.layout.activity_choose_destination);
        setUpMapIfNeeded();

        client = new DefaultHttpClient();
        post = new HttpPost("https://api.postmates.com/v1/customers/cus_K81xUn9jAY2_3F/delivery_quotes");
    }


    public void nextActivity (View view) {
        Location currentLocation = locationManager.getLastKnownLocation(provider);

        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra("currency", currency);
        intent.putExtra("fee", fee);
        intent.putExtra("duration", duration);
        intent.putExtra("targetLat", targetLat);
        intent.putExtra("targetLng", targetLng);
        intent.putExtra("currentLat", currentLocation.getLatitude());
        intent.putExtra("currentLng", currentLocation.getLongitude());
        intent.putExtra("quote_id", quote_id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            //set my location
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("New Marker");
                mMap.addMarker(marker);
                targetLat = point.latitude;
                targetLng = point.longitude;

                new GetQuote().execute();

            }
        });


        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    public class SearchResultsActivity extends Activity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            handleIntent(getIntent());
        }

        @Override
        protected void onNewIntent(Intent intent) {
            handleIntent(intent);
        }

        private void handleIntent(Intent intent) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                //use the query to search your data somehow
            }
        }
    }

}
