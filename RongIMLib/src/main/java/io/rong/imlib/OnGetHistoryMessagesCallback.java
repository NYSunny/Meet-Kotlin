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

public interface OnGetHistoryMessagesCallback extends IInterface {
    void onComplete(RemoteModelWrap var1) throws RemoteException;

    public abstract static class Stub extends Binder implements OnGetHistoryMessagesCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.OnGetHistoryMessagesCallback";
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.OnGetHistoryMessagesCallback");
        }

        public static OnGetHistoryMessagesCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.OnGetHistoryMessagesCallback");
                return (OnGetHistoryMessagesCallback)(iin != null && iin instanceof OnGetHistoryMessagesCallback ? (OnGetHistoryMessagesCallback)iin : new OnGetHistoryMessagesCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.OnGetHistoryMessagesCallback";
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
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(OnGetHistoryMessagesCallback impl) {
            if (OnGetHistoryMessagesCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                OnGetHistoryMessagesCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static OnGetHistoryMessagesCallback getDefaultImpl() {
            return OnGetHistoryMessagesCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements OnGetHistoryMessagesCallback {
            private IBinder mRemote;
            public static OnGetHistoryMessagesCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.OnGetHistoryMessagesCallback";
            }

            public void onComplete(RemoteModelWrap model) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.OnGetHistoryMessagesCallback");
                    if (model != null) {
                        _data.writeInt(1);
                        model.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && OnGetHistoryMessagesCallback.Stub.getDefaultImpl() != null) {
                        OnGetHistoryMessagesCallback.Stub.getDefaultImpl().onComplete(model);
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

    public static class Default implements OnGetHistoryMessagesCallback {
        public Default() {
        }

        public void onComplete(RemoteModelWrap model) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
