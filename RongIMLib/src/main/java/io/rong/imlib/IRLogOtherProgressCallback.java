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

public interface IRLogOtherProgressCallback extends IInterface {
    void write(String var1, int var2) throws RemoteException;

    void setLogLevel(int var1) throws RemoteException;

    void uploadRLog() throws RemoteException;

    public abstract static class Stub extends Binder implements IRLogOtherProgressCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IRLogOtherProgressCallback";
        static final int TRANSACTION_write = 1;
        static final int TRANSACTION_setLogLevel = 2;
        static final int TRANSACTION_uploadRLog = 3;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IRLogOtherProgressCallback");
        }

        public static IRLogOtherProgressCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IRLogOtherProgressCallback");
                return (IRLogOtherProgressCallback)(iin != null && iin instanceof IRLogOtherProgressCallback ? (IRLogOtherProgressCallback)iin : new IRLogOtherProgressCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IRLogOtherProgressCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.write(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    int _2arg0 = data.readInt();
                    this.setLogLevel(_2arg0);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    this.uploadRLog();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IRLogOtherProgressCallback impl) {
            if (IRLogOtherProgressCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IRLogOtherProgressCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IRLogOtherProgressCallback getDefaultImpl() {
            return IRLogOtherProgressCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IRLogOtherProgressCallback {
            private IBinder mRemote;
            public static IRLogOtherProgressCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IRLogOtherProgressCallback";
            }

            public void write(String log, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRLogOtherProgressCallback");
                    _data.writeString(log);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IRLogOtherProgressCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRLogOtherProgressCallback.Stub.getDefaultImpl().write(log, level);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setLogLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRLogOtherProgressCallback");
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IRLogOtherProgressCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRLogOtherProgressCallback.Stub.getDefaultImpl().setLogLevel(level);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void uploadRLog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IRLogOtherProgressCallback");
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (_status || IRLogOtherProgressCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IRLogOtherProgressCallback.Stub.getDefaultImpl().uploadRLog();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IRLogOtherProgressCallback {
        public Default() {
        }

        public void write(String log, int level) throws RemoteException {
        }

        public void setLogLevel(int level) throws RemoteException {
        }

        public void uploadRLog() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
