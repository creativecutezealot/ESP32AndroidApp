package com.nader.esp32connectionapp.Models;

public class DeviceModal {
    private String deviceName;
    private String deviceAddress;

    public  DeviceModal(String deviceName,String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceNamee) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }
    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
