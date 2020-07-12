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

public interface IRTCConfigCallback extends IInterface {
    void onSuccess(String var1, long var2) throws RemoteException;

    void onError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IRTCConfigCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IRTCConfigCallback";
        static final int TRANSACTION_onSuccess = 1;
        static final int TRANSACTION_onError = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IRTCConfigCallback");
        }

        public static IRTCConfigCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IRTCConfigCallback");
                return (IRTCConfigCallback)(iin != null && iin instanceof IRTCConfigCallback ? (IRTCConfigCallback)iin : new IRTCConfigCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IRTCConfigCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    long _arg1 = data.readLong();
                    this.onSuccess(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.onError(_2arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IRTCConfigCallback impl) {
            if (IRTCConfigCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IRTCConfigCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IRTCConfigCallback getDefaultImpl() {
            return IRTCConfigCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IRTCConfigCallback {
            private IBinder mRemote;
            public static IRTCConfigCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IRTCConfigCallback";
            }

            public void onSuccess(String config, long version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRTCConfigCallback");
                    _data.writeString(config);
                    _data.writeLong(version);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IRTCConfigCallback.Stub.getDefaultImpl() != null) {
                        IRTCConfigCallback.Stub.getDefaultImpl().onSuccess(config, version);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRTCConfigCallback");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IRTCConfigCallback.Stub.getDefaultImpl() != null) {
                        IRTCConfigCallback.Stub.getDefaultImpl().onError(status);
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

    public static class Default implements IRTCConfigCallback {
        public Default() {
        }

        public void onSuccess(String config, long version) throws RemoteException {
        }

        public void onError(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
