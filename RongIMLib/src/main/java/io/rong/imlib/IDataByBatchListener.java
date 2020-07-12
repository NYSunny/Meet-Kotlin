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

public interface IDataByBatchListener extends IInterface {
    void onProgress(Map var1) throws RemoteException;

    void onComplete() throws RemoteException;

    void onError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IDataByBatchListener {
        private static final String DESCRIPTOR = "io.rong.imlib.IDataByBatchListener";
        static final int TRANSACTION_onProgress = 1;
        static final int TRANSACTION_onComplete = 2;
        static final int TRANSACTION_onError = 3;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IDataByBatchListener");
        }

        public static IDataByBatchListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IDataByBatchListener");
                return (IDataByBatchListener)(iin != null && iin instanceof IDataByBatchListener ? (IDataByBatchListener)iin : new IDataByBatchListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IDataByBatchListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    ClassLoader cl = this.getClass().getClassLoader();
                    Map _arg0 = data.readHashMap(cl);
                    this.onProgress(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    this.onComplete();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    int _3arg0 = data.readInt();
                    this.onError(_3arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IDataByBatchListener impl) {
            if (IDataByBatchListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IDataByBatchListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IDataByBatchListener getDefaultImpl() {
            return IDataByBatchListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IDataByBatchListener {
            private IBinder mRemote;
            public static IDataByBatchListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IDataByBatchListener";
            }

            public void onProgress(Map data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDataByBatchListener");
                    _data.writeMap(data);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IDataByBatchListener.Stub.getDefaultImpl() != null) {
                        IDataByBatchListener.Stub.getDefaultImpl().onProgress(data);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDataByBatchListener");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IDataByBatchListener.Stub.getDefaultImpl() != null) {
                        IDataByBatchListener.Stub.getDefaultImpl().onComplete();
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDataByBatchListener");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && IDataByBatchListener.Stub.getDefaultImpl() != null) {
                        IDataByBatchListener.Stub.getDefaultImpl().onError(status);
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

    public static class Default implements IDataByBatchListener {
        public Default() {
        }

        public void onProgress(Map data) throws RemoteException {
        }

        public void onComplete() throws RemoteException {
        }

        public void onError(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
