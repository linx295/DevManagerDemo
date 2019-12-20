package com.example.devmanagerdemo;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.devmanagerdemo.interfaces.DevManagerCallbacks;


/**
 * @author linx
 */
public class MyAppActivity1 extends Activity {

    private static final String TAG = "MyAppActivity1";
    //msg what
    //与命令词对应，值要对应
    private static final int START_SCAN = 1;
    private static final String MINI_CAR = "MINI_BYCAR01";

    //小车相关
    private HandlerThread mWorkHandlerThrd;
    private Handler mWorkHandler;
    private DevManagerProxy mDevManagerProxy;
    private BluetoothDevice mDevice;


    private DevManagerCallbacks mDevManagerCallbacks = new DevManagerCallbacks() {
        @Override
        public void onDataReceived(BluetoothDevice device, String data) {
            Log.w(TAG, "onDataReceived[" + device.getAddress() + "]:" + data);
        }

        @Override
        public void onDataSent(BluetoothDevice device, String data) {

        }

        @Override
        public void onDevInformationRead(BluetoothDevice device, String manufacturer, String model, String serialNumber, String hardware, String firmware, String software) {
            Log.w(TAG, "onDevInformationRead[" + device.getAddress() + "]:\r\n" +
                    "\t\t\t\tmanufacturer:\t" + manufacturer + "\r\n" +
                    "\t\t\t\tmodel       :\t" + model + "\r\n" +
                    "\t\t\t\tserialNumber:\t" + serialNumber + "\r\n" +
                    "\t\t\t\thardware    :\t" + hardware + "\r\n" +
                    "\t\t\t\tfirmware    :\t" + firmware + "\r\n" +
                    "\t\t\t\tsoftware    :\t" + software + "\r\n");
        }

        @Override
        public void onKeyEvent(BluetoothDevice device, int event) {
            Log.w(TAG, "onKeyEvent[" + device.getAddress() + "]:" + event);
        }

        @Override
        public void onFoundDevice(BluetoothDevice device) {
            /*
             * 扫到一个目标设备 连接设备 停止扫描
             * 如果需要多个设备 可以在app层添加控制数据结构 并保持扫描状态
             */
            Log.w(TAG, "onFoundDevice[" + device.getAddress() + "]:" + device.getName());
            if (MINI_CAR.equalsIgnoreCase(device.getName())) {
                mDevManagerProxy.stopScan();
                mDevManagerProxy.connect(device);
            }
        }


        @Override
        public void onDeviceConnecting(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onDeviceConnecting[" + device.getAddress() + "]");
        }

        @Override
        public void onDeviceConnected(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onDeviceConnected[" + device.getAddress() + "]");
            //登记已连接的设备，多设备管理用容器存储
            if (mDevice == null) {
                mDevice = device;
            }
        }

        @Override
        public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onDeviceDisconnecting[" + device.getAddress() + "]");
        }

        @Override
        public void onDeviceDisconnected(@NonNull BluetoothDevice device) {
            mDevice = null;
            Log.w(TAG, "onDeviceDisconnected[" + device.getAddress() + "]");
        }

        @Override
        public void onLinkLossOccurred(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onLinkLossOccurred[" + device.getAddress() + "]");
        }

        @Override
        public void onServicesDiscovered(@NonNull BluetoothDevice device, boolean optionalServicesFound) {
            Log.w(TAG, "onServicesDiscovered[" + device.getAddress() + "," + optionalServicesFound + "]");
        }

        @Override
        public void onDeviceReady(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onDeviceReady[" + device.getAddress() + "]");
            //设备就绪后周期性的发送指令
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.w(TAG, "send to Dev[" + device.getAddress() + "]:hello");
                    mDevManagerProxy.send(device, "hello".getBytes());
                    mWorkHandler.postDelayed(this, 1000);
                }
            });
        }

        @Override
        public void onBondingRequired(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onBondingRequired[" + device.getAddress() + "]");
        }

        @Override
        public void onBonded(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onBonded[" + device.getAddress() + "]");
        }

        @Override
        public void onBondingFailed(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onBondingFailed[" + device.getAddress() + "]");
        }

        @Override
        public void onError(@NonNull BluetoothDevice device, @NonNull String message, int errorCode) {
            Log.w(TAG, "onError[" + device.getAddress() + "]" + message + "," + errorCode);
        }

        @Override
        public void onDeviceNotSupported(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onDeviceNotSupported[" + device.getAddress() + "]");
        }

        @Override
        public void onBatteryLevelChanged(@NonNull BluetoothDevice device, int batteryLevel) {
            Log.w(TAG, "onBatteryLevelChanged[" + device.getAddress() + "]" + batteryLevel);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDevManagerProxy = DevManagerProxy.getInstance(getApplicationContext(), true);
        mDevManagerProxy.setManagerCallbacks(mDevManagerCallbacks);


        mWorkHandlerThrd = new HandlerThread("WorkThread");
        mWorkHandlerThrd.start();
        mWorkHandler = new Handler(mWorkHandlerThrd.getLooper(), new WorkCallback());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDevManagerProxy.bindService(getApplicationContext());

        mWorkHandler.sendEmptyMessageDelayed(START_SCAN, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWorkHandlerThrd != null)
            mWorkHandlerThrd.quit();

        if (mDevManagerProxy != null)
            mDevManagerProxy.unbindService();
    }


    /**
     * 该callback运行于子线程，所有与设备相关的指令都委托次线程工作
     */
    class WorkCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case START_SCAN:
                        Log.e(TAG, "startScan");
                        mDevManagerProxy.startScan();
                        break;

                    default:
                        Log.e(TAG, "Error cmd:" + msg.what);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }


}
