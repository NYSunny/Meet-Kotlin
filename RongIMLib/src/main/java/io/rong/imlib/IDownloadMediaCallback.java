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

public interface IDownloadMediaCallback extends IInterface {
    void onComplete(String var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    void onProgress(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IDownloadMediaCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IDownloadMediaCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;
        static final int TRANSACTION_onProgress = 3;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IDownloadMediaCallback");
        }

        public static IDownloadMediaCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IDownloadMediaCallback");
                return (IDownloadMediaCallback)(iin != null && iin instanceof IDownloadMediaCallback ? (IDownloadMediaCallback)iin : new IDownloadMediaCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IDownloadMediaCallback";
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

        public static boolean setDefaultImpl(IDownloadMediaCallback impl) {
            if (IDownloadMediaCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IDownloadMediaCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IDownloadMediaCallback getDefaultImpl() {
            return IDownloadMediaCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IDownloadMediaCallback {
            private IBinder mRemote;
            public static IDownloadMediaCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IDownloadMediaCallback";
            }

            public void onComplete(String url) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaCallback");
                    _data.writeString(url);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IDownloadMediaCallback.Stub.getDefaultImpl() != null) {
                        IDownloadMediaCallback.Stub.getDefaultImpl().onComplete(url);
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
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IDownloadMediaCallback.Stub.getDefaultImpl() != null) {
                        IDownloadMediaCallback.Stub.getDefaultImpl().onFailure(errorCode);
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
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaCallback");
                    _data.writeInt(progress);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && IDownloadMediaCallback.Stub.getDefaultImpl() != null) {
                        IDownloadMediaCallback.Stub.getDefaultImpl().onProgress(progress);
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

    public static class Default implements IDownloadMediaCallback {
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
