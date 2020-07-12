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

public interface IRealTimeLocationListener extends IInterface {
    void onStatusChange(int var1) throws RemoteException;

    void onReceiveLocation(double var1, double var3, String var5) throws RemoteException;

    void onParticipantsJoin(String var1) throws RemoteException;

    void onParticipantsQuit(String var1) throws RemoteException;

    void onError(int var1) throws RemoteException;

    public abstract static class Stub extends Binder implements IRealTimeLocationListener {
        private static final String DESCRIPTOR = "io.rong.imlib.IRealTimeLocationListener";
        static final int TRANSACTION_onStatusChange = 1;
        static final int TRANSACTION_onReceiveLocation = 2;
        static final int TRANSACTION_onParticipantsJoin = 3;
        static final int TRANSACTION_onParticipantsQuit = 4;
        static final int TRANSACTION_onError = 5;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IRealTimeLocationListener");
        }

        public static IRealTimeLocationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IRealTimeLocationListener");
                return (IRealTimeLocationListener)(iin != null && iin instanceof IRealTimeLocationListener ? (IRealTimeLocationListener)iin : new IRealTimeLocationListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IRealTimeLocationListener";
            String _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    int _1arg0 = data.readInt();
                    this.onStatusChange(_1arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    double _2arg0 = data.readDouble();
                    double _arg1 = data.readDouble();
                    String _arg2 = data.readString();
                    this.onReceiveLocation(_2arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readString();
                    this.onParticipantsJoin(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(descriptor);
                    _arg0 = data.readString();
                    this.onParticipantsQuit(_arg0);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(descriptor);
                    int _5arg0 = data.readInt();
                    this.onError(_5arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IRealTimeLocationListener impl) {
            if (IRealTimeLocationListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IRealTimeLocationListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IRealTimeLocationListener getDefaultImpl() {
            return IRealTimeLocationListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IRealTimeLocationListener {
            private IBinder mRemote;
            public static IRealTimeLocationListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IRealTimeLocationListener";
            }

            public void onStatusChange(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRealTimeLocationListener");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IRealTimeLocationListener.Stub.getDefaultImpl() != null) {
                        IRealTimeLocationListener.Stub.getDefaultImpl().onStatusChange(status);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onReceiveLocation(double latitude, double longitude, String userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRealTimeLocationListener");
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeString(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && IRealTimeLocationListener.Stub.getDefaultImpl() != null) {
                        IRealTimeLocationListener.Stub.getDefaultImpl().onReceiveLocation(latitude, longitude, userId);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onParticipantsJoin(String userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRealTimeLocationListener");
                    _data.writeString(userId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && IRealTimeLocationListener.Stub.getDefaultImpl() != null) {
                        IRealTimeLocationListener.Stub.getDefaultImpl().onParticipantsJoin(userId);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onParticipantsQuit(String userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRealTimeLocationListener");
                    _data.writeString(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (_status || IRealTimeLocationListener.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRealTimeLocationListener.Stub.getDefaultImpl().onParticipantsQuit(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onError(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRealTimeLocationListener");
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && IRealTimeLocationListener.Stub.getDefaultImpl() != null) {
                        IRealTimeLocationListener.Stub.getDefaultImpl().onError(errorCode);
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

    public static class Default implements IRealTimeLocationListener {
        public Default() {
        }

        public void onStatusChange(int status) throws RemoteException {
        }

        public void onReceiveLocation(double latitude, double longitude, String userId) throws RemoteException {
        }

        public void onParticipantsJoin(String userId) throws RemoteException {
        }

        public void onParticipantsQuit(String userId) throws RemoteException {
        }

        public void onError(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
