package com.birsan.commander;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nactus on 8/20/14.
 */
public class Device implements Parcelable{

    private String name;
    private String mac;
    private BluetoothDevice myDevice;

    public Device(String name, String mac, BluetoothDevice myDevice) {
        this.name = name;
        this.mac = mac;
        this.myDevice = myDevice;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public BluetoothDevice getMyDevice() {

        return myDevice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
