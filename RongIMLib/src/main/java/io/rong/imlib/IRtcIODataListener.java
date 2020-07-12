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
import java.util.Map;

public interface IRtcIODataListener extends IInterface {
    void OnSuccess(Map var1) throws RemoteException;

    void OnError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IRtcIODataListener {
        private static final String DESCRIPTOR = "io.rong.imlib.IRtcIODataListener";
        static final int TRANSACTION_OnSuccess = 1;
        static final int TRANSACTION_OnError = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IRtcIODataListener");
        }

        public static IRtcIODataListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IRtcIODataListener");
                return (IRtcIODataListener)(iin != null && iin instanceof IRtcIODataListener ? (IRtcIODataListener)iin : new IRtcIODataListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IRtcIODataListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    ClassLoader cl = this.getClass().getClassLoader();
                    Map _arg0 = data.readHashMap(cl);
                    this.OnSuccess(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.OnError(_2arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IRtcIODataListener impl) {
            if (IRtcIODataListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IRtcIODataListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IRtcIODataListener getDefaultImpl() {
            return IRtcIODataListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IRtcIODataListener {
            private IBinder mRemote;
            public static IRtcIODataListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IRtcIODataListener";
            }

            public void OnSuccess(Map data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRtcIODataListener");
                    _data.writeMap(data);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IRtcIODataListener.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRtcIODataListener.Stub.getDefaultImpl().OnSuccess(data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void OnError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRtcIODataListener");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IRtcIODataListener.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRtcIODataListener.Stub.getDefaultImpl().OnError(status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IRtcIODataListener {
        public Default() {
        }

        public void OnSuccess(Map data) throws RemoteException {
        }

        public void OnError(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
