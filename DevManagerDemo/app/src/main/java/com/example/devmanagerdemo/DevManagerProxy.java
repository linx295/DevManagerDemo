package com.example.devmanagerdemo;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.devmanagerdemo.interfaces.DevManagerCallbacks;
import com.example.devmanagerdemo.services.DevManagerService;


public class DevManagerProxy {
    private static final String TAG = DevManagerProxy.class.getSimpleName();

    
    //按键消息
    public static final int KEY_ERROR = -1;
    public static final int KEY_RELEASE = 0;
    public static final int KEY_PRESS = 1;
    


    private static DevManagerProxy mInstance;
    //远程服务Binder的代理
    private IDevAidlInterface mRemoteBinder;//远程binder
    private DevManagerService.DevLocalBinder mLocalBinder;//本地
    private Context mContext;
    private boolean mIsRemote = false;
    private boolean mBinding = false;//是否正在绑定
    private boolean mBinded = false;//是否绑定
    private DevManagerCallbacks mCustomCallbacks = null;//app客户端设置的回调

    //代理本地回调 用于转接 DevManagerservice的消息回调
    private DevManagerCallbacks mLocalCallbacks = new DevManagerCallbacks() {
        @Override
        public void onBatteryLevelChanged(@NonNull BluetoothDevice device, int batteryLevel) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onBatteryLevelChanged(device, batteryLevel);
            }
        }

        @Override
        public void onDeviceConnecting(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDeviceConnecting(device);
            }
        }

        @Override
        public void onDeviceConnected(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDeviceConnected(device);
            } else {
                Log.e(TAG, "onDeviceConnected:mCustomCallbacks is null!!!");
            }
        }

        @Override
        public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDeviceDisconnecting(device);
            }
        }

        @Override
        public void onDeviceDisconnected(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDeviceDisconnected(device);
            }
        }

        @Override
        public void onLinkLossOccurred(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onLinkLossOccurred(device);
            }
        }

        @Override
        public void onServicesDiscovered(@NonNull BluetoothDevice device, boolean optionalServicesFound) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onServicesDiscovered(device, optionalServicesFound);
            }
        }

        @Override
        public void onDeviceReady(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDeviceReady(device);
            }
        }

        @Override
        public void onBondingRequired(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onBondingRequired(device);
            }
        }

        @Override
        public void onBonded(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onBonded(device);
            }
        }

        @Override
        public void onBondingFailed(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onBondingFailed(device);
            }
        }

        @Override
        public void onError(@NonNull BluetoothDevice device, @NonNull String message, int errorCode) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onError(device, message, errorCode);
            }
        }

        @Override
        public void onDeviceNotSupported(@NonNull BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDeviceNotSupported(device);
            }
        }

        @Override
        public void onDataReceived(BluetoothDevice device, String rx) {
            if (mCustomCallbacks == null) {
                Log.e(TAG, "mCustomCallbacks is null!!!");
                return;
            }
            if (rx == null) {
                Log.e(TAG, "Dev(" + device.getAddress() + ") Receive nothing!!!");
                return;
            }
            Log.w(TAG, "onDataReceived:Dev(" + device.getAddress() + ") Receive:" + rx);
            mCustomCallbacks.onDataReceived(device, rx);
        }

        @Override
        public void onDataSent(BluetoothDevice device, String data) {

        }

        @Override
        public void onDevInformationRead(BluetoothDevice device, String manufacturer, String model, String serialNumber, String hardware, String firmware, String software) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onDevInformationRead(device, manufacturer, model, serialNumber, hardware, firmware, software);
            }
        }

        @Override
        public void onKeyEvent(BluetoothDevice device, int event) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onKeyEvent(device, event);
            }
        }

        @Override
        public void onFoundDevice(BluetoothDevice device) {
            if (mCustomCallbacks != null) {
                mCustomCallbacks.onFoundDevice(device);
            }
        }
    };

    //通过binder对象获得回调接口
    private IDevAidlCallbacks mRemoteCallbacks= IDevAidlCallbacks.Stub.asInterface(new IDevAidlCallbacks.Stub() {
        @Override
        public void onDeviceConnecting(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onDeviceConnecting(device);
        }

        @Override
        public void onDeviceConnected(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onDeviceConnected(device);
        }

        @Override
        public void onDeviceDisconnecting(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onDeviceDisconnecting(device);
        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onDeviceDisconnected(device);
        }

        @Override
        public void onLinkLossOccurred(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onLinkLossOccurred(device);
        }

        @Override
        public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) throws RemoteException {
            mLocalCallbacks.onServicesDiscovered(device, optionalServicesFound);
        }

        @Override
        public void onDeviceReady(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onDeviceReady(device);
        }

        @Override
        public void onBondingRequired(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onBondingRequired(device);
        }

        @Override
        public void onBonded(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onBonded(device);
        }

        @Override
        public void onBondingFailed(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onBondingFailed(device);
        }

        @Override
        public void onError(BluetoothDevice device, String message, int errorCode) throws RemoteException {
            mLocalCallbacks.onError(device, message, errorCode);
        }

        @Override
        public void onDeviceNotSupported(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onDeviceNotSupported(device);
        }

        @Override
        public void onDataReceived(BluetoothDevice device, String data) throws RemoteException {
            mLocalCallbacks.onDataReceived(device, data);
        }

        @Override
        public void onDataSent(BluetoothDevice device, String data) throws RemoteException {
            mLocalCallbacks.onDataSent(device, data);
        }

        @Override
        public void onBatteryLevelChanged(BluetoothDevice device, int batteryLevel) throws RemoteException {
            mLocalCallbacks.onBatteryLevelChanged(device, batteryLevel);
        }

        @Override
        public void onDevInformationRead(BluetoothDevice device, String manufacturer, String model, String serialNumber, String hardware, String firmware, String software) throws RemoteException {
            mLocalCallbacks.onDevInformationRead(device, manufacturer, model, serialNumber, hardware, firmware, software);
        }

        @Override
        public void onKeyEvent(BluetoothDevice device, int event) throws RemoteException {
            mLocalCallbacks.onKeyEvent(device, event);
        }

        @Override
        public void onFoundDevice(BluetoothDevice device) throws RemoteException {
            mLocalCallbacks.onFoundDevice(device);
        }
    });


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            if (mIsRemote) {
                mRemoteBinder = IDevAidlInterface.Stub.asInterface(service);

                try {
                    mRemoteBinder.addCallbacks(mRemoteCallbacks);

                    // and notify user if device is connected
                    for (final BluetoothDevice device : mRemoteBinder.getManagedDevices()) {
                        if (mRemoteBinder.isConnected(device))
                            mLocalCallbacks.onDeviceConnected(device);
                        else {
                            // If the device is not connected it means that either it is still connecting,
                            // or the link was lost and service is trying to connect to it (autoConnect=true).
                            mLocalCallbacks.onDeviceConnecting(device);
                        }
                    }
                    mBinded = true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "connected to the remote service");
            } else {
                mLocalBinder = (DevManagerService.DevLocalBinder) service;

                mLocalBinder.setCallbacks(mLocalCallbacks);

                // and notify user if device is connected
                for (final BluetoothDevice device : mLocalBinder.getManagedDevices()) {
                    if (mLocalBinder.isConnected(device))
                        mLocalCallbacks.onDeviceConnected(device);
                    else {
                        // If the device is not connected it means that either it is still connecting,
                        // or the link was lost and service is trying to connect to it (autoConnect=true).
                        mLocalCallbacks.onDeviceConnecting(device);
                    }
                }
                mBinded = true;
                Log.d(TAG, "connected to the local service");
            }
            mBinding = false;

        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            // Note: this method is called only when the service is killed by the system,
            // not when it stops itself or is stopped by the activity.
            // It will be called only when there is critically low memory, in practice never
            // when the activity is in foreground.
            mBinded = false;
            if (mIsRemote) {
                Log.d(TAG, "disconnected from the remote service");
                mRemoteBinder = null;
            } else {
                Log.d(TAG, "disconnected from the local service");
                if (mLocalBinder != null)
                    mLocalBinder.setCallbacks(null);
                mLocalBinder = null;
            }


            mBinding = false;
        }
    };


    /**
     *
     * @param ctx
     * @param isRemote 指定代理是否为远程代理
     * @return
     */
    public static DevManagerProxy getInstance(Context ctx, boolean isRemote) {
        if (mInstance == null) {
            mInstance = new DevManagerProxy(ctx, isRemote);
        }
        return mInstance;
    }



    private DevManagerProxy(Context ctx, boolean isRemote) {
        mContext = ctx;
        mIsRemote = isRemote;
    }


    public void bindService(Context ctx) {
        if (mBinding == true || mBinded == true) {
            Log.e(TAG, "bindService:mBinding="+mBinding+", mBinded="+mBinded);
            return;
        }

        mBinding = true;
        Intent intent;
        if (mIsRemote) {
            intent = new Intent();
            intent.setAction(this.getClass().getPackage().getName() + ".action.REMOTE_DEVMAN");
            intent.setPackage(this.getClass().getPackage().getName());
        } else {
            intent = new Intent(ctx, DevManagerService.class);
        }

        intent.putExtra(DevManagerService.EXTRA_REMOTE_FLAG, mIsRemote);

        ctx.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        mContext.unbindService(mServiceConnection);
    }

    public void startScan() {
        if (mIsRemote) {
            if (mRemoteBinder != null) {
                try {
                    mRemoteBinder.startScan();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "startScan:mRemoteBinder is null, please bind service");
            }
        } else {
            if (mLocalBinder != null) {
                mLocalBinder.startScan();
            } else {
                Log.e(TAG, "startScan:mLocalBinder is null, please bind service");
            }
        }
    }

    public void stopScan() {
        if (mIsRemote) {
            if (mRemoteBinder != null) {
                try {
                    mRemoteBinder.stopScan();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "stopScan:mRemoteBinder is null, please bind service");
            }
        } else {
            if (mLocalBinder != null) {
                mLocalBinder.stopScan();
            } else {
                Log.e(TAG, "stopScan:mLocalBinder is null, please bind service");
            }
        }
    }


    /**
     * Returns <code>true</code> if the device is connected. Services may not have been discovered yet.
     */
    public boolean isDeviceConnected(BluetoothDevice device) {
        if (mIsRemote) {
            try {
                return mRemoteBinder != null && mRemoteBinder.isConnected(device);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return mLocalBinder != null && mLocalBinder.isConnected(device);
        }
    }


    /**
     * 发送指令到设备
     *
     * @param device
     * @param cmd
     */
    public void send(@NonNull final BluetoothDevice device, byte[] cmd) {
        if (mIsRemote) {
            if (mRemoteBinder != null) {
                try {
                    mRemoteBinder.send(device, cmd);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "send:mRemoteBinder is null, please bind service");
            }
        } else {
            if (mLocalBinder != null) {
                mLocalBinder.send(device, cmd);
            } else {
                Log.e(TAG, "send:mLocalBinder is null, please bind service");
            }
        }
    }


    /**
     * 连接设备
     */
    public void connect(BluetoothDevice device) {
        if (mIsRemote) {
            if (mRemoteBinder != null) {
                try {
                    mRemoteBinder.connect(device);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "CAR connect failed:remote service not bind");
            }
        } else {
            if (mLocalBinder != null)
                mLocalBinder.connect(device);
            else
                Log.e(TAG, "CAR connect failed:local service not bind");
        }
    }

    /**
     * 断连指定设备
     */
    public void disconnect(BluetoothDevice device) {
        if (mIsRemote) {
            if (mRemoteBinder != null) {
                try {
                    mRemoteBinder.disconnect(device);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "CAR disconnect failed:remote service not bind");
            }
        } else {
            if (mLocalBinder != null)
                mLocalBinder.disconnect(device);
            else
                Log.e(TAG, "CAR disconnect failed:service not bind");
        }
    }


    public void setManagerCallbacks(DevManagerCallbacks callbacks) {
        mCustomCallbacks = callbacks;
    }
}
