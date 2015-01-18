package com.example.emrehantuzun.sendit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by adarshbhatt on 1/17/15.
 */
public class ConfirmationActivity extends Activity {

    String intent_currency;
    String intent_fee;
    String intent_duration;
    Double intent_targetLat;
    Double intent_targetLng;
    Double intent_currentLat;
    Double intent_currentLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        intent_currency = intent.getStringExtra("currency");
        intent_fee = intent.getStringExtra("fee");
        intent_duration = intent.getStringExtra("duration");
        intent_targetLat = intent.getDoubleExtra("targetLat", 0.0);
        intent_targetLng = intent.getDoubleExtra("targetLng", 0.0);
        intent_currentLat = intent.getDoubleExtra("currentLat", 0.0);
        intent_currentLng = intent.getDoubleExtra("currentLng", 0.0);

        setContentView(R.layout.activity_confirmation);

        TextView fee_value, duration_value;
        fee_value = (TextView) findViewById(R.id.fee_value);
        fee_value.setText(intent_currency + intent_fee);
        duration_value = (TextView) findViewById(R.id.duration_value);
        duration_value.setText(intent_duration + " minutes");

        createButton();
    }

    protected void createButton() {
        Button btn = (Button) findViewById(R.id.butConfirm);
        btn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                EditText editText;
                editText = (EditText) findViewById(R.id.description);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Required Field", Toast.LENGTH_LONG).show();
                    editText.setError( "Required Field" );
                    return;
                }

                editText = (EditText) findViewById(R.id.my_name);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Required Field", Toast.LENGTH_LONG).show();
                    editText.setError( "Required Field" );
                    return;
                }

                editText = (EditText) findViewById(R.id.my_number);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Required Field", Toast.LENGTH_LONG).show();
                    editText.setError( "Required Field" );
                    return;
                }

                editText = (EditText) findViewById(R.id.name);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Required Field", Toast.LENGTH_LONG).show();
                    editText.setError( "Required Field" );
                    return;
                }

                editText = (EditText) findViewById(R.id.number);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Required Field", Toast.LENGTH_LONG).show();
                    editText.setError( "Required Field" );
                    return;
                }

                editText = (EditText) findViewById(R.id.number);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Phone numbers must be in XXX-XXX-XXXX format.", Toast.LENGTH_LONG).show();
                    editText.setError( "Phone numbers must be in XXX-XXX-XXXX format." );
                    return;
                }

                editText = (EditText) findViewById(R.id.my_number);
                if( editText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Phone numbers must be in XXX-XXX-XXXX format.", Toast.LENGTH_LONG).show();
                    editText.setError( "Phone numbers must be in XXX-XXX-XXXX format." );
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
                intent.putExtra("manifest", ((EditText) findViewById(R.id.description)).getText().toString());
                intent.putExtra("pickup_name", ((EditText) findViewById(R.id.my_name)).getText().toString());
                intent.putExtra("pickup_phone_number", ((EditText) findViewById(R.id.my_number)).getText().toString());
                intent.putExtra("dropoff_name", ((EditText) findViewById(R.id.name)).getText().toString());
                intent.putExtra("dropoff_phone_number", ((EditText) findViewById(R.id.number)).getText().toString());
                intent.putExtra("dropoff_notes", ((EditText) findViewById(R.id.note)).getText().toString());
                String pickupAddress = Double.toString(intent_currentLat) + " , " + Double.toString(intent_currentLng);
                intent.putExtra("pickup_address", pickupAddress);
                String dropoffAddress = Double.toString(intent_targetLat) + " , " + Double.toString(intent_targetLng);
                intent.putExtra("dropoff_address", dropoffAddress);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



