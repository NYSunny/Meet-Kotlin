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

public interface ISubscribeUserStatusCallback extends IInterface {
    void onStatusReceived(String var1, String var2) throws RemoteException;

    public abstract static class Stub extends Binder implements ISubscribeUserStatusCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.ISubscribeUserStatusCallback";
        static final int TRANSACTION_onStatusReceived = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ISubscribeUserStatusCallback");
        }

        public static ISubscribeUserStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ISubscribeUserStatusCallback");
                return (ISubscribeUserStatusCallback)(iin != null && iin instanceof ISubscribeUserStatusCallback ? (ISubscribeUserStatusCallback)iin : new ISubscribeUserStatusCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ISubscribeUserStatusCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    this.onStatusReceived(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(ISubscribeUserStatusCallback impl) {
            if (ISubscribeUserStatusCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ISubscribeUserStatusCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ISubscribeUserStatusCallback getDefaultImpl() {
            return ISubscribeUserStatusCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ISubscribeUserStatusCallback {
            private IBinder mRemote;
            public static ISubscribeUserStatusCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ISubscribeUserStatusCallback";
            }

            public void onStatusReceived(String objName, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISubscribeUserStatusCallback");
                    _data.writeString(objName);
                    _data.writeString(content);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && ISubscribeUserStatusCallback.Stub.getDefaultImpl() != null) {
                        ISubscribeUserStatusCallback.Stub.getDefaultImpl().onStatusReceived(objName, content);
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

    public static class Default implements ISubscribeUserStatusCallback {
        public Default() {
        }

        public void onStatusReceived(String objName, String content) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
