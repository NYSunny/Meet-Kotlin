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

public interface IUploadCallback extends IInterface {
    void onComplete(String var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    void onProgress(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IUploadCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IUploadCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;
        static final int TRANSACTION_onProgress = 3;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IUploadCallback");
        }

        public static IUploadCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IUploadCallback");
                return (IUploadCallback)(iin != null && iin instanceof IUploadCallback ? (IUploadCallback)iin : new IUploadCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IUploadCallback";
            int _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _1arg0 = data.readString();
                    this.onComplete(_1arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    this.onFailure(_arg0);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    this.onProgress(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IUploadCallback impl) {
            if (IUploadCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IUploadCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IUploadCallback getDefaultImpl() {
            return IUploadCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IUploadCallback {
            private IBinder mRemote;
            public static IUploadCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IUploadCallback";
            }

            public void onComplete(String url) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IUploadCallback");
                    _data.writeString(url);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IUploadCallback.Stub.getDefaultImpl() != null) {
                        IUploadCallback.Stub.getDefaultImpl().onComplete(url);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IUploadCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IUploadCallback.Stub.getDefaultImpl() != null) {
                        IUploadCallback.Stub.getDefaultImpl().onFailure(errorCode);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onProgress(int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IUploadCallback");
                    _data.writeInt(progress);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (_status || IUploadCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IUploadCallback.Stub.getDefaultImpl().onProgress(progress);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IUploadCallback {
        public Default() {
        }

        public void onComplete(String url) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public void onProgress(int progress) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
