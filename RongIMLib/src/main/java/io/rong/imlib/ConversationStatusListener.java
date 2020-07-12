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
import io.rong.imlib.model.ConversationStatus;

public interface ConversationStatusListener extends IInterface {
    void OnStatusChanged(ConversationStatus[] var1) throws RemoteException;

    public abstract static class Stub extends Binder implements ConversationStatusListener {
        private static final String DESCRIPTOR = "io.rong.imlib.ConversationStatusListener";
        static final int TRANSACTION_OnStatusChanged = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.ConversationStatusListener");
        }

        public static ConversationStatusListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.ConversationStatusListener");
                return (ConversationStatusListener)(iin != null && iin instanceof ConversationStatusListener ? (ConversationStatusListener)iin : new ConversationStatusListener.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.ConversationStatusListener";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    ConversationStatus[] _arg0 = (ConversationStatus[])data.createTypedArray(ConversationStatus.CREATOR);
                    this.OnStatusChanged(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(ConversationStatusListener impl) {
            if (ConversationStatusListener.Stub.Proxy.sDefaultImpl == null && impl != null) {
                ConversationStatusListener.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static ConversationStatusListener getDefaultImpl() {
            return ConversationStatusListener.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements ConversationStatusListener {
            private IBinder mRemote;
            public static ConversationStatusListener sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.ConversationStatusListener";
            }

            public void OnStatusChanged(ConversationStatus[] conversationStatus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.ConversationStatusListener");
                    _data.writeTypedArray(conversationStatus, 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || ConversationStatusListener.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    ConversationStatusListener.Stub.getDefaultImpl().OnStatusChanged(conversationStatus);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements ConversationStatusListener {
        public Default() {
        }

        public void OnStatusChanged(ConversationStatus[] conversationStatus) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
