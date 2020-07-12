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

public interface ISetUserStatusCallback extends IInterface {
    void onComplete() throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements ISetUserStatusCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.ISetUserStatusCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ISetUserStatusCallback");
        }

        public static ISetUserStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ISetUserStatusCallback");
                return (ISetUserStatusCallback)(iin != null && iin instanceof ISetUserStatusCallback ? (ISetUserStatusCallback)iin : new ISetUserStatusCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ISetUserStatusCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    this.onComplete();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    this.onFailure(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(ISetUserStatusCallback impl) {
            if (ISetUserStatusCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ISetUserStatusCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ISetUserStatusCallback getDefaultImpl() {
            return ISetUserStatusCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ISetUserStatusCallback {
            private IBinder mRemote;
            public static ISetUserStatusCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ISetUserStatusCallback";
            }

            public void onComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISetUserStatusCallback");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || ISetUserStatusCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ISetUserStatusCallback.Stub.getDefaultImpl().onComplete();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISetUserStatusCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || ISetUserStatusCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ISetUserStatusCallback.Stub.getDefaultImpl().onFailure(errorCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements ISetUserStatusCallback {
        public Default() {
        }

        public void onComplete() throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
