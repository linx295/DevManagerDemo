package com.example.devmanagerdemo.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.devmanagerdemo.IDevAidlCallbacks;
import com.example.devmanagerdemo.IDevAidlInterface;
import com.example.devmanagerdemo.interfaces.DevManagerCallbacks;
import com.example.devmanagerdemo.interfaces.UARTInterface;
import com.example.devmanagerdemo.utils.DevBleManager;
import com.example.devmanagerdemo.utils.LoggableBleManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DevManagerService extends BleMulticonnectProfileService {

    private static final String TAG = DevManagerService.class.getSimpleName();

    public static final String EXTRA_REMOTE_FLAG = "REMOTE_FLAG";

    private static final long SCAN_DURATION = 20000;//20s;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private boolean mIsScanning = false;//是否正在扫描
    private boolean mIsRemote = false;  //是否是远程bind

    //proxy 本地 callbacks
    private DevManagerCallbacks mLocalCallbacks;//本地回调接口

    //服务多个跨进程对象的远程binder callbacks 数组
    private CopyOnWriteArrayList<IDevAidlCallbacks> mRemoteCallbacksList = new CopyOnWriteArrayList<IDevAidlCallbacks>(); //远程回调接口

    //本地binder
    private DevLocalBinder mLocalBinder = new DevLocalBinder();
    /**
     * 服务回调接口路由 根据mIsRemote 决定远程回调或本地回调
     */
    private DevManagerCallbacks mDevManagerCallbacks = new DevManagerCallbacks() {
        @Override
        public void onBatteryLevelChanged(@NonNull BluetoothDevice device, int batteryLevel) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onBatteryLevelChanged(device, batteryLevel);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onBatteryLevelChanged:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onBatteryLevelChanged(device, batteryLevel);
                } else {
                    Log.e(TAG, "onBatteryLevelChanged:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDeviceConnecting(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDeviceConnecting(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDeviceConnecting:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDeviceConnecting(device);
                } else {
                    Log.e(TAG, "onDeviceConnecting:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDeviceConnected(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDeviceConnected(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDeviceConnected:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDeviceConnected(device);
                } else {
                    Log.e(TAG, "onDeviceConnected:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDeviceDisconnecting(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDeviceDisconnecting:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDeviceDisconnecting(device);
                } else {
                    Log.e(TAG, "onDeviceDisconnecting:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDeviceDisconnected(@NonNull BluetoothDevice device) {
            // Note: if BleManager#shouldAutoConnect() for this device returned true, this callback will be
            // invoked ONLY when user requested disconnection (using Disconnect button). If the device
            // disconnects due to a link loss, the onLinkLossOccurred(BluetoothDevice) method will be called instead.

            // We no longer want to keep the device in the service
            mManagedDevices.remove(device);
            // The BleManager is not removed from the HashMap in order to keep the device's log session.
            // mBleManagers.remove(device);

            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDeviceDisconnected(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDeviceDisconnected:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDeviceDisconnected(device);
                } else {
                    Log.e(TAG, "onDeviceDisconnected:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onLinkLossOccurred(@NonNull BluetoothDevice device) {
             if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onLinkLossOccurred(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onLinkLossOccurred:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onLinkLossOccurred(device);
                } else {
                    Log.e(TAG, "onLinkLossOccurred:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onServicesDiscovered(@NonNull BluetoothDevice device, boolean optionalServicesFound) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onServicesDiscovered(device, optionalServicesFound);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onServicesDiscovered:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onServicesDiscovered(device, optionalServicesFound);
                } else {
                    Log.e(TAG, "onServicesDiscovered:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDeviceReady(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDeviceReady(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDeviceReady:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDeviceReady(device);
                } else {
                    Log.e(TAG, "onDeviceReady:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onBondingRequired(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onBondingRequired(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onBondingRequired:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onBondingRequired(device);
                } else {
                    Log.e(TAG, "onBondingRequired:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onBonded(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onBonded(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onBonded:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onBonded(device);
                } else {
                    Log.e(TAG, "onBonded:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onBondingFailed(@NonNull BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onBondingFailed(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onBondingFailed:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onBondingFailed(device);
                } else {
                    Log.e(TAG, "onBondingFailed:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onError(@NonNull BluetoothDevice device, @NonNull String message, int errorCode) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onError(device, message, errorCode);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onError:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onError(device, message, errorCode);
                } else {
                    Log.e(TAG, "onError:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDeviceNotSupported(@NonNull BluetoothDevice device) {
            // We don't like this device, remove it from both collections
            mManagedDevices.remove(device);
            mBleManagers.remove(device);

            // no need for disconnecting, it will be disconnected by the manager automatically

            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDeviceNotSupported(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDeviceNotSupported:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDeviceNotSupported(device);
                } else {
                    Log.e(TAG, "onDeviceNotSupported:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDataReceived(BluetoothDevice device, String data) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDataReceived(device, data);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDataReceived:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDataReceived(device, data);
                } else {
                    Log.e(TAG, "onDataReceived:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDataSent(BluetoothDevice device, String data) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDataSent(device, data);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDataSent:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDataSent(device, data);
                } else {
                    Log.e(TAG, "onDataSent:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onDevInformationRead(BluetoothDevice device, String manufacturer, String model, String serialNumber, String hardware, String firmware, String software) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onDevInformationRead(device, manufacturer, model, serialNumber, hardware, firmware, software);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onDevInformationRead:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onDevInformationRead(device, manufacturer, model, serialNumber, hardware, firmware, software);
                } else {
                    Log.e(TAG, "onDevInformationRead:mLocalCallbacks is null");
                }
            }
        }


        @Override
        public void onKeyEvent(BluetoothDevice device, int event) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onKeyEvent(device, event);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onKeyEvent:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onKeyEvent(device, event);
                } else {
                    Log.e(TAG, "onKeyEvent:mLocalCallbacks is null");
                }
            }
        }

        @Override
        public void onFoundDevice(BluetoothDevice device) {
            if (mIsRemote) {
                if (mRemoteCallbacksList.size() > 0) {
                    try {
                        for (IDevAidlCallbacks cbs : mRemoteCallbacksList)
                            cbs.onFoundDevice(device);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onScanDevice:mRemoteCallbacksList is 0");
                }
            } else {
                if (mLocalCallbacks != null) {
                    mLocalCallbacks.onFoundDevice(device);
                } else {
                    Log.e(TAG, "onScanDevice:mLocalCallbacks is null");
                }
            }
        }

    };

    //蓝牙状态广播处理
    private final BroadcastReceiver mCommonBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BleMulticonnectProfileService.EXTRA_DEVICE);
            final String action = intent.getAction();
            switch (action) {
                case BleMulticonnectProfileService.BROADCAST_CONNECTION_STATE: {
                    final int state = intent.getIntExtra(BleMulticonnectProfileService.EXTRA_CONNECTION_STATE, BleMulticonnectProfileService.STATE_DISCONNECTED);

                    switch (state) {
                        case BleMulticonnectProfileService.STATE_CONNECTED: {
                            mDevManagerCallbacks.onDeviceConnected(bluetoothDevice);
                            break;
                        }
                        case BleMulticonnectProfileService.STATE_DISCONNECTED: {
                            mDevManagerCallbacks.onDeviceDisconnected(bluetoothDevice);
                            break;
                        }
                        case BleMulticonnectProfileService.STATE_LINK_LOSS: {
                            mDevManagerCallbacks.onLinkLossOccurred(bluetoothDevice);
                            break;
                        }
                        case BleMulticonnectProfileService.STATE_CONNECTING: {
                            mDevManagerCallbacks.onDeviceConnecting(bluetoothDevice);
                            break;
                        }
                        case BleMulticonnectProfileService.STATE_DISCONNECTING: {
                            mDevManagerCallbacks.onDeviceDisconnecting(bluetoothDevice);
                            break;
                        }
                        default:
                            // there should be no other actions
                            break;
                    }
                    break;
                }
                case BleMulticonnectProfileService.BROADCAST_SERVICES_DISCOVERED: {
                    final boolean primaryService = intent.getBooleanExtra(BleMulticonnectProfileService.EXTRA_SERVICE_PRIMARY, false);
                    final boolean secondaryService = intent.getBooleanExtra(BleMulticonnectProfileService.EXTRA_SERVICE_SECONDARY, false);

                    if (primaryService) {
                        mDevManagerCallbacks.onServicesDiscovered(bluetoothDevice, secondaryService);
                    } else {
                        mDevManagerCallbacks.onDeviceNotSupported(bluetoothDevice);
                    }
                    break;
                }
                case BleMulticonnectProfileService.BROADCAST_DEVICE_READY: {
                    mDevManagerCallbacks.onDeviceReady(bluetoothDevice);
                    break;
                }
                case BleMulticonnectProfileService.BROADCAST_BOND_STATE: {
                    final int state = intent.getIntExtra(BleMulticonnectProfileService.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                    switch (state) {
                        case BluetoothDevice.BOND_BONDING:
                            mDevManagerCallbacks.onBondingRequired(bluetoothDevice);
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            mDevManagerCallbacks.onBonded(bluetoothDevice);
                            break;
                    }
                    break;
                }
                case BleMulticonnectProfileService.BROADCAST_BATTERY_LEVEL: {
                    final int value = intent.getIntExtra(BleMulticonnectProfileService.EXTRA_BATTERY_LEVEL, -1);
                    if (value > 0)
                        mDevManagerCallbacks.onBatteryValueReceived(bluetoothDevice, value);
                    break;
                }
                case BleMulticonnectProfileService.BROADCAST_ERROR: {
                    final String message = intent.getStringExtra(BleMulticonnectProfileService.EXTRA_ERROR_MESSAGE);
                    final int errorCode = intent.getIntExtra(BleMulticonnectProfileService.EXTRA_ERROR_CODE, 0);
                    mDevManagerCallbacks.onError(bluetoothDevice, message, errorCode);
                    break;
                }
            }
        }
    };

    //扫描回调
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            final BluetoothDevice dev = result.getDevice();
            if (dev == null) {
                Log.e(TAG, " device is null,do nothing");
                return;
            }

            Log.w(TAG, dev.getName() + "|" + dev.getAddress() + " is found!");
            mDevManagerCallbacks.onFoundDevice(dev);


//            String devName = dev.getName();
//            if (devName != null && devName.equalsIgnoreCase(DEV_NAME) /*&& isFavoriteAddr(dev.getAddress()) && mManagedDevices.get(dev) == null*/) {
//                //stopScan();
//                //onDeviceSelected(dev, null);//TODO
//                Log.w(TAG, "Dev(" + dev.getName() + "|" + dev.getAddress() + ") connecting!!!");
//                connectDevice(dev);
//            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e(TAG, "onBatchScanResults " + results.toString());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "Dev onScanFailed errorCode=" + errorCode);
        }
    };
    //远程binder
    private IDevAidlInterface.Stub mRemoteBinder = new IDevAidlInterface.Stub() {
        @Override
        public void send(BluetoothDevice device, byte[] cmd) throws RemoteException {
            mLocalBinder.send(device, cmd);
        }

        @Override
        public void startScan() throws RemoteException {
            mLocalBinder.startScan();
        }

        @Override
        public void stopScan() throws RemoteException {
            mLocalBinder.stopScan();
        }

        @Override
        public void addCallbacks(IDevAidlCallbacks callbacks) throws RemoteException {
            if (!mRemoteCallbacksList.contains(callbacks))
                mRemoteCallbacksList.add(callbacks);
            callbacks.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    mRemoteCallbacksList.remove(callbacks); //客户端死亡时接口清空
                    Log.w(TAG, "binderDied, remove callbacks="+callbacks);
                }
            }, 0);
        }

        @Override
        public void removeCallbacks(IDevAidlCallbacks callbacks) throws RemoteException {
            if (mRemoteCallbacksList.contains(callbacks)) {
                mRemoteCallbacksList.remove(callbacks);
                Log.w(TAG, "remove by user: callbacks="+callbacks);
            } else {
                Log.w(TAG, "callbacks="+callbacks+" not add!");
            }
        }

        @Override
        public List<BluetoothDevice> getManagedDevices() throws RemoteException {
            return mLocalBinder.getManagedDevices();
        }


        @Override
        public void connect(BluetoothDevice device) throws RemoteException {
            mLocalBinder.connect(device);
        }

        @Override
        public void disconnect(BluetoothDevice device) throws RemoteException {
            mLocalBinder.disconnect(device);
        }

        @Override
        public boolean isConnected(BluetoothDevice device) throws RemoteException {
            return mLocalBinder.isConnected(device);
        }

    };

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleMulticonnectProfileService.BROADCAST_CONNECTION_STATE);
        intentFilter.addAction(BleMulticonnectProfileService.BROADCAST_SERVICES_DISCOVERED);
        intentFilter.addAction(BleMulticonnectProfileService.BROADCAST_DEVICE_READY);
        intentFilter.addAction(BleMulticonnectProfileService.BROADCAST_BOND_STATE);
        //intentFilter.addAction(BleMulticonnectProfileService.BROADCAST_BATTERY_LEVEL);
        intentFilter.addAction(BleMulticonnectProfileService.BROADCAST_ERROR);
        return intentFilter;
    }

    public static void openBT() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (adapter.isEnabled()) {
                //adapter.disable();
            } else {
                adapter.enable();
            }
        }
    }

    public static void closeBT() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (adapter.isEnabled()) {
                adapter.disable();
            }
        }
    }

//    private void connectDevice(final BluetoothDevice device) {
//        if (mIsRemote) {
//            try {
//                mRemoteBinder.connect(device);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mLocalBinder.connect(device);
//        }
//    }

    @Override
    protected LoggableBleManager<DevManagerCallbacks> initializeManager() {
        DevBleManager manager = new DevBleManager(this);
        manager.setGattCallbacks(mDevManagerCallbacks);//此处设置接口路由
        return manager;
    }

    @Override
    protected Binder getBinder() {
        if (mIsRemote) {
            Log.w(TAG, "getBinder: mRemoteBinder=" + mRemoteBinder);
            return mRemoteBinder;
        } else {
            Log.w(TAG, "getBinder: mLocalBinder=" + mLocalBinder);
            return mLocalBinder;
        }
    }

    @Override
    public IBinder onBind(final Intent intent) {
        if (intent != null) {
            mIsRemote = intent.getBooleanExtra(EXTRA_REMOTE_FLAG, false);
        }

        Log.w(TAG, "onBind: remote=" + mIsRemote);

        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        openBT();
        LocalBroadcastManager.getInstance(this).registerReceiver(mCommonBroadcastReceiver, makeIntentFilter());

        mHandlerThread = new HandlerThread("CarManager-Thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCommonBroadcastReceiver);

        if (mHandlerThread != null)
            mHandlerThread.quit();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent != null) {
            mIsRemote = intent.getBooleanExtra(EXTRA_REMOTE_FLAG, false);
        }

        Log.w(TAG, "onStartCommand: remote=" + mIsRemote);

        return super.onStartCommand(intent, flags, startId);
    }


    public class DevLocalBinder extends LocalBinder implements UARTInterface {
        public DevManagerService getService() {
            return DevManagerService.this;
        }

        /**
         * proxy对象 通过 BleManager 对象 发送命令字给对应的蓝牙设备
         *
         * @param device
         * @param cmd
         */
        @Override
        public void send(final BluetoothDevice device, final byte[] cmd) {
            if (device == null) {
                Log.e(TAG, "device is null,do nothing!!!");
                return;
            }
            ((DevBleManager) getBleManager(device)).send(cmd);
        }



        /**
         * 设置proxy local DevManagerCallbacks
         *
         * @param callbacks
         */
        public void setCallbacks(DevManagerCallbacks callbacks) {
            mLocalCallbacks = callbacks;
        }

        public void startScan() {
            Log.d(TAG, "startScan...");
            if (mBluetoothAdapter == null) {
                final BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = manager.getAdapter();
                if (mBluetoothAdapter != null && mScanner == null)
                    mScanner = mBluetoothAdapter.getBluetoothLeScanner();
            }
            mIsScanning = true;
            if (mScanner != null)
                mScanner.startScan(mScanCallback);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan();
                }
            }, SCAN_DURATION);
        }

        /**
         * Stop scan if user tap Cancel button
         */
        public void stopScan() {
            if (mIsScanning) {
                Log.w(TAG, "stopScan!!! ");
                if (mScanner != null)
                    mScanner.stopScan(mScanCallback);
                mIsScanning = false;
            }
        }
    }
}
