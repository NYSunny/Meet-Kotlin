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

public interface IConnectionStatusListener extends IInterface {
    void onChanged(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IConnectionStatusListener {
        private static final String DESCRIPTOR = "io.rong.imlib.IConnectionStatusListener";
        static final int TRANSACTION_onChanged = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IConnectionStatusListener");
        }

        public static IConnectionStatusListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IConnectionStatusListener");
                return (IConnectionStatusListener)(iin != null && iin instanceof IConnectionStatusListener ? (IConnectionStatusListener)iin : new IConnectionStatusListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IConnectionStatusListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    this.onChanged(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IConnectionStatusListener impl) {
            if (IConnectionStatusListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IConnectionStatusListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IConnectionStatusListener getDefaultImpl() {
            return IConnectionStatusListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IConnectionStatusListener {
            private IBinder mRemote;
            public static IConnectionStatusListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IConnectionStatusListener";
            }

            public void onChanged(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IConnectionStatusListener");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IConnectionStatusListener.Stub.getDefaultImpl() != null) {
                        IConnectionStatusListener.Stub.getDefaultImpl().onChanged(status);
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

    public static class Default implements IConnectionStatusListener {
        public Default() {
        }

        public void onChanged(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
