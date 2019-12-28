# DevManagerDemo

一个基于nrf5x ble uart服务管理框架示例
详细说明：
https://blog.csdn.net/qq_42237638/article/details/103545778



## 构建属性服务管理类
本节所述的属性服务内容参见 [NRF52x属性服务示例](https://blog.csdn.net/qq_42237638/article/details/103247432) 。
创建一个DevBleManager类，整合以下几方面内容：
+ 原UARTManager功能及触摸按键扩充属性访问
+ 原BatteryManager功能
+ 设备信息服务属性
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191221222739402.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)


1. 电池服务
```java
	// Battery Service UUID.
    private final static UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    // Battery Level characteristic UUID.
    private final static UUID BATTERY_LEVEL_CHARACTERISTIC_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
	private BluetoothGattCharacteristic mBatteryLevelCharacteristic;
	
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

```

2. 设备信息服务	

```java
	// device information Service UUID.
    private final static UUID DEVICE_INFORMATION_SERVICE_UUID = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    // < Manufacturer Name String characteristic UUID.
    private final static UUID MANUFACTURER_NAME_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb");
    //其他几个属性值 参考固件实现：[NRF52x属性服务示例](https://blog.csdn.net/qq_42237638/article/details/103247432)
    ...
    // < Software Revision String characteristic UUID.
    private final static UUID SOFTWARE_REVISION_STRING_CHARACTERISTIC_UUID = UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mManufacturerCharacteristic, /*..., */ mSoftwareCharacteristic;
    private DevInfo mDevInfo = null;
    /*
     * 设备信息
     */
    public class DevInfo {
        public String Manufacturer;
        //other fields...
        public String Software;
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
        //其他几个属性值的访问
        ...
        //最后一个属性值
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

```
   
3. 触摸按键属性访问

```java
	/**
     * < The UUID of the TP Characteristic.
     */ //触摸
    private final static UUID UART_TP_CHARACTERISTIC_UUID = UUID.fromString("6E400005-B5A3-F393-E0A9-E50E24DCCA9E");
    private BluetoothGattCharacteristic mTPCharacteristic;    

```

4. 扩展 BleManagerGattCallback 对象内容

```java
 	/**
     * BluetoothGatt callbacks for connection/disconnection, service discovery,
     * receiving indication, etc.
     */
    private final BleManagerGattCallback mGattCallback = new BleManagerGattCallback() {

        @Override
        protected void initialize() {
            //设备信息属性访问
            readDevInformationCharacteristic();

            //uart service
            setNotificationCallback(mTXCharacteristic)
                    .with((device, data) -> {
                        final String text = data.getStringValue(0);
                        if (!TextUtils.isEmpty(text)) {
                            mCallbacks.onDataReceived(device, text.trim());
                        }
                    });
            requestMtu(260).enqueue();
            enableNotifications(mTXCharacteristic).enqueue();

			//添加的触摸按键属性
            if (mTPCharacteristic != null) {
                setNotificationCallback(mTPCharacteristic)
                        .with((device, data) -> {
                            final int event = data.getIntValue(Data.FORMAT_UINT8, 0);                            
                            mCallbacks.onKeyEvent(device, event);
                        });
                enableNotifications(mTPCharacteristic).enqueue();
            }
            
            //电池服务访问
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

            //other code...

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
                //...
                mSoftwareCharacteristic = service1.getCharacteristic(SOFTWARE_REVISION_STRING_CHARACTERISTIC_UUID);
            }

            return mBatteryLevelCharacteristic != null ||
                    //other char...
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
            //other char...
            mSoftwareCharacteristic = null;
        }
    };
```

5. 定义DevManagerCallbacks
   Manager类需要将 设备信息、电池电量、按键事件 等设备数据传递出去，所以需要扩展UARTManagerCallbacks接口：
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191221222709446.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)
   其中UARTManagerCallbacks、BatteryLevelCallback使用官方源码，DevServiceCallbacks内容如下：  

```java
	/**
 	* device uart ext char  & device information service char
 	*/
	public interface DevServiceCallbacks {
		/**
		* 设备信息
		*/
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
     	* ScanResult found device 扫描结果
     	* @param device
     	*/
    	void onFoundDevice(BluetoothDevice device);
	}

```

<br>   
<br>

## 构建核心框架类
上节已经实现了关键的设备管理访问接口==DevBleManager==类，本节主要描述如何构建一个支持本地/跨进程服务框架。
这个框架对DevBleManager集合进行管理并提供对外交互的接口。
- 跨进程系统框架图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191219110249269.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)
本框架的设计目标是支持多app客户端与设备管理服务app统一交互，各app控制它自己关心的ble设备，完成丰富的互动。
举个例子：
    app1 为小坦克控制端;
    app2 为小飞机控制端;
    app3 为小舰艇控制端;
    service app仅负责各app与ble设备之间的数据交换，不负责具体内容。是不是有一种打造海陆空三军指挥中心的感觉 : )
