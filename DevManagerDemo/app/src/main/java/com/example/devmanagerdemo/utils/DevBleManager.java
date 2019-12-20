package com.example.devmanagerdemo.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.devmanagerdemo.interfaces.DevManagerCallbacks;

import java.util.UUID;

import no.nordicsemi.android.ble.WriteRequest;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.common.callback.battery.BatteryLevelDataCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.log.LogContract;


public class DevBleManager extends LoggableBleManager<DevManagerCallbacks> {
    private final static String TAG = DevBleManager.class.getSimpleName();

    /**
     * Nordic UART Service UUID
     */
    private final static UUID UART_SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    /**
     * RX characteristic UUID
     */
    private final static UUID UART_RX_CHARACTERISTIC_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    /**
     * TX characteristic UUID
     */
    private final static UUID UART_TX_CHARACTERISTIC_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    /**
     * < The UUID of the TP Characteristic.
     */ //触摸
    private final static UUID UART_TP_CHARACTERISTIC_UUID = UUID.fromString("6E400005-B5A3-F393-E0A9-E50E24DCCA9E");


    /**
     * Battery Service UUID.
     */
    private final static UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    /**
     * Battery Level characteristic UUID.
     */
    private final static UUID BATTERY_LEVEL_CHARACTERISTIC_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");


    /**
     * device information Service UUID.
     */
    private final static UUID DEVICE_INFORMATION_SERVICE_UUID = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    /**
     * < Manufacturer Name String characteristic UUID.
     */
    private final static UUID MANUFACTURER_NAME_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb");
    /**
     * < Model Number String characteristic UUID.
     */
    private final static UUID MODEL_NUMBER_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb");
    /**
     * < Serial Number String characteristic UUID.
     */
    private final static UUID SERIAL_NUMBER_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A25-0000-1000-8000-00805f9b34fb");
    /**
     * < Hardware Revision String characteristic UUID.
     */
    private final static UUID HARDWARE_REVISION_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb");
    /**
     * < Firmware Revision String characteristic UUID.
     */
    private final static UUID FIRMWARE_REVISION_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb");
    /**
     * < Software Revision String characteristic UUID.
     */
    private final static UUID SOFTWARE_REVISION_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mRXCharacteristic, mTXCharacteristic, mTPCharacteristic;
    /**
     * A flag indicating whether Long Write can be used. It's set to false if the UART RX
     * characteristic has only PROPERTY_WRITE_NO_RESPONSE property and no PROPERTY_WRITE.
     * If you set it to false here, it will never use Long Write.
     * <p>
     * TODO change this flag if you don't want to use Long Write even with Write Request.
     */
    private boolean mUseLongWrite = true;
    private BluetoothGattCharacteristic mBatteryLevelCharacteristic;
    /**
     * Last received Battery Level value.
     */
    private Integer mBatteryLevel;
    private BluetoothGattCharacteristic mManufacturerCharacteristic, mModelCharacteristic, mSerialNumberCharacteristic, mHardwareCharacteristic, mFirmwareCharacteristic, mSoftwareCharacteristic;
    private DevInfo mDevInfo = null;

