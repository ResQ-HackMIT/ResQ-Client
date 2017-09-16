package com.avontell.resq_client;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.avontell.resq_client.domain.RescueID;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Main activity of the rescue app, which displays the Rescue ID
 */
public class MainActivity extends AppCompatActivity {

    private RescueID card;
    private RecyclerView rescueIdView;
    private RescueIdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load up the existing RescueID if it exist
        card = new RescueID(this);

        // Refresh the recycler view
        loadViews(card);

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

    /**
     * Starts the beacon
     */
    public void startBeacon() {

    }

}
