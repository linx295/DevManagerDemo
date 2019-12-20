package com.example.devmanagerdemo.interfaces;

import android.bluetooth.BluetoothDevice;

/**
 * device uart ext char  & device information service char
 */
public interface DevServiceCallbacks {
    void onDevInformationRead(BluetoothDevice device,
                              String manufacturer,
                              String model,
                              String serialNumber,
                              String hardware,
                              String firmware,
                              String software);

    /**
     * 按键事件
     * @param device
     * @param event
     */
    void onKeyEvent(BluetoothDevice device, int event);

    /**
     * ScanResult found device
     * @param device
     */
    void onFoundDevice(BluetoothDevice device);
}
