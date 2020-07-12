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

public interface IIntegerCallback extends IInterface {
    void onComplete(int var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IIntegerCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IIntegerCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IIntegerCallback");
        }

        public static IIntegerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IIntegerCallback");
                return (IIntegerCallback)(iin != null && iin instanceof IIntegerCallback ? (IIntegerCallback)iin : new IIntegerCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IIntegerCallback";
            int _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    this.onComplete(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
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

        public static boolean setDefaultImpl(IIntegerCallback impl) {
            if (IIntegerCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IIntegerCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IIntegerCallback getDefaultImpl() {
            return IIntegerCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IIntegerCallback {
            private IBinder mRemote;
            public static IIntegerCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IIntegerCallback";
            }

            public void onComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IIntegerCallback");
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IIntegerCallback.Stub.getDefaultImpl() != null) {
                        IIntegerCallback.Stub.getDefaultImpl().onComplete(result);
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
                    _data.writeInterfaceToken("io.rong.imlib.IIntegerCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IIntegerCallback.Stub.getDefaultImpl() != null) {
                        IIntegerCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements IIntegerCallback {
        public Default() {
        }

        public void onComplete(int result) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
