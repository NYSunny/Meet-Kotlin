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

public interface IGetUserStatusCallback extends IInterface {
    void onComplete(String var1, int var2) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IGetUserStatusCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IGetUserStatusCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IGetUserStatusCallback");
        }

        public static IGetUserStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IGetUserStatusCallback");
                return (IGetUserStatusCallback)(iin != null && iin instanceof IGetUserStatusCallback ? (IGetUserStatusCallback)iin : new IGetUserStatusCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IGetUserStatusCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.onComplete(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.onFailure(_2arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IGetUserStatusCallback impl) {
            if (IGetUserStatusCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IGetUserStatusCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IGetUserStatusCallback getDefaultImpl() {
            return IGetUserStatusCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IGetUserStatusCallback {
            private IBinder mRemote;
            public static IGetUserStatusCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IGetUserStatusCallback";
            }

            public void onComplete(String platformInfo, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IGetUserStatusCallback");
                    _data.writeString(platformInfo);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IGetUserStatusCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IGetUserStatusCallback.Stub.getDefaultImpl().onComplete(platformInfo, status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IGetUserStatusCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IGetUserStatusCallback.Stub.getDefaultImpl() != null) {
                        IGetUserStatusCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements IGetUserStatusCallback {
        public Default() {
        }

        public void onComplete(String platformInfo, int status) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
