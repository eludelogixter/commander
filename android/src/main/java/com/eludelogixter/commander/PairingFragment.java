package com.birsan.commander;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ListIterator;

import static android.R.color.*;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PairingFragment extends Fragment {
    public interface BluetoothConnect {
        public void connDevice(BluetoothDevice myDevice);
        public void searchForDevices();
        public void turnOnProgress(Boolean state);
        public void cancelSearch();
        public void registerBReceiver();

    }
    public static final String TAG = "Pairing_Fragment.TAG";
    public static final String ARG_MESSAGE = "Pairing_Fragment.ARG_MESSAGE";
    public static final String ARG_DATA_ARRAY = "Pairing_Fragment.ARG_DATA_ARRAY";
    private BluetoothConnect myInterface;
    private ArrayList<Device> devices = new ArrayList<Device>();
    private CustomAdapter adapter;
    private ImageView tap_icon;
    protected static Button searchBtn;

    // Factory method Pairing_Fragment
    // No constructor overload allowed
    public static PairingFragment newInstance(String message, ArrayList<Device> tempArray) {

        // Create a new instance of this very object
        PairingFragment frag = new PairingFragment();

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putParcelableArrayList(ARG_DATA_ARRAY, tempArray);
        frag.setArguments(args);

        return frag;

    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _saveInstanceState) {

        // Create and return a view for this fragment
        View view = _inflater.inflate(R.layout.pairing_fragment, _container, false);
        // Target the robot_listview
        final ListView robot_listview = (ListView)view.findViewById(R.id.robot_list);

        // Instantiate an interface object
        myInterface = (BluetoothConnect)getActivity();
        myInterface.registerBReceiver();

        // Set up event-listener for the listview
        robot_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //setDisplayText("Connecting");
                // Target the tapped row and change its background
                //v.setDrawingCacheBackgroundColor(holo_blue_light);

                //ProgressBar bar = (ProgressBar)v.findViewById(R.id.some_progress);
                Toast.makeText(getActivity(), "Connected", Toast.LENGTH_LONG).show();


                // Turn on the progress bar
                //myInterface.turnOnProgress(true);

                // When the user clicks on an entry from the list launch connDevice

                myInterface.connDevice(devices.get(position).getMyDevice());

                //Log.i("PairingDevice", devices.get(position).getName());
                //Create an instance of the fragments

            }
        });

        // set up the listview with the new information
        adapter = new CustomAdapter(getActivity().getBaseContext(),
                R.layout.row, devices, getActivity());

        robot_listview.setAdapter(adapter);

        // Check to see if the robot_list has any entries in it, if it does hide tap_icon
        // if not make tap_icon visible
        tap_icon = (ImageView)view.findViewById(R.id.tap_icon);
        robot_listview.setEmptyView(tap_icon);

        // Call addDevice manually to see if the listivew in the fragment works properly
        //addDevice("test_name", "test_mac");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_MESSAGE) && args.containsKey(ARG_DATA_ARRAY)){
            setDisplayText(args.getString(ARG_MESSAGE));
            ArrayList<Device> tempArray = args.getParcelableArrayList(ARG_DATA_ARRAY);
            if(tempArray != null){
                for(Device d : tempArray){
                    addDevice(d.getMyDevice());
                }
            }
        }



        // Target connect button
        searchBtn = (Button)getView().findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Every time the search button is pressed, reset the devices to display new results
                //devices = new ArrayList<Devices>();

                // Turn on visibility of the undetermined progress bar to true
                myInterface.turnOnProgress(true);

                // Refresh adapter
                adapter.clear();

                // Connect btn logic
                myInterface = (BluetoothConnect) getActivity();


                // Check for phone's bluetooth module status
                myInterface.searchForDevices();

            }
        });
    }

    public void setDisplayText(String message) {

        getArguments().putString(ARG_MESSAGE, message);
        TextView text = (TextView)getView().findViewById(R.id.statusText);

        text.setText(message);

    }

    public void addDevice(BluetoothDevice myDevice) {

        boolean found = false;
        Device temp;

        ListIterator<Device> i = devices.listIterator();

        while(i.hasNext() && !found){

            temp = i.next();

            if(temp.getMac().equals(myDevice.getAddress()))
                found = true;

        }

        if(!found){
            devices.add(new Device(myDevice.getName(), myDevice.getAddress(), myDevice));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        myInterface.cancelSearch();
    }
}
