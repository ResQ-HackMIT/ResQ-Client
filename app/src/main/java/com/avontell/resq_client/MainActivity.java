package com.avontell.resq_client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.avontell.resq_client.domain.RescueID;
import com.avontell.resq_client.util.ResQApi;

/**
 * Main activity of the rescue app, which displays the Rescue ID
 */
public class MainActivity extends AppCompatActivity {

    private RescueID card;
    private RecyclerView rescueIdView;
    private RescueIdAdapter adapter;
    private Context context;

    private SharedPreferences sharedPref;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    boolean mRequestingLocationUpdates = false;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        sharedPref = context.getSharedPreferences(
                ResQApi.SHARED_PREFS, Context.MODE_PRIVATE);

        // Load up the existing RescueID if it exist
        card = new RescueID(this);

        // Refresh the recycler view
        loadViews(card);

        String apiKey = sharedPref.getString(ResQApi.ACCOUNT_AUTH_KEY, "NOPE");
        if (apiKey.equals("NOPE")) {
            new CreateAccountTask().execute();
        } else {
            Log.e("GOT KEY", apiKey);
        }

        // Setup location updates
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lastLocation = location;
                        }
                    }
                });
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location loc = locationResult.getLastLocation();
                if (loc != null) {
                    lastLocation = loc;
                    Log.e("LOGGED LOCATION", lastLocation.toString());
                    new UpdateLocationTask().execute();
                }
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.beacon_button:
                startBeaconFlow();
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000); // one second interval
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Attaches the views from the Rescue ID layout
     */
    public void loadViews(RescueID information) {

        rescueIdView = (RecyclerView) findViewById(R.id.id_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rescueIdView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rescueIdView.setLayoutManager(manager);

        // specify an adapter (see also next example)
        adapter = new RescueIdAdapter(information);
        rescueIdView.setAdapter(adapter);

    }

    /**
     * Begins the beacon flow
     */
    public void startBeaconFlow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if (mRequestingLocationUpdates) {
            builder.setTitle("End Beacon?");
            builder.setMessage("You are about to end your location beacon. Do you wish to proceed?");

            String positiveText = "Yes";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                            dialog.cancel();
                            endBeacon();
                        }
                    });

            String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            builder.setTitle("Start Beacon?");
            builder.setMessage("You are about to start a beacon, which will alert responders regarding your location and information. Do you wish to proceed?");

            String positiveText = "Yes";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                            dialog.cancel();
                            startBeacon();
                        }
                    });

            String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Starts the beacon
     */
    public void startBeacon() {
        mRequestingLocationUpdates = true;
        startLocationUpdates();
    }

    /**
     * End the beacon
     */
    public void endBeacon() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    class CreateAccountTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void ... voids) {

            ResQApi.createAccount(
                    context,
                    "Aaron Vontell",
                    new String[]{"Obesity III", "Diabetes"},
                    new String[]{"Peanut Butter", "Cats"},
                    new String[]{"Laxatives", "Penicillin"},
                    260, 20, 71, 0, 2, true, true, false);
            return null;
        }
    }

    class UpdateLocationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void ... voids) {

            ResQApi.updateLocation(context, lastLocation);
            return null;
        }

    }

}
