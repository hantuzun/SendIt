package com.example.emrehantuzun.sendit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class TrackActivity extends ActionBarActivity {

    HttpClient client;
    HttpPost post;
    Context CONTEXT;

    String manifest;
    String pickup_name;
    String pickup_address;
    String pickup_phone_number;
    String pickup_business_name;
    String dropoff_name;
    String dropoff_address;
    String dropoff_phone_number;
    String dropoff_business_name;
    String dropoff_notes;
    String quote_id;


    String delivery_id, status, created, pickup_eta,  dropoff_eta, dropoff_deadline;
    TextView statusView, pickup_estimateView, dropoff_etaView, dropoff_deadlineView;



    @Override
    protected void onResume() {
        super.onResume();
        new updateTexts(this).execute();
    }

    private class updateTexts extends AsyncTask<Void, Void, HttpResponse> {
        private Context context;

        public updateTexts(Context context){
            this.context=context;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            HttpResponse response = null;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                String uri = "https://api.postmates.com/v1/customers/cus_K81xUn9jAY2_3F/deliveries/" + delivery_id;
                request.setURI(new URI(uri));
                request.setHeader("Authorization", getB64Auth("c1338e55-3d4b-47a4-8b4c-43f2c519e94a", ""));
                response = client.execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

            @Override
            protected void onPostExecute(HttpResponse response) {
                try {
                    String responseString = EntityUtils.toString(response.getEntity());
                    Log.v("responseString", responseString);
                    JSONObject responseObject = new JSONObject(responseString);
                    Log.v("responseObject", responseObject.toString());
                    status = responseObject.getString("status").toUpperCase();
                    pickup_eta = responseObject.getString("pickup_eta");
                    dropoff_eta = responseObject.getString("dropoff_eta");
                    dropoff_deadline = responseObject.getString("dropoff_deadline");

                    statusView = (TextView) findViewById(R.id.status);
                    statusView.setText(status);

                    pickup_estimateView = (TextView) findViewById(R.id.pickup_estimate);
                    pickup_estimateView.setText(convertTime(pickup_eta));

                    dropoff_etaView = (TextView) findViewById(R.id.dropoff_estimate);
                    dropoff_etaView.setText(convertTime(dropoff_eta));

                    dropoff_deadlineView = (TextView) findViewById(R.id.dropoff_deadline);
                    dropoff_deadlineView.setText(convertTime(dropoff_deadline));

                    if (status.equalsIgnoreCase("DELIVERED")) {
                        goToDelivered();
                    } else {
                        new updateTexts(context).execute();
                    }
                } catch (Exception e) {

                }
            }

        private void goToDelivered() {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 5 seconds
                }
            }, 5000);
            Intent intent = new Intent(context, DeliveredActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    private String convertTime(String date) {
        Log.v("                                     ut", date);
        if (date == null || date == "null") return "";

        String utcTime = date.substring(11, 16);
        Log.v("                                     utime", utcTime);

        String utcHour = utcTime.substring(0, 2);
        String utcMin = utcTime.substring(3, 5);
        int localHour = Integer.parseInt(utcHour) - 5;

        String localTime = localHour + ":" + utcMin;
        Log.v("                                     lt", localTime);
        return localTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CONTEXT = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        client = new DefaultHttpClient();
        post = new HttpPost("https://api.postmates.com/v1/customers/cus_K81xUn9jAY2_3F/deliveries");

        Intent intent = getIntent();
        manifest = intent.getStringExtra("manifest");
         pickup_name = intent.getStringExtra("pickup_name");
         pickup_address = intent.getStringExtra("pickup_address");
         pickup_phone_number = intent.getStringExtra("pickup_phone_number");
         pickup_business_name = intent.getStringExtra("pickup_business_name");
         dropoff_name = intent.getStringExtra("dropoff_name");
         dropoff_address = intent.getStringExtra("dropoff_address");
         dropoff_phone_number = intent.getStringExtra("dropoff_phone_number");
         dropoff_business_name = intent.getStringExtra("dropoff_business_name");
         dropoff_notes = intent.getStringExtra("dropoff_notes");
         quote_id = intent.getStringExtra("quote_id");

        new makeOrder().execute();


    }

    private class makeOrder extends AsyncTask<Void, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                post.setHeader("Authorization", getB64Auth("c1338e55-3d4b-47a4-8b4c-43f2c519e94a", ""));
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();

                pairs.add(new BasicNameValuePair("manifest", manifest));
                pairs.add(new BasicNameValuePair("pickup_name", pickup_name));
                pairs.add(new BasicNameValuePair("pickup_address", pickup_address));
                pairs.add(new BasicNameValuePair("pickup_phone_number", pickup_phone_number));
                pairs.add(new BasicNameValuePair("pickup_business_name", 		pickup_business_name));
                pairs.add(new BasicNameValuePair("dropoff_name", dropoff_name));
                pairs.add(new BasicNameValuePair("dropoff_address", dropoff_address));
                pairs.add(new BasicNameValuePair("dropoff_phone_number", dropoff_phone_number));
                pairs.add(new BasicNameValuePair("dropoff_business_name", dropoff_business_name));
                pairs.add(new BasicNameValuePair("dropoff_notes", dropoff_notes));
                pairs.add(new BasicNameValuePair("quote_id", quote_id));
                post.setEntity(new UrlEncodedFormEntity(pairs));
                Log.v("pn", ">>>" + pickup_name);

                // Execute HTTP Post Request
                HttpResponse response = client.execute(post);
                Log.v("ok", "request made");
                Log.v("response", response.toString());
                return response;
            } catch (Exception e) {
                Log.e("e", e.toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            try {
                String responseString = EntityUtils.toString(response.getEntity());
                Log.v("responseString", responseString);
                JSONObject responseObject = new JSONObject(responseString);
                Log.v("responseObject", responseObject.toString());
                status = responseObject.getString("status");
                pickup_eta = responseObject.getString("pickup_eta");
                created= responseObject.getString("created");
                dropoff_eta = responseObject.getString("dropoff_eta");
                dropoff_deadline = responseObject.getString("dropoff_deadline");
                delivery_id =  responseObject.getString("id");

                Log.v("delivery_id", delivery_id);
                Log.v("status", status);
                Log.v("pickup_eta", pickup_eta);
                Log.v("created", created);
                Log.v("dropoff_eta", dropoff_eta);
                Log.v("dropoff_deadline", dropoff_deadline);

                new updateTexts(CONTEXT).execute();
            } catch (Exception e) {

            }
        }
    }

    private String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        String ret="Basic "+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_track, container, false);
            return rootView;
        }
    }
}
