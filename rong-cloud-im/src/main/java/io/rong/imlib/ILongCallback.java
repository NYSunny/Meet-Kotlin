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

public interface ILongCallback extends IInterface {
    void onComplete(long var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements ILongCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.ILongCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ILongCallback");
        }

        public static ILongCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ILongCallback");
                return (ILongCallback)(iin != null && iin instanceof ILongCallback ? (ILongCallback)iin : new ILongCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ILongCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    long _arg0 = data.readLong();
                    this.onComplete(_arg0);
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

        public static boolean setDefaultImpl(ILongCallback impl) {
            if (ILongCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ILongCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ILongCallback getDefaultImpl() {
            return ILongCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ILongCallback {
            private IBinder mRemote;
            public static ILongCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ILongCallback";
            }

            public void onComplete(long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ILongCallback");
                    _data.writeLong(result);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || ILongCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ILongCallback.Stub.getDefaultImpl().onComplete(result);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ILongCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || ILongCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ILongCallback.Stub.getDefaultImpl().onFailure(errorCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements ILongCallback {
        public Default() {
        }

        public void onComplete(long result) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
