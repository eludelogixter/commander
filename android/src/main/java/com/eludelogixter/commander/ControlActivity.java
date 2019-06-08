package com.birsan.commander;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class ControlActivity extends Activity implements ControlFragment.CatchIntent{
    private PairingActivity pairingCaller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

//        //Set up the actionbar navigation
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//        // Set up the entries in the actionbar navigation
//        ArrayList<String> actionList = new ArrayList<String>();
//        actionList.add("Pairing");
//        actionList.add("Control");
//        actionList.add("About");
//        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,actionList);
//        actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        actionBar.setListNavigationCallbacks(actionAdapter, this);

        // Target the action-bar and hide it
        ActionBar localBar = getActionBar();
        localBar.hide();

        // Add the ControlFragment on to this activity
        ControlFragment frag = ControlFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, frag, ControlFragment.TAG).commit();

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.control, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.sound_on:
//                Toast.makeText(this, "Sound On", Toast.LENGTH_SHORT)
//                        .show();
//                ControlFragment.soundFlag = true;
//
//                // Save current soundFlag's value to shared-preferences
//                pairingCaller = new PairingActivity();
//                pairingCaller.writePreferences(ControlFragment.soundFlag);
//                break;
//            // action with ID action_settings was selected
//            case R.id.sound_off:
//                Toast.makeText(this, "Sound Off", Toast.LENGTH_SHORT)
//                        .show();
//                ControlFragment.soundFlag = false;
//
//                // Save current soundFlag's value to shared-preferences
//                pairingCaller = new PairingActivity();
//                pairingCaller.writePreferences(ControlFragment.soundFlag);
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    @Override
    public ConnectThread catchMyThread() {

//        Intent newIntent = getIntent();
//        Bundle bundle = newIntent.getExtras();
//        return (ConnectThread)bundle.get("ConnectThread");
        return null;

    }

//    @Override
//    public boolean onNavigationItemSelected(int position, long id) {
//        // Logic for the navigation
//        // Create an instance of the fragments
//        ControlFragment ctrlFrag = ControlFragment.newInstance();
//
//        AboutFragment aboutFrag = AboutFragment.newInstance();
//
//        // Check which option was selected from the actionbar spinner
//        if (position == 1) {
//
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container, ctrlFrag).commitAllowingStateLoss();
//
//        }
//        if (position == 0) {
//
//            //getFragmentManager().beginTransaction().replace(R.id.fragment_container, pairFrag).commitAllowingStateLoss();
//            Intent goBack = new Intent(this, PairingActivity.class);
//            startActivity(goBack);
//
//        }
//        else if (position == 2) {
//
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container, aboutFrag).commitAllowingStateLoss();
//
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {

        PairingActivity.connectThread.cancel();
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void backHome(){
        PairingActivity.connectThread.cancel();
        finish();
    }
}
