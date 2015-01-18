package com.example.emrehantuzun.sendit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


public class TrackActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Intent intent = getIntent();
        String manifest = intent.getStringExtra("manifest");
        String pickup_name = intent.getStringExtra("pickup_name");
        String pickup_address = intent.getStringExtra("pickup_address");
        String pickup_phone_number = intent.getStringExtra("pickup_phone_number");
        String pickup_business_name = intent.getStringExtra("pickup_business_name");
        String dropoff_name = intent.getStringExtra("dropoff_name");
        String dropoff_address = intent.getStringExtra("dropoff_address");
        String dropoff_phone_number = intent.getStringExtra("dropoff_phone_number");
        String dropoff_business_name = intent.getStringExtra("dropoff_business_name");
        String dropoff_notes = intent.getStringExtra("dropoff_notes");
        Log.v("pa", pickup_address);
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
