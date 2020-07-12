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

public interface IDownloadMediaFileCallback extends IInterface {
    void onFileNameChanged(String var1) throws RemoteException;

    void onComplete() throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    void onProgress(int var1) throws RemoteException;

    void onCanceled() throws RemoteException;

    public abstract static class Stub extends Binder implements IDownloadMediaFileCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IDownloadMediaFileCallback";
        static final int TRANSACTION_onFileNameChanged = 1;
        static final int TRANSACTION_onComplete = 2;
        static final int TRANSACTION_onFailure = 3;
        static final int TRANSACTION_onProgress = 4;
        static final int TRANSACTION_onCanceled = 5;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IDownloadMediaFileCallback");
        }

        public static IDownloadMediaFileCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IDownloadMediaFileCallback");
                return (IDownloadMediaFileCallback)(iin != null && iin instanceof IDownloadMediaFileCallback ? (IDownloadMediaFileCallback)iin : new IDownloadMediaFileCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IDownloadMediaFileCallback";
            int _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _1arg0 = data.readString();
                    this.onFileNameChanged(_1arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    this.onComplete();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    this.onFailure(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    this.onProgress(_arg0);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(descriptor);
                    this.onCanceled();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IDownloadMediaFileCallback impl) {
            if (IDownloadMediaFileCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IDownloadMediaFileCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IDownloadMediaFileCallback getDefaultImpl() {
            return IDownloadMediaFileCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IDownloadMediaFileCallback {
            private IBinder mRemote;
            public static IDownloadMediaFileCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IDownloadMediaFileCallback";
            }

            public void onFileNameChanged(String newFileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaFileCallback");
                    _data.writeString(newFileName);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IDownloadMediaFileCallback.Stub.getDefaultImpl() != null) {
                        IDownloadMediaFileCallback.Stub.getDefaultImpl().onFileNameChanged(newFileName);
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
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaFileCallback");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IDownloadMediaFileCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IDownloadMediaFileCallback.Stub.getDefaultImpl().onComplete();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaFileCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (_status || IDownloadMediaFileCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IDownloadMediaFileCallback.Stub.getDefaultImpl().onFailure(errorCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onProgress(int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaFileCallback");
                    _data.writeInt(progress);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && IDownloadMediaFileCallback.Stub.getDefaultImpl() != null) {
                        IDownloadMediaFileCallback.Stub.getDefaultImpl().onProgress(progress);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onCanceled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaFileCallback");
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (_status || IDownloadMediaFileCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IDownloadMediaFileCallback.Stub.getDefaultImpl().onCanceled();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IDownloadMediaFileCallback {
        public Default() {
        }

        public void onFileNameChanged(String newFileName) throws RemoteException {
        }

        public void onComplete() throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public void onProgress(int progress) throws RemoteException {
        }

        public void onCanceled() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
