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
import io.rong.imlib.model.Conversation;
import java.util.List;

public interface IGetConversationListWithProcessCallback extends IInterface {
    void onProcess(List<Conversation> var1) throws RemoteException;

    void onComplete() throws RemoteException;

    public abstract static class Stub extends Binder implements IGetConversationListWithProcessCallback {
        private static final String DESCRIPTOR = "io.rong.imlib.IGetConversationListWithProcessCallback";
        static final int TRANSACTION_onProcess = 1;
        static final int TRANSACTION_onComplete = 2;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IGetConversationListWithProcessCallback");
        }

        public static IGetConversationListWithProcessCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IGetConversationListWithProcessCallback");
                return (IGetConversationListWithProcessCallback)(iin != null && iin instanceof IGetConversationListWithProcessCallback ? (IGetConversationListWithProcessCallback)iin : new IGetConversationListWithProcessCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IGetConversationListWithProcessCallback";
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    List<Conversation> _arg0 = data.createTypedArrayList(Conversation.CREATOR);
                    this.onProcess(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    this.onComplete();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IGetConversationListWithProcessCallback impl) {
            if (IGetConversationListWithProcessCallback.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IGetConversationListWithProcessCallback.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IGetConversationListWithProcessCallback getDefaultImpl() {
            return IGetConversationListWithProcessCallback.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IGetConversationListWithProcessCallback {
            private IBinder mRemote;
            public static IGetConversationListWithProcessCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IGetConversationListWithProcessCallback";
            }

            public void onProcess(List<Conversation> list) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IGetConversationListWithProcessCallback");
                    _data.writeTypedList(list);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && IGetConversationListWithProcessCallback.Stub.getDefaultImpl() != null) {
                        IGetConversationListWithProcessCallback.Stub.getDefaultImpl().onProcess(list);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IGetConversationListWithProcessCallback");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (_status || IGetConversationListWithProcessCallback.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IGetConversationListWithProcessCallback.Stub.getDefaultImpl().onComplete();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IGetConversationListWithProcessCallback {
        public Default() {
        }

        public void onProcess(List<Conversation> list) throws RemoteException {
        }

        public void onComplete() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
