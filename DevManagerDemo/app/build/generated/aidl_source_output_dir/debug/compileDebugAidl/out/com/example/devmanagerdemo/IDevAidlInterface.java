/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.example.devmanagerdemo;
public interface IDevAidlInterface extends android.os.IInterface
{
  /** Default implementation for IDevAidlInterface. */
  public static class Default implements com.example.devmanagerdemo.IDevAidlInterface
  {
    // send cmd to ble device

    @Override public void send(android.bluetooth.BluetoothDevice device, byte[] cmd) throws android.os.RemoteException
    {
    }
    //start ble car scanner

    @Override public void startScan() throws android.os.RemoteException
    {
    }
    // Stop scan if user cancel/or search target car/

    @Override public void stopScan() throws android.os.RemoteException
    {
    }
    @Override public void addCallbacks(com.example.devmanagerdemo.IDevAidlCallbacks callbacks) throws android.os.RemoteException
    {
    }
    @Override public void removeCallbacks(com.example.devmanagerdemo.IDevAidlCallbacks callbacks) throws android.os.RemoteException
    {
    }
    /**
         * Returns an unmodifiable list of devices managed by the service.
         * The returned devices do not need to be connected at tha moment. Each of them was however created
         * using {@link #connect(BluetoothDevice)} method so they might have been connected before and disconnected.
         *
         * @return unmodifiable list of devices managed by the service
         */
    @Override public java.util.List<android.bluetooth.BluetoothDevice> getManagedDevices() throws android.os.RemoteException
    {
      return null;
    }
    /**
         * Connects to the given device. If the device is already connected this method does nothing.
         *
         * @param device target Bluetooth device
         */
    @Override public void connect(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Disconnects the given device and removes the associated BleManager object.
         * If the list of BleManagers is empty while the last activity unbinds from the service,
         * the service will stop itself.
         *
         * @param device target device to disconnect and forget
         */
    @Override public void disconnect(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
    }
    /**
         * Returns <code>true</code> if the device is connected to the sensor.
         *
         * @param device the target device
         * @return <code>true</code> if device is connected to the sensor, <code>false</code> otherwise
         */
    @Override public boolean isConnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.example.devmanagerdemo.IDevAidlInterface
  {
    private static final java.lang.String DESCRIPTOR = "com.example.devmanagerdemo.IDevAidlInterface";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.example.devmanagerdemo.IDevAidlInterface interface,
     * generating a proxy if needed.
     */
    public static com.example.devmanagerdemo.IDevAidlInterface asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.example.devmanagerdemo.IDevAidlInterface))) {
        return ((com.example.devmanagerdemo.IDevAidlInterface)iin);
      }
      return new com.example.devmanagerdemo.IDevAidlInterface.Stub.Proxy(obj);
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
        case TRANSACTION_send:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          byte[] _arg1;
          _arg1 = data.createByteArray();
          this.send(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_startScan:
        {
          data.enforceInterface(descriptor);
          this.startScan();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_stopScan:
        {
          data.enforceInterface(descriptor);
          this.stopScan();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_addCallbacks:
        {
          data.enforceInterface(descriptor);
          com.example.devmanagerdemo.IDevAidlCallbacks _arg0;
          _arg0 = com.example.devmanagerdemo.IDevAidlCallbacks.Stub.asInterface(data.readStrongBinder());
          this.addCallbacks(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_removeCallbacks:
        {
          data.enforceInterface(descriptor);
          com.example.devmanagerdemo.IDevAidlCallbacks _arg0;
          _arg0 = com.example.devmanagerdemo.IDevAidlCallbacks.Stub.asInterface(data.readStrongBinder());
          this.removeCallbacks(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getManagedDevices:
        {
          data.enforceInterface(descriptor);
          java.util.List<android.bluetooth.BluetoothDevice> _result = this.getManagedDevices();
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_connect:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.connect(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_disconnect:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.disconnect(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isConnected:
        {
          data.enforceInterface(descriptor);
          android.bluetooth.BluetoothDevice _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.isConnected(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.example.devmanagerdemo.IDevAidlInterface
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
      // send cmd to ble device

      @Override public void send(android.bluetooth.BluetoothDevice device, byte[] cmd) throws android.os.RemoteException
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
          _data.writeByteArray(cmd);
          boolean _status = mRemote.transact(Stub.TRANSACTION_send, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().send(device, cmd);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      //start ble car scanner

      @Override public void startScan() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startScan, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().startScan();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Stop scan if user cancel/or search target car/

      @Override public void stopScan() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopScan, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().stopScan();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addCallbacks(com.example.devmanagerdemo.IDevAidlCallbacks callbacks) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((callbacks!=null))?(callbacks.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_addCallbacks, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addCallbacks(callbacks);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeCallbacks(com.example.devmanagerdemo.IDevAidlCallbacks callbacks) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((callbacks!=null))?(callbacks.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeCallbacks, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().removeCallbacks(callbacks);
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
           * Returns an unmodifiable list of devices managed by the service.
           * The returned devices do not need to be connected at tha moment. Each of them was however created
           * using {@link #connect(BluetoothDevice)} method so they might have been connected before and disconnected.
           *
           * @return unmodifiable list of devices managed by the service
           */
      @Override public java.util.List<android.bluetooth.BluetoothDevice> getManagedDevices() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.bluetooth.BluetoothDevice> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getManagedDevices, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getManagedDevices();
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.bluetooth.BluetoothDevice.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
           * Connects to the given device. If the device is already connected this method does nothing.
           *
           * @param device target Bluetooth device
           */
      @Override public void connect(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
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
          boolean _status = mRemote.transact(Stub.TRANSACTION_connect, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().connect(device);
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
           * Disconnects the given device and removes the associated BleManager object.
           * If the list of BleManagers is empty while the last activity unbinds from the service,
           * the service will stop itself.
           *
           * @param device target device to disconnect and forget
           */
      @Override public void disconnect(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
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
          boolean _status = mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().disconnect(device);
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
           * Returns <code>true</code> if the device is connected to the sensor.
           *
           * @param device the target device
           * @return <code>true</code> if device is connected to the sensor, <code>false</code> otherwise
           */
      @Override public boolean isConnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((device!=null)) {
            _data.writeInt(1);
            device.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_isConnected, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isConnected(device);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static com.example.devmanagerdemo.IDevAidlInterface sDefaultImpl;
    }
    static final int TRANSACTION_send = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_startScan = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_stopScan = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_addCallbacks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_removeCallbacks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getManagedDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_connect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_isConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    public static boolean setDefaultImpl(com.example.devmanagerdemo.IDevAidlInterface impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.example.devmanagerdemo.IDevAidlInterface getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  // send cmd to ble device

  public void send(android.bluetooth.BluetoothDevice device, byte[] cmd) throws android.os.RemoteException;
  //start ble car scanner

  public void startScan() throws android.os.RemoteException;
  // Stop scan if user cancel/or search target car/

  public void stopScan() throws android.os.RemoteException;
  public void addCallbacks(com.example.devmanagerdemo.IDevAidlCallbacks callbacks) throws android.os.RemoteException;
  public void removeCallbacks(com.example.devmanagerdemo.IDevAidlCallbacks callbacks) throws android.os.RemoteException;
  /**
       * Returns an unmodifiable list of devices managed by the service.
       * The returned devices do not need to be connected at tha moment. Each of them was however created
       * using {@link #connect(BluetoothDevice)} method so they might have been connected before and disconnected.
       *
       * @return unmodifiable list of devices managed by the service
       */
  public java.util.List<android.bluetooth.BluetoothDevice> getManagedDevices() throws android.os.RemoteException;
  /**
       * Connects to the given device. If the device is already connected this method does nothing.
       *
       * @param device target Bluetooth device
       */
  public void connect(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Disconnects the given device and removes the associated BleManager object.
       * If the list of BleManagers is empty while the last activity unbinds from the service,
       * the service will stop itself.
       *
       * @param device target device to disconnect and forget
       */
  public void disconnect(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
  /**
       * Returns <code>true</code> if the device is connected to the sensor.
       *
       * @param device the target device
       * @return <code>true</code> if device is connected to the sensor, <code>false</code> otherwise
       */
  public boolean isConnected(android.bluetooth.BluetoothDevice device) throws android.os.RemoteException;
}
