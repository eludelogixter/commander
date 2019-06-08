package com.birsan.commander;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class PairingActivity extends Activity implements
        ActionBar.OnNavigationListener, PairingFragment.BluetoothConnect{

    // Properties
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final String B_OFF = "Bluetooth Off";
    private static final String B_READY = "Search Ready";
    private static final String B_WAIT = "Searching...";
    private static final String B_DONE = "Search Complete";
    private static final String B_CONNECTING = "Connecting to device";
    private BluetoothAdapter mBluetoothAdapter;
    //private ArrayList<String> mArrayAdapter;
    private BroadcastReceiver mReceiver;
    private ArrayList<Device> devices = new ArrayList<Device>();
    private SharedPreferences sharedPref;
    private int statePosition = 1;

    public static ConnectThread connectThread;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call for the ProgressBar before the setting the activity's UI xml file
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pairing);

        // Bluetooth activation check-up
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //mArrayAdapter = new ArrayList<String>();

        // Target the shared preferences space to save toggle buttons values in there
        sharedPref = getApplicationContext().getSharedPreferences("sound_on", 0);

        // Set up the actionbar navigation
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the entries in the actionbar navigation
        ArrayList<String> actionList = new ArrayList<String>();
        actionList.add("Pairing");
        actionList.add("About");
        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,actionList);
        actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(actionAdapter, this);

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                devices.add(new Device(device.getName(), device.getAddress(), device));
            }
        }

        PairingFragment frag;
        if(!mBluetoothAdapter.isEnabled()){
            frag = PairingFragment.newInstance(B_OFF, devices);
        }else{
            frag = PairingFragment.newInstance(B_READY, devices);
        }

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, frag, PairingFragment.TAG).commit();

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    PairingFragment frag = (PairingFragment) getFragmentManager().findFragmentByTag(PairingFragment.TAG);
                    frag.addDevice(device);
                }

                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                    // Set status-text to inform the user the search is in progress
                    changeStatusText(B_WAIT);

                    // Don't allow navigation to the about fragment while searching -to avoid crash
                    //ActionBar actionBar = getActionBar();
                    //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    //actionBar.setDisplayShowTitleEnabled(false);

                    // Disable the search button from the pairing_fragment and change its label
                    PairingFragment.searchBtn.setEnabled(false);

                }

                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                    // Set status-text to inform the user the search progress is done
                    changeStatusText(B_DONE);

                    // Do allow navigation to the about fragment once search has finished
                    // Set up the actionbar navigation
                    //ActionBar actionBar = getActionBar();
                    //actionBar.setDisplayShowTitleEnabled(false);
                    //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

                    // Enable the search button from pairing_fragment and change its label
                    PairingFragment.searchBtn.setEnabled(true);
                    //PairingFragment.searchBtn.setText("Search");

                    // Stop progress-bar
                    turnOnProgress(false);

                }

            }
        };
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.sound_on:

                ControlFragment.soundFlag = true;

                Toast.makeText(this, "Sound On", Toast.LENGTH_SHORT)
                        .show();

                // Write soundFlag's value on shared-preferences
                writePreferences(ControlFragment.soundFlag);
                break;

            case R.id.sound_off:


                ControlFragment.soundFlag = false;

                Toast.makeText(this, "Sound Off", Toast.LENGTH_SHORT)
                        .show();

                // Write soundFlag's value on shared-preferences
                writePreferences(ControlFragment.soundFlag);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {

        // Logic for the navigation
        // Create an instance of the fragments
        ControlFragment ctrlFrag = ControlFragment.newInstance();

        PairingFragment frag = (PairingFragment)getFragmentManager().findFragmentByTag(PairingFragment.TAG);
        if(frag == null) {
            if (!mBluetoothAdapter.isEnabled()) {
                frag = PairingFragment.newInstance(B_OFF, devices);
            } else {
                frag = PairingFragment.newInstance(B_READY, devices);
            }
        }

        AboutFragment aboutFrag = AboutFragment.newInstance();

        if (position == 0 && statePosition == 0) {

            statePosition = 1;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).addToBackStack("pairingFrag").commit();


        } else if (position == 1 && statePosition == 1) {

            statePosition = 0;
            devices = frag.getDevices();

            getFragmentManager().beginTransaction().replace(R.id.fragment_container, aboutFrag).addToBackStack("aboutFrag").commit();


        }
        return true;

    }


    // Catch the bluetooth intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.i("codigos: ", String.valueOf(requestCode)+" "+String.valueOf(resultCode)+" "+String.valueOf(RESULT_OK));

        if(requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {

                //check = true;
                changeStatusText(B_WAIT);
                Log.i("Activation Bluetooth", "Bluetooth activated");

                PairingFragment frag = (PairingFragment)getFragmentManager().findFragmentByTag(PairingFragment.TAG);
                if (frag == null) {
                    frag = PairingFragment.newInstance(B_READY,devices);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, frag, PairingFragment.TAG).commit();
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        frag.addDevice(device);
                    }
                }
                mBluetoothAdapter.startDiscovery();

            } else {

                changeStatusText(B_OFF);

                // If the user clicks choose cancel (in the bluetooth activation dialogue)
                // turn off the top progress bar
                turnOnProgress(false);
                Log.i("Activation Bluetooth", "Bluetooth not activated");
            }
        }
    }

    @Override
    public void connDevice(BluetoothDevice myDevice) {

        // Turn on progress bar
        //turnOnProgress(true);
        //ProgressBar bar = (ProgressBar)view.findViewById(R.id.some_progress);
        //bar.setVisibility(View.VISIBLE);

        //changeStatusText(B_CONNECTING);

        //Log.i("PairingActivity", "connDevice");
        //Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
        connectThread = new ConnectThread(myDevice, mBluetoothAdapter);
        connectThread.start();

        if(connectThread.isConnected()){

            // Turn off progress bar
            //turnOnProgress(false);

            //changeStatusText(B_READY);
            Intent changeFragments = new Intent(this, ControlActivity.class);
            startActivityForResult(changeFragments,7);



        } else{
            Toast.makeText(getApplicationContext(), "Cannot connect to device", Toast.LENGTH_LONG).show();
            //changeStatusText(B_READY);
            // Turn off progress bar
            //turnOnProgress(false);

        }
    }

    @Override
    public void searchForDevices() {

        if (!mBluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            changeStatusText(B_OFF);

        }else{

            mBluetoothAdapter.startDiscovery();
            changeStatusText(B_READY);

        }
    }

    @Override
    public void onResume(){

        super.onResume();

        registerBReceiver();
    }

    @Override
    public void onPause() {

        super.onPause();

        //cancelSearch();
    }

    private void changeStatusText(String message){
        PairingFragment frag = (PairingFragment)getFragmentManager().findFragmentByTag(PairingFragment.TAG);

        if (frag == null) {
            frag = PairingFragment.newInstance(message,devices);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, frag, PairingFragment.TAG).commit();
        } else {
            frag.setDisplayText(message);
        }
    }

    public void turnOnProgress(Boolean state) {

        // Progress bar on
        if(state == true) {

            setProgressBarIndeterminateVisibility(true);
        }

        // Progress bar off
        else {

            setProgressBarIndeterminateVisibility(false);
        }

    }

    // Add the soundFag values to the shared-preferences sector
    private boolean writePreferences(Boolean bool) {

        SharedPreferences.Editor editor = sharedPref.edit();
        try {

            // Write on shared pref.
            editor.putBoolean("sound_on", bool);

            editor.commit();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("writePreferences error", "can't write on sharedPref file");

            return false;
        }

    }

    @Override
    public void onBackPressed(){

        FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();

        if(count > 0 && !fm.getBackStackEntryAt(count-1).getName().equals("pairingFrag")){
            ActionBar actionBar = getActionBar();
            actionBar.setSelectedNavigationItem(0);
            //moveTaskToBack(true);
            count --;
        }

        for(int i = 0; i < count ; ++i) {
            Log.i("Stack entry: ",fm.getBackStackEntryAt(i).getName());
            fm.popBackStack();

        }

        super.onBackPressed();

    }

    public void cancelSearch(){


        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
        mBluetoothAdapter.cancelDiscovery();
        turnOnProgress(false);
        PairingFragment.searchBtn.setEnabled(true);
        changeStatusText(B_READY);

    }

    public void registerBReceiver(){
        if(mReceiver != null){

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);

            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mReceiver, filter);

            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            registerReceiver(mReceiver, filter);

        }
    }
}


