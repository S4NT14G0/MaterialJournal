package com.developer.android.sroig.materialjournal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.developer.android.sroig.materialjournal.models.Database;
import com.developer.android.sroig.materialjournal.models.JournalItem;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

/**
 * Created by Santiago Roig on 11/3/15.
 */
public class JournalEditActivity extends AppCompatActivity {

    // Create references to our xml
    MaterialEditText editTextTitle;
    MaterialEditText editTextDate;
    MaterialEditText editTextTags;
    MaterialEditText editTextLocation;
    MaterialEditText editTextJournalEntry;

    int year, month, day;
    int journalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_journal_edit);

        // Set up our references
        editTextTitle = (MaterialEditText) findViewById(R.id.textTitle);
        editTextDate = (MaterialEditText) findViewById(R.id.textDate);
        editTextTags = (MaterialEditText) findViewById(R.id.textTags);
        editTextLocation = (MaterialEditText) findViewById(R.id.textLocation);
        editTextJournalEntry = (MaterialEditText) findViewById(R.id.textJournal);

        // Grab message from our activity
        Intent intent = getIntent();
        // Find what our current Id is
        journalId = Integer.parseInt(intent.getStringExtra("itemId"));

        // Load in the current Journal Item from DB
        loadJournalItem();
        showDatePicker();
    }

    private void loadJournalItem() {

        // Grab our current journal item from db
        JournalItem item = Database.getInstance(this).getRow(journalId);

        if (item != null) {
            // Populate the view with data
            editTextTitle.setText(item.getTitle());
            editTextDate.setText(Database.getInstance(this).dateToString(item.getDate()));
            editTextTags.setText(item.getTags());
            editTextLocation.setText(item.getLocation());
            editTextJournalEntry.setText(item.getText());
        }


    }

    public void updateJournalItem(View view) {

        // Create new item to pass to DB
        JournalItem item = new JournalItem();

        // Grab the values out of text boxes
        item.setTitle(editTextTitle.getText().toString());
        item.setDate(Database.getInstance(this).stringToDate(editTextDate.getText().toString()));
        item.setTags(editTextTags.getText().toString());
        item.setLocation(editTextLocation.getText().toString());
        item.setText(editTextJournalEntry.getText().toString());

        // Update this row of DB
        Database.getInstance(this).updateRow(journalId, item);

        // Return to main menu
        Intent intent = new Intent(JournalEditActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    public void addLocation (View view) {

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {}

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        try {
            // Acquire a reference to the system Location Manager
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            // Check if we know our last location
            if (lastKnownLocation != null) {
                String loc = "Longitude: " + (Math.round(lastKnownLocation.getLongitude() * 100.0)/ 100.0)
                        + ", Latitude: " + (Math.round(lastKnownLocation.getLatitude() * 100.0) / 100.0);
                editTextLocation.setText(loc);
            } else {
                // If it's null then the user didn't get their location
                Toast.makeText(getApplicationContext(), "Turn on Location Services for this feature to work!",
                        Toast.LENGTH_LONG).show();
            }

            // Remove the listener you previously added
            locationManager.removeUpdates(locationListener);

        } catch (SecurityException e) {
            e.printStackTrace();
        }



    }

    DatePickerDialog.OnDateSetListener dpClickListen = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int _year, int _monthOfYear, int _dayOfMonth) {
            // Click handler for our datepicker dialog
            year = _year;
            month = _monthOfYear + 1;
            day = _dayOfMonth;

            editTextDate.setText(month + "/" + day + "/" + year);
        }
    };

    public void showDatePicker () {
        // Intialize a datepicker

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(JournalEditActivity.this, dpClickListen, year, month, day);
                dpd.show();
            }
        });
    }

}

