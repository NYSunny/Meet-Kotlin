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
import io.rong.imlib.model.RemoteModelWrap;

public interface IResultCallback extends IInterface {
    void onComplete(RemoteModelWrap var1) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IResultCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IResultCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IResultCallback");
        }

        public static IResultCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IResultCallback");
                return (IResultCallback)(iin != null && iin instanceof IResultCallback ? (IResultCallback)iin : new IResultCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IResultCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    RemoteModelWrap _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (RemoteModelWrap)RemoteModelWrap.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    this.onComplete(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.onFailure(_2arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IResultCallback impl) {
            if (IResultCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IResultCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IResultCallback getDefaultImpl() {
            return IResultCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IResultCallback {
            private IBinder mRemote;
            public static IResultCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IResultCallback";
            }

            public void onComplete(RemoteModelWrap model) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IResultCallback");
                    if (model != null) {
                        _data.writeInt(1);
                        model.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IResultCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IResultCallback.Stub.getDefaultImpl().onComplete(model);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IResultCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IResultCallback.Stub.getDefaultImpl() != null) {
                        IResultCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements IResultCallback {
        public Default() {
        }

        public void onComplete(RemoteModelWrap model) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