<br>

- 框架核心类图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191219111313146.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)
1. 借助android的binder通信实现设备管理代理模式，DevManagerProxy 本地模式绑定 DevLocalBinder，远程模式绑定 IDevAidlInterface.Stub ：
```java
	//DevManagerService.java
	//本地binder
	private DevLocalBinder mLocalBinder = new DevLocalBinder();
	//远程binder
    private IDevAidlInterface.Stub mRemoteBinder = new IDevAidlInterface.Stub() {
    	//code...
    }
	
    @Override
    public IBinder onBind(final Intent intent) {
        if (intent != null) {
            mIsRemote = intent.getBooleanExtra(EXTRA_REMOTE_FLAG, false);
        }

        Log.w(TAG, "onBind: remote=" + mIsRemote);

        if (mIsRemote) {
            Log.w(TAG, "getBinder: mRemoteBinder=" + mRemoteBinder);
            return mRemoteBinder;
        } else {
            Log.w(TAG, "getBinder: mLocalBinder=" + mLocalBinder);
            return mLocalBinder;
        }
    }
	
	//DevManagerProxy.java
	/**     
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


	//app客户端
	//构建DevManagerProxy对象 指定为远程模式
	Context ctx = getApplicationContext();
	mDevManagerProxy = DevManagerProxy.getInstance(ctx, true);
	mDevManagerProxy.setManagerCallbacks(mDevManagerCallbacks);
	mDevManagerProxy.bindService(ctx);
```

2. 取得binder对象后，通过binder对象的 DevLocalBinder.setCallbacks（本地）/ IDevAidlInterface.Stub.addCallbacks（远程）设置客户端回调接口：

```java
	//DevManagerService.java
	//本地binder	
	public class DevLocalBinder extends LocalBinder implements UARTInterface {
       
        /**
         * 设置proxy local DevManagerCallbacks        
         * @param callbacks
         */
        public void setCallbacks(DevManagerCallbacks callbacks) {
            mLocalCallbacks = callbacks;
        }
    }
    
	//远程binder
    private IDevAidlInterface.Stub mRemoteBinder = new IDevAidlInterface.Stub() {
        //other method...
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
     }

	//DevManagerProxy.java
	private ServiceConnection mServiceConnection = new ServiceConnection() {
        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            if (mIsRemote) {
                mRemoteBinder = IDevAidlInterface.Stub.asInterface(service);
                try {
                    mRemoteBinder.addCallbacks(mRemoteCallbacks);
                    //other code...
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
                //other code...
                mBinded = true;
                Log.d(TAG, "connected to the local service");
            }
            mBinding = false;

        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
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
```

3. app端通过DevManagerProxy对象执行如下操作
    + step1   startScan扫描设备
    + step2   选中扫到设备并发起connect
    + step3   设备ready后发送 hello
    + step4   app端收到设备回复 world

```java
	//app客户端
	mDevManagerProxy.startScan();

	
	private DevManagerCallbacks mDevManagerCallbacks = new DevManagerCallbacks() {
        @Override
        public void onDataReceived(BluetoothDevice device, String data) {
            Log.w(TAG, "onDataReceived[" + device.getAddress() + "]:" + data);
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
        public void onDeviceConnected(@NonNull BluetoothDevice device) {
            Log.w(TAG, "onDeviceConnected[" + device.getAddress() + "]");
            //登记已连接的设备，多设备管理用容器存储
            if (mDevice == null) {
                mDevice = device;
            }
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
        //other callback...
    };
```

4. 上述app/service通信时序图:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191228121844740.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)

5. 实测log
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191219120447451.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)
<br>
<br> 


- 代理模式的优点

   新框架编程模式如下
   
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191228121919545.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMjM3NjM4,size_16,color_FFFFFF,t_70)
1. 无需让activity继承BleProfileServiceReadyActivity，将蓝牙相关的操作封装到代理对象中，实现界面与业务逻辑解耦。   
2. 弃用原框架的服务-设备 一对一模式，改用一对多模式，即 BleProfileService -> BleMulticonnectProfileService
    一对多即包含一对一的特例，并且可以由客户程序自行决定管理设备的个数。 
3. 支持本地和远程服务两种bind模式，灵活运用到各种软件框架下实现高效ble设备控制，尤其适用于无屏幕的交互设备。
4. 蓝牙状态回调抛弃之前的本地广播方式，采用本地/远程回调方案替代，更加高效且易于移植。
 
<br>
<br>


