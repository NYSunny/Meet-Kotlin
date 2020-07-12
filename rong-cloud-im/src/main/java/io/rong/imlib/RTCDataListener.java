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

public interface RTCDataListener extends IInterface {
    void OnSuccess(List var1) throws RemoteException;

    void OnError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements RTCDataListener {
        private static final String DESCRIPTOR = "io.rong.imlib.RTCDataListener";
        static final int TRANSACTION_OnSuccess = 1;
        static final int TRANSACTION_OnError = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.RTCDataListener");
        }

        public static RTCDataListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.RTCDataListener");
                return (RTCDataListener)(iin != null && iin instanceof RTCDataListener ? (RTCDataListener)iin : new RTCDataListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.RTCDataListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    ClassLoader cl = this.getClass().getClassLoader();
                    List _arg0 = data.readArrayList(cl);
                    this.OnSuccess(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _arg1 = data.readInt();
                    this.OnError(_arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(RTCDataListener impl) {
            if (RTCDataListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                RTCDataListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static RTCDataListener getDefaultImpl() {
            return RTCDataListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements RTCDataListener {
            private IBinder mRemote;
            public static RTCDataListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.RTCDataListener";
            }

            public void OnSuccess(List data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.RTCDataListener");
                    _data.writeList(data);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && RTCDataListener.Stub.getDefaultImpl() != null) {
                        RTCDataListener.Stub.getDefaultImpl().OnSuccess(data);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void OnError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.RTCDataListener");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && RTCDataListener.Stub.getDefaultImpl() != null) {
                        RTCDataListener.Stub.getDefaultImpl().OnError(status);
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

    public static class Default implements RTCDataListener {
        public Default() {
        }

        public void OnSuccess(List data) throws RemoteException {
        }

        public void OnError(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
