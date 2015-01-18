package com.example.emrehantuzun.sendit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by adarshbhatt on 1/17/15.
 */
public class ConfirmationActivity extends Activity {

    Button confirm;
    TextView fee, timeVal;
    EditText name, number, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        confirm = (Button) findViewById(R.id.butConfirm);
        fee = (TextView) findViewById(R.id.fee);
        timeVal = (TextView) findViewById(R.id.time);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        note = (EditText) findViewById(R.id.note);

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



