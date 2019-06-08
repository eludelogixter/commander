package com.birsan.commander;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Created by nactus on 12/08/14.
 */
public class ConnectThread extends Thread implements Serializable{
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private ConnectedThread manageConnection;
    private final CountDownLatch myCoundLatch;
    private boolean bool = false;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public ConnectThread(BluetoothDevice device, BluetoothAdapter mBluetoothAdapter) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        this.mBluetoothAdapter = mBluetoothAdapter;
        BluetoothSocket tmp = null;
        mmDevice = device;

        myCoundLatch = new CountDownLatch(1);

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {

            // Establishes a bluetooth channel with the arduino robot
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            if(!mmSocket.isConnected()) {
                mmSocket.connect();
            }
            bool = true;
            //Log.i("ConnectThread ", "Device Connected");

        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            myCoundLatch.countDown();
            Log.i("ConnectThread ", connectException.toString());

            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.i("ConnectThread ", connectException.toString());
            }

            return;
        }

        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);

        manageConnection = new ConnectedThread(mmSocket);
        manageConnection.start();
        myCoundLatch.countDown();
    }

    public ConnectedThread getManageConnection(){

        return manageConnection;
    }

    public boolean isConnected(){

        try {
            myCoundLatch.await();
            Log.i("ConnectThread ", "Despertado");
        } catch (InterruptedException e) {
            e.printStackTrace();
            bool = false;
        }
        return bool;
    }


    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
