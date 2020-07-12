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

public interface OnReceiveMessageListener extends IInterface {
    boolean onReceived(Message var1, int var2, boolean var3, boolean var4, int var5) throws RemoteException;

    public abstract static class Stub extends Binder implements OnReceiveMessageListener {
        private static final String DESCRIPTOR = "io.rong.imlib.OnReceiveMessageListener";
        static final int TRANSACTION_onReceived = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.OnReceiveMessageListener");
        }

        public static OnReceiveMessageListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.OnReceiveMessageListener");
                return (OnReceiveMessageListener)(iin != null && iin instanceof OnReceiveMessageListener ? (OnReceiveMessageListener)iin : new OnReceiveMessageListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.OnReceiveMessageListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    Message _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    int _arg1 = data.readInt();
                    boolean _arg2 = 0 != data.readInt();
                    boolean _arg3 = 0 != data.readInt();
                    int _arg4 = data.readInt();
                    boolean _result = this.onReceived(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(OnReceiveMessageListener impl) {
            if (OnReceiveMessageListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                OnReceiveMessageListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static OnReceiveMessageListener getDefaultImpl() {
            return OnReceiveMessageListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements OnReceiveMessageListener {
            private IBinder mRemote;
            public static OnReceiveMessageListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.OnReceiveMessageListener";
            }

            public boolean onReceived(Message message, int left, boolean offline, boolean hasMsg, int cmdLeft) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.OnReceiveMessageListener");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeInt(left);
                    _data.writeInt(offline ? 1 : 0);
                    _data.writeInt(hasMsg ? 1 : 0);
                    _data.writeInt(cmdLeft);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && OnReceiveMessageListener.Stub.getDefaultImpl() != null) {
                        boolean var10 = OnReceiveMessageListener.Stub.getDefaultImpl().onReceived(message, left, offline, hasMsg, cmdLeft);
                        return var10;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }
        }
    }

    public static class Default implements OnReceiveMessageListener {
        public Default() {
        }

        public boolean onReceived(Message message, int left, boolean offline, boolean hasMsg, int cmdLeft) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
