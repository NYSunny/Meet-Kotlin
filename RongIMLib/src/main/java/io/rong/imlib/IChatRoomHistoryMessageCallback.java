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

public interface IChatRoomHistoryMessageCallback extends IInterface {
    void onComplete(RemoteModelWrap var1, long var2) throws RemoteException;

    void onFailure(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IChatRoomHistoryMessageCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IChatRoomHistoryMessageCallback";
        static final int TRANSACTION_onComplete = 1;
        static final int TRANSACTION_onFailure = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IChatRoomHistoryMessageCallback");
        }

        public static IChatRoomHistoryMessageCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IChatRoomHistoryMessageCallback");
                return (IChatRoomHistoryMessageCallback)(iin != null && iin instanceof IChatRoomHistoryMessageCallback ? (IChatRoomHistoryMessageCallback)iin : new IChatRoomHistoryMessageCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IChatRoomHistoryMessageCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    RemoteModelWrap _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (RemoteModelWrap)RemoteModelWrap.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    long _arg1 = data.readLong();
                    this.onComplete(_arg0, _arg1);
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

        public static boolean setDefaultImpl(IChatRoomHistoryMessageCallback impl) {
            if (IChatRoomHistoryMessageCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IChatRoomHistoryMessageCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IChatRoomHistoryMessageCallback getDefaultImpl() {
            return IChatRoomHistoryMessageCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IChatRoomHistoryMessageCallback {
            private IBinder mRemote;
            public static IChatRoomHistoryMessageCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IChatRoomHistoryMessageCallback";
            }

            public void onComplete(RemoteModelWrap model, long syncTime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IChatRoomHistoryMessageCallback");
                    if (model != null) {
                        _data.writeInt(1);
                        model.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeLong(syncTime);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IChatRoomHistoryMessageCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IChatRoomHistoryMessageCallback.Stub.getDefaultImpl().onComplete(model, syncTime);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFailure(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IChatRoomHistoryMessageCallback");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IChatRoomHistoryMessageCallback.Stub.getDefaultImpl() != null) {
                        IChatRoomHistoryMessageCallback.Stub.getDefaultImpl().onFailure(errorCode);
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

    public static class Default implements IChatRoomHistoryMessageCallback {
        public Default() {
        }

        public void onComplete(RemoteModelWrap model, long syncTime) throws RemoteException {
        }

        public void onFailure(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
