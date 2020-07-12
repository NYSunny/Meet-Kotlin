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

public interface IRTCJoinRoomCallback extends IInterface {
    void OnSuccess(List var1, String var2, String var3) throws RemoteException;

    void OnError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IRTCJoinRoomCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IRTCJoinRoomCallback";
        static final int TRANSACTION_OnSuccess = 1;
        static final int TRANSACTION_OnError = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IRTCJoinRoomCallback");
        }

        public static IRTCJoinRoomCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IRTCJoinRoomCallback");
                return (IRTCJoinRoomCallback)(iin != null && iin instanceof IRTCJoinRoomCallback ? (IRTCJoinRoomCallback)iin : new IRTCJoinRoomCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IRTCJoinRoomCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    ClassLoader cl = this.getClass().getClassLoader();
                    List _arg0 = data.readArrayList(cl);
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    this.OnSuccess(_arg0, _arg1, _arg2);
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

        public static boolean setDefaultImpl(IRTCJoinRoomCallback impl) {
            if (IRTCJoinRoomCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IRTCJoinRoomCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IRTCJoinRoomCallback getDefaultImpl() {
            return IRTCJoinRoomCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IRTCJoinRoomCallback {
            private IBinder mRemote;
            public static IRTCJoinRoomCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IRTCJoinRoomCallback";
            }

            public void OnSuccess(List data, String token, String sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRTCJoinRoomCallback");
                    _data.writeList(data);
                    _data.writeString(token);
                    _data.writeString(sessionId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IRTCJoinRoomCallback.Stub.getDefaultImpl() != null) {
                        IRTCJoinRoomCallback.Stub.getDefaultImpl().OnSuccess(data, token, sessionId);
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
                    _data.writeInterfaceToken("io.rong.imlib.IRTCJoinRoomCallback");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IRTCJoinRoomCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRTCJoinRoomCallback.Stub.getDefaultImpl().OnError(status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IRTCJoinRoomCallback {
        public Default() {
        }

        public void OnSuccess(List data, String token, String sessionId) throws RemoteException {
        }

        public void OnError(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
