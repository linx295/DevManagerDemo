/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.example.devmanagerdemo;
public interface IDevAidlCallbacks extends android.os.IInterface
{
  /** Default implementation for IDevAidlCallbacks. */
  public static class Default implements com.example.devmanagerdemo.IDevAidlCallbacks
  {
    /**
         * Called when the Android device started connecting to given device.
         * The {@link #onDeviceConnected(in BluetoothDevice)} will be called when the device is connected,
         * or {@link #onError(in BluetoothDevice, String, int)} in case of error.
         *
         * @param device the device that got connected.
         */
    @Override public void onDeviceConnecting(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when the device has been connected. This does not mean that the application may start
         * communication.
         * A service discovery will be handled automatically after this call. Service discovery
         * may ends up with calling {@link #onServicesDiscovered(in BluetoothDevice, boolean)} or
         * {@link #onDeviceNotSupported(in BluetoothDevice)} if required services have not been found.
         *
         * @param device the device that got connected.
         */
    @Override public void onDeviceConnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when user initialized disconnection.
         *
         * @param device the device that gets disconnecting.
         */
    @Override public void onDeviceDisconnecting(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when the device has disconnected (when the callback returned
         * {@link BluetoothGattCallback#onConnectionStateChange(BluetoothGatt, int, int)} with state
         * DISCONNECTED), but ONLY if the {@link BleManager#shouldAutoConnect()} method returned false
         * for this device when it was connecting.
         * Otherwise the {@link #onLinkLossOccurred(in BluetoothDevice)} method will be called instead.
         *
         * @param device the device that got disconnected.
         */
    @Override public void onDeviceDisconnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * This callback is invoked when the Ble Manager lost connection to a device that has been
         * connected with autoConnect option (see {@link BleManager#shouldAutoConnect()}.
         * Otherwise a {@link #onDeviceDisconnected(in BluetoothDevice)} method will be called on such
         * event.
         *
         * @param device the device that got disconnected due to a link loss.
         */
    @Override public void onLinkLossOccurred(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
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
    @Override public void onServicesDiscovered(android.bluetooth.BluetoothDevice device, boolean optionalServicesFound) throws android.os.RemoteException
    {
    }
    /**
         * Method called when all initialization requests has been completed.
         *
         * @param device the device that get ready.
         */
    @Override public void onDeviceReady(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when an {@link BluetoothGatt#GATT_INSUFFICIENT_AUTHENTICATION} error occurred and the
         * device bond state is {@link in BluetoothDevice#BOND_NONE}.
         *
         * @param device the device that requires bonding.
         */
    @Override public void onBondingRequired(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when the device has been successfully bonded.
         *
         * @param device the device that got bonded.
         */
    @Override public void onBonded(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when the bond state has changed from {@link in BluetoothDevice#BOND_BONDING} to
         * {@link in BluetoothDevice#BOND_NONE}.
         *
         * @param device the device that failed to bond.
         */
    @Override public void onBondingFailed(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Called when a BLE error has occurred
         *
         * @param message   the error message.
         * @param errorCode the error code.
         * @param device    the device that caused an error.
         */
    @Override public void onError(android.bluetooth.BluetoothDevice device, java.lang.String message, int errorCode) throws android.os.RemoteException
    {
    }
    /**
         * Called when service discovery has finished but the main services were not found on the device.
         *
         * @param device the device that failed to connect due to lack of required services.
         */
    @Override public void onDeviceNotSupported(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    @Override public void onDataReceived(android.bluetooth.BluetoothDevice device, java.lang.String data) throws android.os.RemoteException
    {
    }
    @Override public void onDataSent(android.bluetooth.BluetoothDevice device, java.lang.String data) throws android.os.RemoteException
    {
    }
    /**
         * Callback received each time the Battery Level value was read or has changed using
         * a notification.
         *
         * @param device       the target device.
         * @param batteryLevel the battery value in percent.
         */
    @Override public void onBatteryLevelChanged(android.bluetooth.BluetoothDevice device, int batteryLevel) throws android.os.RemoteException
    {
    }
    @Override public void onDevInformationRead(android.bluetooth.BluetoothDevice device, java.lang.String manufacturer, java.lang.String model, java.lang.String serialNumber, java.lang.String hardware, java.lang.String firmware, java.lang.String software) throws android.os.RemoteException
    {
    }
    @Override public void onKeyEvent(android.bluetooth.BluetoothDevice device, int event) throws android.os.RemoteException
    {
    }
    @Override public void onFoundDevice(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.example.devmanagerdemo.IDevAidlCallbacks
  {
    private static final java.lang.String DESCRIPTOR = "com.example.devmanagerdemo.IDevAidlCallbacks";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.example.devmanagerdemo.IDevAidlCallbacks interface,
     * generating a proxy if needed.
     */
    public static com.example.devmanagerdemo.IDevAidlCallbacks asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.example.devmanagerdemo.IDevAidlCallbacks))) {
        return ((com.example.devmanagerdemo.IDevAidlCallbacks)iin);
      }
      return new com.example.devmanagerdemo.IDevAidlCallbacks.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_onDeviceConnecting:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onDeviceConnecting(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDeviceConnected:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onDeviceConnected(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDeviceDisconnecting:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onDeviceDisconnecting(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDeviceDisconnected:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onDeviceDisconnected(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onLinkLossOccurred:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onLinkLossOccurred(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onServicesDiscovered:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onServicesDiscovered(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDeviceReady:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onDeviceReady(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onBondingRequired:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onBondingRequired(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onBonded:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onBonded(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onBondingFailed:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onBondingFailed(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onError:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.onError(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDeviceNotSupported:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onDeviceNotSupported(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDataReceived:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onDataReceived(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDataSent:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onDataSent(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onBatteryLevelChanged:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _arg1;
          _arg1 = data.readInt();
          this.onBatteryLevelChanged(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onDevInformationRead:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String _arg3;
          _arg3 = data.readString();
          java.lang.String _arg4;
          _arg4 = data.readString();
          java.lang.String _arg5;
          _arg5 = data.readString();
          java.lang.String _arg6;
          _arg6 = data.readString();
          this.onDevInformationRead(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onKeyEvent:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _arg1;
          _arg1 = data.readInt();
          this.onKeyEvent(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onFoundDevice:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onFoundDevice(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.example.devmanagerdemo.IDevAidlCallbacks
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      /**
           * Called when the Android device started connecting to given device.
           * The {@link #onDeviceConnected(in BluetoothDevice)} will be called when the device is connected,
           * or {@link #onError(in BluetoothDevice, String, int)} in case of error.
           *
           * @param device the device that got connected.
           */
      @Override public void onDeviceConnecting(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDeviceConnecting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDeviceConnecting(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when the device has been connected. This does not mean that the application may start
           * communication.
           * A service discovery will be handled automatically after this call. Service discovery
           * may ends up with calling {@link #onServicesDiscovered(in BluetoothDevice, boolean)} or
           * {@link #onDeviceNotSupported(in BluetoothDevice)} if required services have not been found.
           *
           * @param device the device that got connected.
           */
      @Override public void onDeviceConnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDeviceConnected, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDeviceConnected(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when user initialized disconnection.
           *
           * @param device the device that gets disconnecting.
           */
      @Override public void onDeviceDisconnecting(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDeviceDisconnecting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDeviceDisconnecting(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when the device has disconnected (when the callback returned
           * {@link BluetoothGattCallback#onConnectionStateChange(BluetoothGatt, int, int)} with state
           * DISCONNECTED), but ONLY if the {@link BleManager#shouldAutoConnect()} method returned false
           * for this device when it was connecting.
           * Otherwise the {@link #onLinkLossOccurred(in BluetoothDevice)} method will be called instead.
           *
           * @param device the device that got disconnected.
           */
      @Override public void onDeviceDisconnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDeviceDisconnected, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDeviceDisconnected(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * This callback is invoked when the Ble Manager lost connection to a device that has been
           * connected with autoConnect option (see {@link BleManager#shouldAutoConnect()}.
           * Otherwise a {@link #onDeviceDisconnected(in BluetoothDevice)} method will be called on such
           * event.
           *
           * @param device the device that got disconnected due to a link loss.
           */
      @Override public void onLinkLossOccurred(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onLinkLossOccurred, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onLinkLossOccurred(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
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
      @Override public void onServicesDiscovered(android.bluetooth.BluetoothDevice device, boolean optionalServicesFound) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(((optionalServicesFound)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onServicesDiscovered, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onServicesDiscovered(device, optionalServicesFound);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Method called when all initialization requests has been completed.
           *
           * @param device the device that get ready.
           */
      @Override public void onDeviceReady(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDeviceReady, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDeviceReady(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when an {@link BluetoothGatt#GATT_INSUFFICIENT_AUTHENTICATION} error occurred and the
           * device bond state is {@link in BluetoothDevice#BOND_NONE}.
           *
           * @param device the device that requires bonding.
           */
      @Override public void onBondingRequired(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onBondingRequired, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onBondingRequired(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when the device has been successfully bonded.
           *
           * @param device the device that got bonded.
           */
      @Override public void onBonded(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onBonded, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onBonded(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when the bond state has changed from {@link in BluetoothDevice#BOND_BONDING} to
           * {@link in BluetoothDevice#BOND_NONE}.
           *
           * @param device the device that failed to bond.
           */
      @Override public void onBondingFailed(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onBondingFailed, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onBondingFailed(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when a BLE error has occurred
           *
           * @param message   the error message.
           * @param errorCode the error code.
           * @param device    the device that caused an error.
           */
      @Override public void onError(android.bluetooth.BluetoothDevice device, java.lang.String message, int errorCode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(message);
          _data.writeInt(errorCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onError, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onError(device, message, errorCode);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Called when service discovery has finished but the main services were not found on the device.
           *
           * @param device the device that failed to connect due to lack of required services.
           */
      @Override public void onDeviceNotSupported(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDeviceNotSupported, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDeviceNotSupported(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onDataReceived(android.bluetooth.BluetoothDevice device, java.lang.String data) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(data);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDataReceived, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDataReceived(device, data);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onDataSent(android.bluetooth.BluetoothDevice device, java.lang.String data) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(data);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDataSent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDataSent(device, data);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
           * Callback received each time the Battery Level value was read or has changed using
           * a notification.
           *
           * @param device       the target device.
           * @param batteryLevel the battery value in percent.
           */
      @Override public void onBatteryLevelChanged(android.bluetooth.BluetoothDevice device, int batteryLevel) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(batteryLevel);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onBatteryLevelChanged, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onBatteryLevelChanged(device, batteryLevel);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onDevInformationRead(android.bluetooth.BluetoothDevice device, java.lang.String manufacturer, java.lang.String model, java.lang.String serialNumber, java.lang.String hardware, java.lang.String firmware, java.lang.String software) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(manufacturer);
          _data.writeString(model);
          _data.writeString(serialNumber);
          _data.writeString(hardware);
          _data.writeString(firmware);
          _data.writeString(software);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDevInformationRead, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onDevInformationRead(device, manufacturer, model, serialNumber, hardware, firmware, software);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onKeyEvent(android.bluetooth.BluetoothDevice device, int event) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(event);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onKeyEvent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onKeyEvent(device, event);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onFoundDevice(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFoundDevice, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onFoundDevice(device);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.example.devmanagerdemo.IDevAidlCallbacks sDefaultImpl;
    }
    static final int TRANSACTION_onDeviceConnecting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onDeviceConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onDeviceDisconnecting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onDeviceDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_onLinkLossOccurred = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_onServicesDiscovered = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_onDeviceReady = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_onBondingRequired = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_onBonded = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_onBondingFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_onError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_onDeviceNotSupported = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_onDataReceived = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_onDataSent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_onBatteryLevelChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_onDevInformationRead = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_onKeyEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_onFoundDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    public static boolean setDefaultImpl(com.example.devmanagerdemo.IDevAidlCallbacks impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.example.devmanagerdemo.IDevAidlCallbacks getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  /**
       * Called when the Android device started connecting to given device.
       * The {@link #onDeviceConnected(in BluetoothDevice)} will be called when the device is connected,
       * or {@link #onError(in BluetoothDevice, String, int)} in case of error.
       *
       * @param device the device that got connected.
       */
  public void onDeviceConnecting(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when the device has been connected. This does not mean that the application may start
       * communication.
       * A service discovery will be handled automatically after this call. Service discovery
       * may ends up with calling {@link #onServicesDiscovered(in BluetoothDevice, boolean)} or
       * {@link #onDeviceNotSupported(in BluetoothDevice)} if required services have not been found.
       *
       * @param device the device that got connected.
       */
  public void onDeviceConnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when user initialized disconnection.
       *
       * @param device the device that gets disconnecting.
       */
  public void onDeviceDisconnecting(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when the device has disconnected (when the callback returned
       * {@link BluetoothGattCallback#onConnectionStateChange(BluetoothGatt, int, int)} with state
       * DISCONNECTED), but ONLY if the {@link BleManager#shouldAutoConnect()} method returned false
       * for this device when it was connecting.
       * Otherwise the {@link #onLinkLossOccurred(in BluetoothDevice)} method will be called instead.
       *
       * @param device the device that got disconnected.
       */
  public void onDeviceDisconnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * This callback is invoked when the Ble Manager lost connection to a device that has been
       * connected with autoConnect option (see {@link BleManager#shouldAutoConnect()}.
       * Otherwise a {@link #onDeviceDisconnected(in BluetoothDevice)} method will be called on such
       * event.
       *
       * @param device the device that got disconnected due to a link loss.
       */
  public void onLinkLossOccurred(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
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
  public void onServicesDiscovered(android.bluetooth.BluetoothDevice device, boolean optionalServicesFound) throws android.os.RemoteException;
  /**
       * Method called when all initialization requests has been completed.
       *
       * @param device the device that get ready.
       */
  public void onDeviceReady(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when an {@link BluetoothGatt#GATT_INSUFFICIENT_AUTHENTICATION} error occurred and the
       * device bond state is {@link in BluetoothDevice#BOND_NONE}.
       *
       * @param device the device that requires bonding.
       */
  public void onBondingRequired(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when the device has been successfully bonded.
       *
       * @param device the device that got bonded.
       */
  public void onBonded(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when the bond state has changed from {@link in BluetoothDevice#BOND_BONDING} to
       * {@link in BluetoothDevice#BOND_NONE}.
       *
       * @param device the device that failed to bond.
       */
  public void onBondingFailed(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Called when a BLE error has occurred
       *
       * @param message   the error message.
       * @param errorCode the error code.
       * @param device    the device that caused an error.
       */
  public void onError(android.bluetooth.BluetoothDevice device, java.lang.String message, int errorCode) throws android.os.RemoteException;
  /**
       * Called when service discovery has finished but the main services were not found on the device.
       *
       * @param device the device that failed to connect due to lack of required services.
       */
  public void onDeviceNotSupported(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  public void onDataReceived(android.bluetooth.BluetoothDevice device, java.lang.String data) throws android.os.RemoteException;
  public void onDataSent(android.bluetooth.BluetoothDevice device, java.lang.String data) throws android.os.RemoteException;
  /**
       * Callback received each time the Battery Level value was read or has changed using
       * a notification.
       *
       * @param device       the target device.
       * @param batteryLevel the battery value in percent.
       */
  public void onBatteryLevelChanged(android.bluetooth.BluetoothDevice device, int batteryLevel) throws android.os.RemoteException;
  public void onDevInformationRead(android.bluetooth.BluetoothDevice device, java.lang.String manufacturer, java.lang.String model, java.lang.String serialNumber, java.lang.String hardware, java.lang.String firmware, java.lang.String software) throws android.os.RemoteException;
  public void onKeyEvent(android.bluetooth.BluetoothDevice device, int event) throws android.os.RemoteException;
  public void onFoundDevice(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
}
