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

public interface PushNotificationListener extends IInterface {
    void OnPushNotificationChanged(long var1) throws RemoteException;

    public abstract static class Stub extends Binder implements PushNotificationListener {
        private static final String DESCRIPTOR = "io.rong.imlib.PushNotificationListener";
        static final int TRANSACTION_OnPushNotificationChanged = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.PushNotificationListener");
        }

        public static PushNotificationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.PushNotificationListener");
                return (PushNotificationListener)(iin != null && iin instanceof PushNotificationListener ? (PushNotificationListener)iin : new PushNotificationListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.PushNotificationListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    long _arg0 = data.readLong();
                    this.OnPushNotificationChanged(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(PushNotificationListener impl) {
            if (PushNotificationListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                PushNotificationListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static PushNotificationListener getDefaultImpl() {
            return PushNotificationListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements PushNotificationListener {
            private IBinder mRemote;
            public static PushNotificationListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.PushNotificationListener";
            }

            public void OnPushNotificationChanged(long version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.PushNotificationListener");
                    _data.writeLong(version);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || PushNotificationListener.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    PushNotificationListener.Stub.getDefaultImpl().OnPushNotificationChanged(version);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements PushNotificationListener {
        public Default() {
        }

        public void OnPushNotificationChanged(long version) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
