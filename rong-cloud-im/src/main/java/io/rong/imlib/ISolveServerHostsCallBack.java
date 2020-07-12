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
import java.util.List;

public interface ISolveServerHostsCallBack extends IInterface {
    void onSuccess(List<String> var1) throws RemoteException;

    void onFailed(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements ISolveServerHostsCallBack {
        private static final String DESCRIPTOR = "io.rong.imlib.ISolveServerHostsCallBack";
        static final int TRANSACTION_onSuccess = 1;
        static final int TRANSACTION_onFailed = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ISolveServerHostsCallBack");
        }

        public static ISolveServerHostsCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ISolveServerHostsCallBack");
                return (ISolveServerHostsCallBack)(iin != null && iin instanceof ISolveServerHostsCallBack ? (ISolveServerHostsCallBack)iin : new ISolveServerHostsCallBack.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ISolveServerHostsCallBack";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    List<String> _arg0 = data.createStringArrayList();
                    this.onSuccess(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.onFailed(_2arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(ISolveServerHostsCallBack impl) {
            if (ISolveServerHostsCallBack.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ISolveServerHostsCallBack.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ISolveServerHostsCallBack getDefaultImpl() {
            return ISolveServerHostsCallBack.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ISolveServerHostsCallBack {
            private IBinder mRemote;
            public static ISolveServerHostsCallBack sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ISolveServerHostsCallBack";
            }

            public void onSuccess(List<String> hosts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISolveServerHostsCallBack");
                    _data.writeStringList(hosts);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && ISolveServerHostsCallBack.Stub.getDefaultImpl() != null) {
                        ISolveServerHostsCallBack.Stub.getDefaultImpl().onSuccess(hosts);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailed(int code) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISolveServerHostsCallBack");
                    _data.writeInt(code);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || ISolveServerHostsCallBack.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ISolveServerHostsCallBack.Stub.getDefaultImpl().onFailed(code);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements ISolveServerHostsCallBack {
        public Default() {
        }

        public void onSuccess(List<String> hosts) throws RemoteException {
        }

        public void onFailed(int code) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
