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

public interface ILongSendMessageCallback extends IInterface {
    void onComplete(long var1) throws RemoteException;

    void onFailure(long var1, int var3) throws RemoteException;

    public abstract static class Stub extends Binder implements ILongSendMessageCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.ILongSendMessageCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ILongSendMessageCallback");
        }

        public static ILongSendMessageCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ILongSendMessageCallback");
                return (ILongSendMessageCallback)(iin != null && iin instanceof ILongSendMessageCallback ? (ILongSendMessageCallback)iin : new ILongSendMessageCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ILongSendMessageCallback";
            long _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readLong();
                    this.onComplete(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readLong();
                    int _arg1 = data.readInt();
                    this.onFailure(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(ILongSendMessageCallback impl) {
            if (ILongSendMessageCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ILongSendMessageCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ILongSendMessageCallback getDefaultImpl() {
            return ILongSendMessageCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ILongSendMessageCallback {
            private IBinder mRemote;
            public static ILongSendMessageCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ILongSendMessageCallback";
            }

            public void onComplete(long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ILongSendMessageCallback");
                    _data.writeLong(result);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && ILongSendMessageCallback.Stub.getDefaultImpl() != null) {
                        ILongSendMessageCallback.Stub.getDefaultImpl().onComplete(result);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(long result, int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ILongSendMessageCallback");
                    _data.writeLong(result);
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && ILongSendMessageCallback.Stub.getDefaultImpl() != null) {
                        ILongSendMessageCallback.Stub.getDefaultImpl().onFailure(result, errorCode);
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

    public static class Default implements ILongSendMessageCallback {
        public Default() {
        }

        public void onComplete(long result) throws RemoteException {
        }

        public void onFailure(long result, int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
