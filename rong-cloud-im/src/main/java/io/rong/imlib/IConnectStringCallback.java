//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IConnectStringCallback extends IInterface {
    void onComplete(String var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    void OnDatabaseOpened(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IConnectStringCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IConnectStringCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;
        static final int TRANSACTION_OnDatabaseOpened = 3;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IConnectStringCallback");
        }

        public static IConnectStringCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IConnectStringCallback");
                return (IConnectStringCallback)(iin != null && iin instanceof IConnectStringCallback ? (IConnectStringCallback)iin : new IConnectStringCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IConnectStringCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _1arg0 = data.readString();
                    this.onComplete(_1arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.onFailure(_2arg0);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    int _3arg0 = data.readInt();
                    this.OnDatabaseOpened(_3arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IConnectStringCallback impl) {
            if (IConnectStringCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IConnectStringCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IConnectStringCallback getDefaultImpl() {
            return IConnectStringCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IConnectStringCallback {
            private IBinder mRemote;
            public static IConnectStringCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IConnectStringCallback";
            }

            public void onComplete(String string) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IConnectStringCallback");
                    _data.writeString(string);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IConnectStringCallback.Stub.getDefaultImpl() != null) {
                        IConnectStringCallback.Stub.getDefaultImpl().onComplete(string);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IConnectStringCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IConnectStringCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IConnectStringCallback.Stub.getDefaultImpl().onFailure(errorCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void OnDatabaseOpened(int code) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IConnectStringCallback");
                    _data.writeInt(code);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && IConnectStringCallback.Stub.getDefaultImpl() != null) {
                        IConnectStringCallback.Stub.getDefaultImpl().OnDatabaseOpened(code);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IConnectStringCallback {
        public Default() {
        }

        public void onComplete(String string) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public void OnDatabaseOpened(int code) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
