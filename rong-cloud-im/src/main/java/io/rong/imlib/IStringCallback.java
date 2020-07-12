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

public interface IStringCallback extends IInterface {
    void onComplete(String var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IStringCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IStringCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IStringCallback");
        }

        public static IStringCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IStringCallback");
                return (IStringCallback)(iin != null && iin instanceof IStringCallback ? (IStringCallback)iin : new IStringCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IStringCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
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

        public static boolean setDefaultImpl(IStringCallback impl) {
            if (IStringCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IStringCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IStringCallback getDefaultImpl() {
            return IStringCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IStringCallback {
            private IBinder mRemote;
            public static IStringCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IStringCallback";
            }

            public void onComplete(String string) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IStringCallback");
                    _data.writeString(string);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IStringCallback.Stub.getDefaultImpl() != null) {
                        IStringCallback.Stub.getDefaultImpl().onComplete(string);
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
                    _data.writeInterfaceToken("io.rong.imlib.IStringCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IStringCallback.Stub.getDefaultImpl() != null) {
                        IStringCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements IStringCallback {
        public Default() {
        }

        public void onComplete(String string) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
