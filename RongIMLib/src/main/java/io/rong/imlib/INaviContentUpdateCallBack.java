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

public interface INaviContentUpdateCallBack extends IInterface {
    void naviContentUpdate() throws RemoteException;

    public abstract static class Stub extends Binder implements INaviContentUpdateCallBack {
        private static final String DESCRIPTOR = "io.rong.imlib.INaviContentUpdateCallBack";
        static final int TRANSACTION_naviContentUpdate = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.INaviContentUpdateCallBack");
        }

        public static INaviContentUpdateCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.INaviContentUpdateCallBack");
                return (INaviContentUpdateCallBack)(iin != null && iin instanceof INaviContentUpdateCallBack ? (INaviContentUpdateCallBack)iin : new INaviContentUpdateCallBack.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.INaviContentUpdateCallBack";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    this.naviContentUpdate();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(INaviContentUpdateCallBack impl) {
            if (INaviContentUpdateCallBack.Stub.Proxy.sDefaultImpl == null && impl != null) {
                INaviContentUpdateCallBack.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static INaviContentUpdateCallBack getDefaultImpl() {
            return INaviContentUpdateCallBack.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements INaviContentUpdateCallBack {
            private IBinder mRemote;
            public static INaviContentUpdateCallBack sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.INaviContentUpdateCallBack";
            }

            public void naviContentUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.INaviContentUpdateCallBack");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && INaviContentUpdateCallBack.Stub.getDefaultImpl() != null) {
                        INaviContentUpdateCallBack.Stub.getDefaultImpl().naviContentUpdate();
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

    public static class Default implements INaviContentUpdateCallBack {
        public Default() {
        }

        public void naviContentUpdate() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