    private DataReceivedCallback mBatteryLevelDataCallback = new BatteryLevelDataCallback() {
        @Override
        public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
            log(LogContract.Log.Level.APPLICATION, "Battery Level received: " + batteryLevel + "%");
            mBatteryLevel = batteryLevel;
            mCallbacks.onBatteryLevelChanged(device, batteryLevel);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device, final @NonNull Data data) {
            log(Log.WARN, "Invalid Battery Level data received: " + data);
        }
    };
    /**
     * BluetoothGatt callbacks for connection/disconnection, service discovery,
     * receiving indication, etc.
     */
    private final BleManagerGattCallback mGattCallback = new BleManagerGattCallback() {

        @Override
        protected void initialize() {
            //device information service
            readDevInformationCharacteristic();

            //uart service
            setNotificationCallback(mTXCharacteristic)
                    .with((device, data) -> {
                        final String text = data.getStringValue(0);
                        //log(LogContract.Log.Level.APPLICATION, "\"" + text + "\" received");
                        if (!TextUtils.isEmpty(text)) {
                            mCallbacks.onDataReceived(device, text.trim());
                        }
                    });
            requestMtu(260).enqueue();
            enableNotifications(mTXCharacteristic).enqueue();

            if (mTPCharacteristic != null) {
                setNotificationCallback(mTPCharacteristic)
                        .with((device, data) -> {
                            final int event = data.getIntValue(Data.FORMAT_UINT8, 0);
                            log(LogContract.Log.Level.APPLICATION, "touch \"" + event + "\" received");
                            mCallbacks.onKeyEvent(device, event);
                        });
                enableNotifications(mTPCharacteristic).enqueue();
            }


            //bat service
            readBatteryLevelCharacteristic();
            enableBatteryLevelCharacteristicNotifications();
        }

        @Override
        public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(UART_SERVICE_UUID);
            if (service != null) {
                mRXCharacteristic = service.getCharacteristic(UART_RX_CHARACTERISTIC_UUID);
                mTXCharacteristic = service.getCharacteristic(UART_TX_CHARACTERISTIC_UUID);
                mTPCharacteristic = service.getCharacteristic(UART_TP_CHARACTERISTIC_UUID);
            }

            boolean writeRequest = false;
            boolean writeCommand = false;
            if (mRXCharacteristic != null) {
                final int rxProperties = mRXCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
                writeCommand = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0;

                // Set the WRITE REQUEST type when the characteristic supports it.
                // This will allow to send long write (also if the characteristic support it).
                // In case there is no WRITE REQUEST property, this manager will divide texts
                // longer then MTU-3 bytes into up to MTU-3 bytes chunks.
                if (writeRequest)
                    mRXCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                else
                    mUseLongWrite = false;
            }

            return mRXCharacteristic != null && mTXCharacteristic != null && (writeRequest || writeCommand);
        }

        @Override
        protected boolean isOptionalServiceSupported(@NonNull final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(BATTERY_SERVICE_UUID);
            if (service != null) {
                mBatteryLevelCharacteristic = service.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC_UUID);
            }

            final BluetoothGattService service1 = gatt.getService(DEVICE_INFORMATION_SERVICE_UUID);
            if (service1 != null) {
                mManufacturerCharacteristic = service1.getCharacteristic(MANUFACTURER_NAME_STRING_CHARACTERISTIC_UUID);
                mModelCharacteristic = service1.getCharacteristic(MODEL_NUMBER_STRING_CHARACTERISTIC_UUID);
                mSerialNumberCharacteristic = service1.getCharacteristic(SERIAL_NUMBER_STRING_CHARACTERISTIC_UUID);
                mHardwareCharacteristic = service1.getCharacteristic(HARDWARE_REVISION_STRING_CHARACTERISTIC_UUID);
                mFirmwareCharacteristic = service1.getCharacteristic(FIRMWARE_REVISION_STRING_CHARACTERISTIC_UUID);
                mSoftwareCharacteristic = service1.getCharacteristic(SOFTWARE_REVISION_STRING_CHARACTERISTIC_UUID);
            }

            return mBatteryLevelCharacteristic != null ||
                    mManufacturerCharacteristic != null ||
                    mModelCharacteristic != null ||
                    mSerialNumberCharacteristic != null ||
                    mHardwareCharacteristic != null ||
                    mFirmwareCharacteristic != null ||
                    mSoftwareCharacteristic != null;
        }

        @Override
        protected void onDeviceDisconnected() {
            mRXCharacteristic = null;
            mTXCharacteristic = null;
            mTPCharacteristic = null;

            mBatteryLevelCharacteristic = null;
            mBatteryLevel = null;
            mUseLongWrite = true;

            mManufacturerCharacteristic = null;
            mModelCharacteristic = null;
            mSerialNumberCharacteristic = null;
            mHardwareCharacteristic = null;
            mFirmwareCharacteristic = null;
            mSoftwareCharacteristic = null;
        }
    };

    /**
     * The manager constructor.
     *
     * @param context context.
     */
    public DevBleManager(final Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return mGattCallback;
    }

    public void readBatteryLevelCharacteristic() {
        readCharacteristic(mBatteryLevelCharacteristic)
                .with(mBatteryLevelDataCallback)
                .fail((device, status) -> log(Log.WARN, "Battery Level characteristic not found"))
                .enqueue();
    }

    public void enableBatteryLevelCharacteristicNotifications() {
        // If the Battery Level characteristic is null, the request will be ignored
        setNotificationCallback(mBatteryLevelCharacteristic)
                .with(mBatteryLevelDataCallback);
        enableNotifications(mBatteryLevelCharacteristic)
                .done(device -> log(Log.INFO, "Battery Level notifications enabled"))
                .fail((device, status) -> log(Log.WARN, "Battery Level characteristic not found"))
                .enqueue();
    }

    /**
     * Disables Battery Level notifications on the Server.
     */
    public void disableBatteryLevelCharacteristicNotifications() {
        disableNotifications(mBatteryLevelCharacteristic)
                .done(device -> log(Log.INFO, "Battery Level notifications disabled"))
                .enqueue();
    }


    public void readDevInformationCharacteristic() {
        if (mSerialNumberCharacteristic != null && mDevInfo == null)
            mDevInfo = new DevInfo();

        readCharacteristic(mManufacturerCharacteristic)
                .with((device, data) -> {
                    final String text = data.getStringValue(0);
                    mDevInfo.Manufacturer = text;
                })
                .fail((device, status) -> log(Log.WARN, "mManufacturerCharacteristic not found"))
                .enqueue();
        readCharacteristic(mModelCharacteristic)
                .with((device, data) -> {
                    final String text = data.getStringValue(0);
                    mDevInfo.Model = text;
                })
                .fail((device, status) -> log(Log.WARN, "mModelCharacteristic not found"))
                .enqueue();
        readCharacteristic(mSerialNumberCharacteristic)
                .with((device, data) -> {
                    final String text = data.getStringValue(0);
                    mDevInfo.SerialNumber = text;
                })
                .fail((device, status) -> log(Log.WARN, "mSerialNumberCharacteristic not found"))
                .enqueue();
        readCharacteristic(mHardwareCharacteristic)
                .with((device, data) -> {
                    final String text = data.getStringValue(0);
                    mDevInfo.Hardware = text;
                })
                .fail((device, status) -> log(Log.WARN, "mHardwareCharacteristic not found"))
                .enqueue();
        readCharacteristic(mFirmwareCharacteristic)
                .with((device, data) -> {
                    final String text = data.getStringValue(0);
                    mDevInfo.Firmware = text;
                })
                .fail((device, status) -> log(Log.WARN, "mFirmwareCharacteristic not found"))
                .enqueue();
        readCharacteristic(mSoftwareCharacteristic)
                .with((device, data) -> {
                    final String text = data.getStringValue(0);
                    mDevInfo.Software = text;

                    //回调客户端监听接口
                    if (mDevInfo != null) {
                        mCallbacks.onDevInformationRead(device, mDevInfo.Manufacturer,
                                mDevInfo.Model, mDevInfo.SerialNumber, mDevInfo.Hardware,
                                mDevInfo.Firmware, mDevInfo.Software);
                    }
                })
                .fail((device, status) -> log(Log.WARN, "mSoftwareCharacteristic not found"))
                .enqueue();


    }

    /**
     * Sends the given text to RX characteristic.
     * @param cmd to be send
     */
    public void send(final byte[] cmd) {
        // Are we connected?
        if (mRXCharacteristic == null) {
            Log.e(TAG, "mRXCharacteristic is null");
            return;
        }

        if (cmd != null) {
            final WriteRequest request = writeCharacteristic(mRXCharacteristic, cmd)
                    .with((device, data) -> log(LogContract.Log.Level.APPLICATION,
                            "\"" + data.getStringValue(0) + "\" sent"));
            if (!mUseLongWrite) {
                // This will automatically split the long data into MTU-3-byte long packets.
                request.split();
            }
            request.enqueue();
        }
    }

    // This has been moved to the service in BleManager v2.0.
	/*@Override
	protected boolean shouldAutoConnect() {
		// We want the connection to be kept
		return true;
	}*/

    /**
     * Returns the last received Battery Level value.
     * The value is set to null when the device disconnects.
     *
     * @return Battery Level value, in percent.
     */
    public Integer getBatteryLevel() {
        return mBatteryLevel;
    }

    /**
     * 获取连接的小车信息 旧版小车没有信息则返回null，请改用老版命令方式获取版本号
     *
     * @return
     */
    public DevInfo getDevInfo() {
        return mDevInfo;
    }


    /*
     * 设备信息
     */
    public class DevInfo {
        public String Manufacturer;
        public String Model;
        public String SerialNumber;
        public String Hardware;
        public String Firmware;
        public String Software;
    }
}
