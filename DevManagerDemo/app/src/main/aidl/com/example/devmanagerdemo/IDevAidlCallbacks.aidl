// IDevAidlCallbacks.aidl
package com.example.devmanagerdemo;

// Declare any non-default types here with import statements
import android.bluetooth.BluetoothDevice;


interface IDevAidlCallbacks {
    /**
     * Called when the Android device started connecting to given device.
     * The {@link #onDeviceConnected(in BluetoothDevice)} will be called when the device is connected,
     * or {@link #onError(in BluetoothDevice, String, int)} in case of error.
     *
     * @param device the device that got connected.
     */
    void onDeviceConnecting(in BluetoothDevice device);

    /**
     * Called when the device has been connected. This does not mean that the application may start
     * communication.
     * A service discovery will be handled automatically after this call. Service discovery
     * may ends up with calling {@link #onServicesDiscovered(in BluetoothDevice, boolean)} or
     * {@link #onDeviceNotSupported(in BluetoothDevice)} if required services have not been found.
     *
     * @param device the device that got connected.
     */
    void onDeviceConnected(in BluetoothDevice device);

    /**
     * Called when user initialized disconnection.
     *
     * @param device the device that gets disconnecting.
     */
    void onDeviceDisconnecting(in BluetoothDevice device);

    /**
     * Called when the device has disconnected (when the callback returned
     * {@link BluetoothGattCallback#onConnectionStateChange(BluetoothGatt, int, int)} with state
     * DISCONNECTED), but ONLY if the {@link BleManager#shouldAutoConnect()} method returned false
     * for this device when it was connecting.
     * Otherwise the {@link #onLinkLossOccurred(in BluetoothDevice)} method will be called instead.
     *
     * @param device the device that got disconnected.
     */
    void onDeviceDisconnected(in BluetoothDevice device);

    /**
     * This callback is invoked when the Ble Manager lost connection to a device that has been
     * connected with autoConnect option (see {@link BleManager#shouldAutoConnect()}.
     * Otherwise a {@link #onDeviceDisconnected(in BluetoothDevice)} method will be called on such
     * event.
     *
     * @param device the device that got disconnected due to a link loss.
     */
    void onLinkLossOccurred(in BluetoothDevice device);

    /**
     * Called when service discovery has finished and primary services has been found.
     * This method is not called if the primary, mandatory services were not found during service
     * discovery. For example in the Blood Pressure Monitor, a Blood Pressure service is a
     * primary service and Intermediate Cuff Pressure service is a optional secondary service.
     * Existence of battery service is not notified by this call.
     * <p>
     * After successful service discovery the service will initialize all services.
     * The {@link #onDeviceReady(in BluetoothDevice)} method will be called when the initialization
     * is complete.
     *
     * @param device                the device which services got disconnected.
     * @param optionalServicesFound if <code>true</code> the secondary services were also found
     *                              on the device.
     */
    void onServicesDiscovered(in BluetoothDevice device, boolean optionalServicesFound);

    /**
     * Method called when all initialization requests has been completed.
     *
     * @param device the device that get ready.
     */
    void onDeviceReady(in BluetoothDevice device);



    /**
     * Called when an {@link BluetoothGatt#GATT_INSUFFICIENT_AUTHENTICATION} error occurred and the
     * device bond state is {@link in BluetoothDevice#BOND_NONE}.
     *
     * @param device the device that requires bonding.
     */
    void onBondingRequired(in BluetoothDevice device);

    /**
     * Called when the device has been successfully bonded.
     *
     * @param device the device that got bonded.
     */
    void onBonded(in BluetoothDevice device);

    /**
     * Called when the bond state has changed from {@link in BluetoothDevice#BOND_BONDING} to
     * {@link in BluetoothDevice#BOND_NONE}.
     *
     * @param device the device that failed to bond.
     */
    void onBondingFailed(in BluetoothDevice device);

    /**
     * Called when a BLE error has occurred
     *
     * @param message   the error message.
     * @param errorCode the error code.
     * @param device    the device that caused an error.
     */
    void onError(in BluetoothDevice device,
                 String message, int errorCode);

    /**
     * Called when service discovery has finished but the main services were not found on the device.
     *
     * @param device the device that failed to connect due to lack of required services.
     */
    void onDeviceNotSupported(in BluetoothDevice device);



    void onDataReceived(in BluetoothDevice device, String data);


    void onDataSent(in BluetoothDevice device, String data);

    /**
     * Callback received each time the Battery Level value was read or has changed using
     * a notification.
     *
     * @param device       the target device.
     * @param batteryLevel the battery value in percent.
     */
    void onBatteryLevelChanged(in BluetoothDevice device, int batteryLevel);


    void onDevInformationRead(in BluetoothDevice device,
                              String manufacturer,
                              String model,
                              String serialNumber,
                              String hardware,
                              String firmware,
                              String software);


    void onKeyEvent(in BluetoothDevice device, int event);

    void onFoundDevice(in BluetoothDevice device);
}
