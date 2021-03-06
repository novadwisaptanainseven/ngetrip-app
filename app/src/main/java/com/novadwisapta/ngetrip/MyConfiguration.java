package com.novadwisapta.ngetrip;

import android.app.Application;

public class MyConfiguration extends Application {
    private String IPAddress = "http://192.168.42.242:8000/api/";
    private String IPAddressForFile = "http://192.168.42.242:8000/api/file/";

    public MyConfiguration() {

    }

    public String getIPAddress() {
        return IPAddress;
    }

    public String getIPAddressForFile() {
        return IPAddressForFile;
    }
}
