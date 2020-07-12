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

public interface ISendMediaMessageCallback extends IInterface {
    void onAttached(Message var1) throws RemoteException;

    void onProgress(Message var1, int var2) throws RemoteException;

    void onSuccess(Message var1) throws RemoteException;

    void onError(Message var1, int var2) throws RemoteException;

    void onCanceled(Message var1) throws RemoteException;

    public abstract static class Stub extends Binder implements ISendMediaMessageCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.ISendMediaMessageCallback";
        static final int TRANSACTION_onAttached = 1;
        static final int TRANSACTION_onProgress = 2;
        static final int TRANSACTION_onSuccess = 3;
        static final int TRANSACTION_onError = 4;
        static final int TRANSACTION_onCanceled = 5;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ISendMediaMessageCallback");
        }

        public static ISendMediaMessageCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ISendMediaMessageCallback");
                return (ISendMediaMessageCallback)(iin != null && iin instanceof ISendMediaMessageCallback ? (ISendMediaMessageCallback)iin : new ISendMediaMessageCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ISendMediaMessageCallback";
            Message _arg0;
            int _arg1;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    if (0 != data.readInt()) {
                        _arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    this.onAttached(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    if (0 != data.readInt()) {
                        _arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    _arg1 = data.readInt();
                    this.onProgress(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    if (0 != data.readInt()) {
                        _arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    this.onSuccess(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(descriptor);
                    if (0 != data.readInt()) {
                        _arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    _arg1 = data.readInt();
                    this.onError(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(descriptor);
                    if (0 != data.readInt()) {
                        _arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    this.onCanceled(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(ISendMediaMessageCallback impl) {
            if (ISendMediaMessageCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ISendMediaMessageCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ISendMediaMessageCallback getDefaultImpl() {
            return ISendMediaMessageCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ISendMediaMessageCallback {
            private IBinder mRemote;
            public static ISendMediaMessageCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ISendMediaMessageCallback";
            }

            public void onAttached(Message message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISendMediaMessageCallback");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || ISendMediaMessageCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ISendMediaMessageCallback.Stub.getDefaultImpl().onAttached(message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onProgress(Message message, int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISendMediaMessageCallback");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeInt(progress);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && ISendMediaMessageCallback.Stub.getDefaultImpl() != null) {
                        ISendMediaMessageCallback.Stub.getDefaultImpl().onProgress(message, progress);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onSuccess(Message message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISendMediaMessageCallback");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && ISendMediaMessageCallback.Stub.getDefaultImpl() != null) {
                        ISendMediaMessageCallback.Stub.getDefaultImpl().onSuccess(message);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onError(Message message, int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISendMediaMessageCallback");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && ISendMediaMessageCallback.Stub.getDefaultImpl() != null) {
                        ISendMediaMessageCallback.Stub.getDefaultImpl().onError(message, errorCode);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onCanceled(Message message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ISendMediaMessageCallback");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (_status || ISendMediaMessageCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ISendMediaMessageCallback.Stub.getDefaultImpl().onCanceled(message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements ISendMediaMessageCallback {
        public Default() {
        }

        public void onAttached(Message message) throws RemoteException {
        }

        public void onProgress(Message message, int progress) throws RemoteException {
        }

        public void onSuccess(Message message) throws RemoteException {
        }

        public void onError(Message message, int errorCode) throws RemoteException {
        }

        public void onCanceled(Message message) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
