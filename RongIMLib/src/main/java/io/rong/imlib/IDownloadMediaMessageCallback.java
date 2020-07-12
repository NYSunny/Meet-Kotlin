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
import io.rong.imlib.model.Message;

public interface IDownloadMediaMessageCallback extends IInterface {
    void onComplete(Message var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    void onProgress(int var1) throws RemoteException;

    void onCanceled() throws RemoteException;

    public abstract static class Stub extends Binder implements IDownloadMediaMessageCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IDownloadMediaMessageCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;
        static final int TRANSACTION_onProgress = 3;
        static final int TRANSACTION_onCanceled = 4;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IDownloadMediaMessageCallback");
        }

        public static IDownloadMediaMessageCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IDownloadMediaMessageCallback");
                return (IDownloadMediaMessageCallback)(iin != null && iin instanceof IDownloadMediaMessageCallback ? (IDownloadMediaMessageCallback)iin : new IDownloadMediaMessageCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IDownloadMediaMessageCallback";
            int _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    Message _1arg0;
                    if (0 != data.readInt()) {
                        _1arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _1arg0 = null;
                    }

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
                case 4:
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

        public static boolean setDefaultImpl(IDownloadMediaMessageCallback impl) {
            if (IDownloadMediaMessageCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IDownloadMediaMessageCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IDownloadMediaMessageCallback getDefaultImpl() {
            return IDownloadMediaMessageCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IDownloadMediaMessageCallback {
            private IBinder mRemote;
            public static IDownloadMediaMessageCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IDownloadMediaMessageCallback";
            }

            public void onComplete(Message messag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaMessageCallback");
                    if (messag != null) {
                        _data.writeInt(1);
                        messag.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IDownloadMediaMessageCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IDownloadMediaMessageCallback.Stub.getDefaultImpl().onComplete(messag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaMessageCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IDownloadMediaMessageCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IDownloadMediaMessageCallback.Stub.getDefaultImpl().onFailure(errorCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onProgress(int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaMessageCallback");
                    _data.writeInt(progress);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && IDownloadMediaMessageCallback.Stub.getDefaultImpl() != null) {
                        IDownloadMediaMessageCallback.Stub.getDefaultImpl().onProgress(progress);
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
                    _data.writeInterfaceToken("io.rong.imlib.IDownloadMediaMessageCallback");
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (_status || IDownloadMediaMessageCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IDownloadMediaMessageCallback.Stub.getDefaultImpl().onCanceled();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IDownloadMediaMessageCallback {
        public Default() {
        }

        public void onComplete(Message messag) throws RemoteException {
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
