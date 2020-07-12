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

public interface ISetPushSettingCallback extends IInterface {
    void onComplete() throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements ISetPushSettingCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.ISetPushSettingCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ISetPushSettingCallback");
        }

        public static ISetPushSettingCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ISetPushSettingCallback");
                return (ISetPushSettingCallback)(iin != null && iin instanceof ISetPushSettingCallback ? (ISetPushSettingCallback)iin : new ISetPushSettingCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ISetPushSettingCallback";
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

        public static boolean setDefaultImpl(ISetPushSettingCallback impl) {
            if (ISetPushSettingCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ISetPushSettingCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ISetPushSettingCallback getDefaultImpl() {
            return ISetPushSettingCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ISetPushSettingCallback {
            private IBinder mRemote;
            public static ISetPushSettingCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ISetPushSettingCallback";
            }

            public void onComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISetPushSettingCallback");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && ISetPushSettingCallback.Stub.getDefaultImpl() != null) {
                        ISetPushSettingCallback.Stub.getDefaultImpl().onComplete();
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
                    _data.writeInterfaceToken("io.rong.imlib.ISetPushSettingCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && ISetPushSettingCallback.Stub.getDefaultImpl() != null) {
                        ISetPushSettingCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements ISetPushSettingCallback {
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
