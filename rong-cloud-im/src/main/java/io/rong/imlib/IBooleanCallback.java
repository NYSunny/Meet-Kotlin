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

public interface IBooleanCallback extends IInterface {
    void onComplete(boolean var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IBooleanCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IBooleanCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IBooleanCallback");
        }

        public static IBooleanCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IBooleanCallback");
                return (IBooleanCallback)(iin != null && iin instanceof IBooleanCallback ? (IBooleanCallback)iin : new IBooleanCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IBooleanCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
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

        public static boolean setDefaultImpl(IBooleanCallback impl) {
            if (IBooleanCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IBooleanCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IBooleanCallback getDefaultImpl() {
            return IBooleanCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IBooleanCallback {
            private IBinder mRemote;
            public static IBooleanCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IBooleanCallback";
            }

            public void onComplete(boolean result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IBooleanCallback");
                    _data.writeInt(result ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IBooleanCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IBooleanCallback.Stub.getDefaultImpl().onComplete(result);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IBooleanCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IBooleanCallback.Stub.getDefaultImpl() != null) {
                        IBooleanCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements IBooleanCallback {
        public Default() {
        }

        public void onComplete(boolean result) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
