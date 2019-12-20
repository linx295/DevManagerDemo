// IDevAidlInterface.aidl
package com.example.devmanagerdemo;

// Declare any non-default types here with import statements
import android.bluetooth.BluetoothDevice;
import com.example.devmanagerdemo.IDevAidlCallbacks;

interface IDevAidlInterface {
    // send cmd to ble device
    void send(in BluetoothDevice device, in byte[] cmd);

    //start ble car scanner
    void startScan();

    // Stop scan if user cancel/or search target car/
    void stopScan();

    void addCallbacks(in IDevAidlCallbacks callbacks);

    void removeCallbacks(in IDevAidlCallbacks callbacks);

    /**
     * Returns an unmodifiable list of devices managed by the service.
     * The returned devices do not need to be connected at tha moment. Each of them was however created
     * using {@link #connect(BluetoothDevice)} method so they might have been connected before and disconnected.
     *
     * @return unmodifiable list of devices managed by the service
     */
    List<BluetoothDevice> getManagedDevices();

    /**
     * Connects to the given device. If the device is already connected this method does nothing.
     *
     * @param device target Bluetooth device
     */
    void connect(in BluetoothDevice device);

    /**
     * Disconnects the given device and removes the associated BleManager object.
     * If the list of BleManagers is empty while the last activity unbinds from the service,
     * the service will stop itself.
     *
     * @param device target device to disconnect and forget
     */
    void disconnect(in BluetoothDevice device);

    /**
     * Returns <code>true</code> if the device is connected to the sensor.
     *
     * @param device the target device
     * @return <code>true</code> if device is connected to the sensor, <code>false</code> otherwise
     */
    boolean isConnected(in BluetoothDevice device);
}
