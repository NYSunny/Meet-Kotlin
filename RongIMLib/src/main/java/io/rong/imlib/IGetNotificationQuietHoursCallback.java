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

public interface IGetNotificationQuietHoursCallback extends IInterface {
    void onSuccess(String var1, int var2) throws RemoteException;

    void onError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IGetNotificationQuietHoursCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IGetNotificationQuietHoursCallback";
        static final int TRANSACTION_onSuccess = 1;
        static final int TRANSACTION_onError = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IGetNotificationQuietHoursCallback");
        }

        public static IGetNotificationQuietHoursCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IGetNotificationQuietHoursCallback");
                return (IGetNotificationQuietHoursCallback)(iin != null && iin instanceof IGetNotificationQuietHoursCallback ? (IGetNotificationQuietHoursCallback)iin : new IGetNotificationQuietHoursCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IGetNotificationQuietHoursCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
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

        public static boolean setDefaultImpl(IGetNotificationQuietHoursCallback impl) {
            if (IGetNotificationQuietHoursCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IGetNotificationQuietHoursCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IGetNotificationQuietHoursCallback getDefaultImpl() {
            return IGetNotificationQuietHoursCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IGetNotificationQuietHoursCallback {
            private IBinder mRemote;
            public static IGetNotificationQuietHoursCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IGetNotificationQuietHoursCallback";
            }

            public void onSuccess(String startTime, int minutes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IGetNotificationQuietHoursCallback");
                    _data.writeString(startTime);
                    _data.writeInt(minutes);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IGetNotificationQuietHoursCallback.Stub.getDefaultImpl() != null) {
                        IGetNotificationQuietHoursCallback.Stub.getDefaultImpl().onSuccess(startTime, minutes);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onError(int errorcode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IGetNotificationQuietHoursCallback");
                    _data.writeInt(errorcode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IGetNotificationQuietHoursCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IGetNotificationQuietHoursCallback.Stub.getDefaultImpl().onError(errorcode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IGetNotificationQuietHoursCallback {
        public Default() {
        }

        public void onSuccess(String startTime, int minutes) throws RemoteException {
        }

        public void onError(int errorcode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
