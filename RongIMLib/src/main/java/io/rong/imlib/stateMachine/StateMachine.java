//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.stateMachine;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class StateMachine {
    private String mName;
    private static final int SM_QUIT_CMD = -1;
    private static final int SM_INIT_CMD = -2;
    public static final boolean HANDLED = true;
    public static final boolean NOT_HANDLED = false;
    private StateMachine.SmHandler mSmHandler;
    private HandlerThread mSmThread;

    private void initStateMachine(String name, Looper looper) {
        this.mName = name;
        this.mSmHandler = new StateMachine.SmHandler(looper, this);
    }

    protected StateMachine(String name) {
        this.mSmThread = new HandlerThread(name);
        this.mSmThread.start();
        Looper looper = this.mSmThread.getLooper();
        this.initStateMachine(name, looper);
    }

    protected StateMachine(String name, Looper looper) {
        this.initStateMachine(name, looper);
    }

    protected StateMachine(String name, Handler handler) {
        this.initStateMachine(name, handler.getLooper());
    }

    protected final void addState(State state, State parent) {
        this.mSmHandler.addState(state, parent);
    }

    protected final void addState(State state) {
        this.mSmHandler.addState(state, (State)null);
    }

    protected final void setInitialState(State initialState) {
        this.mSmHandler.setInitialState(initialState);
    }

    protected final Message getCurrentMessage() {
        StateMachine.SmHandler smh = this.mSmHandler;
        return smh == null ? null : smh.getCurrentMessage();
    }

    protected final IState getCurrentState() {
        StateMachine.SmHandler smh = this.mSmHandler;
        return smh == null ? null : smh.getCurrentState();
    }

    protected final void transitionTo(IState destState) {
        this.mSmHandler.transitionTo(destState);
    }

    protected final void transitionToHaltingState() {
        this.mSmHandler.transitionTo(this.mSmHandler.mHaltingState);
    }

    protected final void deferMessage(Message msg) {
        this.mSmHandler.deferMessage(msg);
    }

    protected void unhandledMessage(Message msg) {
        if (this.mSmHandler.mDbg) {
            this.loge(" - unhandledMessage: msg.what=" + msg.what);
        }

    }

    protected void haltedProcessMessage(Message msg) {
    }

    protected void onHalting() {
    }

    protected void onQuitting() {
    }

    public final String getName() {
        return this.mName;
    }

    public final void setLogRecSize(int maxSize) {
        this.mSmHandler.mLogRecords.setSize(maxSize);
    }

    public final void setLogOnlyTransitions(boolean enable) {
        this.mSmHandler.mLogRecords.setLogOnlyTransitions(enable);
    }

    public final int getLogRecSize() {
        StateMachine.SmHandler smh = this.mSmHandler;
        return smh == null ? 0 : smh.mLogRecords.size();
    }

    public final int getLogRecCount() {
        StateMachine.SmHandler smh = this.mSmHandler;
        return smh == null ? 0 : smh.mLogRecords.count();
    }

    public final StateMachine.LogRec getLogRec(int index) {
        StateMachine.SmHandler smh = this.mSmHandler;
        return smh == null ? null : smh.mLogRecords.get(index);
    }

    public final Collection<StateMachine.LogRec> copyLogRecs() {
        Vector<StateMachine.LogRec> vlr = new Vector();
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            vlr.addAll(smh.mLogRecords.mLogRecVector);
        }

        return vlr;
    }

    protected void addLogRec(String string) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.mLogRecords.add(this, smh.getCurrentMessage(), string, smh.getCurrentState(), smh.mStateStack[smh.mStateStackTopIndex].state, smh.mDestState);
        }
    }

    protected boolean recordLogRec(Message msg) {
        return true;
    }

    protected String getLogRecString(Message msg) {
        return "";
    }

    protected String getWhatToString(int what) {
        return null;
    }

    public final Handler getHandler() {
        return this.mSmHandler;
    }

    public final Message obtainMessage() {
        return Message.obtain(this.mSmHandler);
    }

    public final Message obtainMessage(int what) {
        return Message.obtain(this.mSmHandler, what);
    }

    public final Message obtainMessage(int what, Object obj) {
        return Message.obtain(this.mSmHandler, what, obj);
    }

    public final Message obtainMessage(int what, int arg1) {
        return Message.obtain(this.mSmHandler, what, arg1, 0);
    }

    public final Message obtainMessage(int what, int arg1, int arg2) {
        return Message.obtain(this.mSmHandler, what, arg1, arg2);
    }

    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return Message.obtain(this.mSmHandler, what, arg1, arg2, obj);
    }

    public final void sendMessage(int what) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessage(this.obtainMessage(what));
        }
    }

    public final void sendMessage(int what, Object obj) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessage(this.obtainMessage(what, obj));
        }
    }

    public final void sendMessage(int what, int arg1) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessage(this.obtainMessage(what, arg1));
        }
    }

    public final void sendMessage(int what, int arg1, int arg2) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessage(this.obtainMessage(what, arg1, arg2));
        }
    }

    public final void sendMessage(int what, int arg1, int arg2, Object obj) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessage(this.obtainMessage(what, arg1, arg2, obj));
        }
    }

    public final void sendMessage(Message msg) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessage(msg);
        }
    }

    public final void sendMessageDelayed(int what, long delayMillis) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageDelayed(this.obtainMessage(what), delayMillis);
        }
    }

    public final void sendMessageDelayed(int what, Object obj, long delayMillis) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageDelayed(this.obtainMessage(what, obj), delayMillis);
        }
    }

    public final void sendMessageDelayed(int what, int arg1, long delayMillis) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageDelayed(this.obtainMessage(what, arg1), delayMillis);
        }
    }

    public final void sendMessageDelayed(int what, int arg1, int arg2, long delayMillis) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageDelayed(this.obtainMessage(what, arg1, arg2), delayMillis);
        }
    }

    public final void sendMessageDelayed(int what, int arg1, int arg2, Object obj, long delayMillis) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageDelayed(this.obtainMessage(what, arg1, arg2, obj), delayMillis);
        }
    }

    public final void sendMessageDelayed(Message msg, long delayMillis) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageDelayed(msg, delayMillis);
        }
    }

    protected final void sendMessageAtFrontOfQueue(int what) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageAtFrontOfQueue(this.obtainMessage(what));
        }
    }

    protected final void sendMessageAtFrontOfQueue(int what, Object obj) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageAtFrontOfQueue(this.obtainMessage(what, obj));
        }
    }

    protected final void sendMessageAtFrontOfQueue(int what, int arg1) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageAtFrontOfQueue(this.obtainMessage(what, arg1));
        }
    }

    protected final void sendMessageAtFrontOfQueue(int what, int arg1, int arg2) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageAtFrontOfQueue(this.obtainMessage(what, arg1, arg2));
        }
    }

    protected final void sendMessageAtFrontOfQueue(int what, int arg1, int arg2, Object obj) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageAtFrontOfQueue(this.obtainMessage(what, arg1, arg2, obj));
        }
    }

    protected final void sendMessageAtFrontOfQueue(Message msg) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.sendMessageAtFrontOfQueue(msg);
        }
    }

    protected final void removeMessages(int what) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.removeMessages(what);
        }
    }

    protected final boolean isQuit(Message msg) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh == null) {
            return msg.what == -1;
        } else {
            return smh.isQuit(msg);
        }
    }

    protected final void quit() {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.quit();
        }
    }

    protected final void quitNow() {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.quitNow();
        }
    }

    public boolean isDbg() {
        StateMachine.SmHandler smh = this.mSmHandler;
        return smh == null ? false : smh.isDbg();
    }

    public void setDbg(boolean dbg) {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.setDbg(dbg);
        }
    }

    public void start() {
        StateMachine.SmHandler smh = this.mSmHandler;
        if (smh != null) {
            smh.completeConstruction();
        }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println(" total records=" + this.getLogRecCount());

        for(int i = 0; i < this.getLogRecSize(); ++i) {
            StateMachine.LogRec logRec = this.getLogRec(i);
            if (logRec != null) {
                pw.printf(" rec[%d]: %s\n", i, logRec.toString());
            }

            pw.flush();
        }

        IState iState = this.getCurrentState();
        if (iState != null) {
            pw.println("curState=" + iState.getName());
        }

    }

    protected void logAndAddLogRec(String s) {
        this.addLogRec(s);
        this.log(s);
    }

    protected void log(String s) {
        Log.d(this.mName, s);
    }

    protected void logd(String s) {
        Log.d(this.mName, s);
    }

    protected void logv(String s) {
        Log.v(this.mName, s);
    }

    protected void logi(String s) {
        Log.i(this.mName, s);
    }

    protected void logw(String s) {
        Log.w(this.mName, s);
    }

    protected void loge(String s) {
        Log.e(this.mName, s);
    }

    protected void loge(String s, Throwable e) {
        Log.e(this.mName, s, e);
    }

    private static class SmHandler extends Handler {
        private boolean mHasQuit;
        private boolean mDbg;
        private static final Object mSmHandlerObj = new Object();
        private Message mMsg;
        private StateMachine.LogRecords mLogRecords;
        private boolean mIsConstructionCompleted;
        private StateMachine.SmHandler.StateInfo[] mStateStack;
        private int mStateStackTopIndex;
        private StateMachine.SmHandler.StateInfo[] mTempStateStack;
        private int mTempStateStackCount;
        private StateMachine.SmHandler.HaltingState mHaltingState;
        private StateMachine.SmHandler.QuittingState mQuittingState;
        private StateMachine mSm;
        private HashMap<State, StateMachine.SmHandler.StateInfo> mStateInfo;
        private State mInitialState;
        private State mDestState;
        private ArrayList<Message> mDeferredMessages;

        public final void handleMessage(Message msg) {
            if (!this.mHasQuit) {
                if (this.mDbg) {
                    this.mSm.log("handleMessage: E msg.what=" + msg.what);
                }

                this.mMsg = msg;
                State msgProcessedState = null;
                if (this.mIsConstructionCompleted) {
                    msgProcessedState = this.processMsg(msg);
                } else {
                    if (this.mMsg.what != -2 || this.mMsg.obj != mSmHandlerObj) {
                        throw new RuntimeException("StateMachine.handleMessage: The start method not called, received msg: " + msg);
                    }

                    this.mIsConstructionCompleted = true;
                    this.invokeEnterMethods(0);
                }

                this.performTransitions(msgProcessedState, msg);
                if (this.mDbg && this.mSm != null) {
                    this.mSm.log("handleMessage: X");
                }
            }

        }

        private void performTransitions(State msgProcessedState, Message msg) {
            State orgState = this.mStateStack[this.mStateStackTopIndex].state;
            boolean recordLogMsg = this.mSm.recordLogRec(this.mMsg) && msg.obj != mSmHandlerObj;
            if (this.mLogRecords.logOnlyTransitions()) {
                if (this.mDestState != null) {
                    this.mLogRecords.add(this.mSm, this.mMsg, this.mSm.getLogRecString(this.mMsg), msgProcessedState, orgState, this.mDestState);
                }
            } else if (recordLogMsg) {
                this.mLogRecords.add(this.mSm, this.mMsg, this.mSm.getLogRecString(this.mMsg), msgProcessedState, orgState, this.mDestState);
            }

            State destState = this.mDestState;
            if (destState != null) {
                while(true) {
                    if (this.mDbg) {
                        this.mSm.log("handleMessage: new destination call exit/enter");
                    }

                    StateMachine.SmHandler.StateInfo commonStateInfo = this.setupTempStateStackWithStatesToEnter(destState);
                    this.invokeExitMethods(commonStateInfo);
                    int stateStackEnteringIndex = this.moveTempStateStackToStateStack();
                    this.invokeEnterMethods(stateStackEnteringIndex);
                    this.moveDeferredMessageAtFrontOfQueue();
                    if (destState == this.mDestState) {
                        this.mDestState = null;
                        break;
                    }

                    destState = this.mDestState;
                }
            }

            if (destState != null) {
                if (destState == this.mQuittingState) {
                    this.mSm.onQuitting();
                    this.cleanupAfterQuitting();
                } else if (destState == this.mHaltingState) {
                    this.mSm.onHalting();
                }
            }

        }

        private void cleanupAfterQuitting() {
            if (this.mSm.mSmThread != null) {
                this.getLooper().quit();
                this.mSm.mSmThread = null;
            }

            this.mSm.mSmHandler = null;
            this.mSm = null;
            this.mMsg = null;
            this.mLogRecords.cleanup();
            this.mStateStack = null;
            this.mTempStateStack = null;
            this.mStateInfo.clear();
            this.mInitialState = null;
            this.mDestState = null;
            this.mDeferredMessages.clear();
            this.mHasQuit = true;
        }

        private void completeConstruction() {
            if (this.mDbg) {
                this.mSm.log("completeConstruction: E");
            }

            int maxDepth = 0;
            Iterator var2 = this.mStateInfo.values().iterator();

            while(var2.hasNext()) {
                StateMachine.SmHandler.StateInfo si = (StateMachine.SmHandler.StateInfo)var2.next();
                int depth = 0;

                for(StateMachine.SmHandler.StateInfo i = si; i != null; ++depth) {
                    i = i.parentStateInfo;
                }

                if (maxDepth < depth) {
                    maxDepth = depth;
                }
            }

            if (this.mDbg) {
                this.mSm.log("completeConstruction: maxDepth=" + maxDepth);
            }

            this.mStateStack = new StateMachine.SmHandler.StateInfo[maxDepth];
            this.mTempStateStack = new StateMachine.SmHandler.StateInfo[maxDepth];
            this.setupInitialStateStack();
            this.sendMessageAtFrontOfQueue(this.obtainMessage(-2, mSmHandlerObj));
            if (this.mDbg) {
                this.mSm.log("completeConstruction: X");
            }

        }

        private State processMsg(Message msg) {
            StateMachine.SmHandler.StateInfo curStateInfo = this.mStateStack[this.mStateStackTopIndex];
            if (this.mDbg) {
                this.mSm.log("processMsg: " + curStateInfo.state.getName());
            }

            if (this.isQuit(msg)) {
                this.transitionTo(this.mQuittingState);
            } else {
                while(!curStateInfo.state.processMessage(msg)) {
                    curStateInfo = curStateInfo.parentStateInfo;
                    if (curStateInfo == null) {
                        this.mSm.unhandledMessage(msg);
                        break;
                    }

                    if (this.mDbg) {
                        this.mSm.log("processMsg: " + curStateInfo.state.getName());
                    }
                }
            }

            return curStateInfo != null ? curStateInfo.state : null;
        }

        private void invokeExitMethods(StateMachine.SmHandler.StateInfo commonStateInfo) {
            while(this.mStateStackTopIndex >= 0 && this.mStateStack[this.mStateStackTopIndex] != commonStateInfo) {
                State curState = this.mStateStack[this.mStateStackTopIndex].state;
                if (this.mDbg) {
                    this.mSm.log("invokeExitMethods: " + curState.getName());
                }

                curState.exit();
                this.mStateStack[this.mStateStackTopIndex].active = false;
                --this.mStateStackTopIndex;
            }

        }

        private void invokeEnterMethods(int stateStackEnteringIndex) {
            for(int i = stateStackEnteringIndex; i <= this.mStateStackTopIndex; ++i) {
                if (this.mDbg) {
                    this.mSm.log("invokeEnterMethods: " + this.mStateStack[i].state.getName());
                }

                this.mStateStack[i].state.enter();
                this.mStateStack[i].active = true;
            }

        }

        private void moveDeferredMessageAtFrontOfQueue() {
            for(int i = this.mDeferredMessages.size() - 1; i >= 0; --i) {
                Message curMsg = (Message)this.mDeferredMessages.get(i);
                if (this.mDbg) {
                    this.mSm.log("moveDeferredMessageAtFrontOfQueue; what=" + curMsg.what);
                }

                this.sendMessageAtFrontOfQueue(curMsg);
            }

            this.mDeferredMessages.clear();
        }

        private int moveTempStateStackToStateStack() {
            int startingIndex = this.mStateStackTopIndex + 1;
            int i = this.mTempStateStackCount - 1;

            int j;
            for(j = startingIndex; i >= 0; --i) {
                if (this.mDbg) {
                    this.mSm.log("moveTempStackToStateStack: i=" + i + ",j=" + j);
                }

                this.mStateStack[j] = this.mTempStateStack[i];
                ++j;
            }

            this.mStateStackTopIndex = j - 1;
            if (this.mDbg) {
                this.mSm.log("moveTempStackToStateStack: X mStateStackTop=" + this.mStateStackTopIndex + ",startingIndex=" + startingIndex + ",Top=" + this.mStateStack[this.mStateStackTopIndex].state.getName());
            }

            return startingIndex;
        }

        private StateMachine.SmHandler.StateInfo setupTempStateStackWithStatesToEnter(State destState) {
            this.mTempStateStackCount = 0;
            StateMachine.SmHandler.StateInfo curStateInfo = (StateMachine.SmHandler.StateInfo)this.mStateInfo.get(destState);

            do {
                this.mTempStateStack[this.mTempStateStackCount++] = curStateInfo;
                if (curStateInfo != null) {
                    curStateInfo = curStateInfo.parentStateInfo;
                }
            } while(curStateInfo != null && !curStateInfo.active);

            if (this.mDbg) {
                this.mSm.log("setupTempStateStackWithStatesToEnter: X mTempStateStackCount=" + this.mTempStateStackCount + ",curStateInfo: " + curStateInfo);
            }

            return curStateInfo;
        }

        private void setupInitialStateStack() {
            if (this.mDbg) {
                this.mSm.log("setupInitialStateStack: E mInitialState=" + this.mInitialState.getName());
            }

            StateMachine.SmHandler.StateInfo curStateInfo = (StateMachine.SmHandler.StateInfo)this.mStateInfo.get(this.mInitialState);

            for(this.mTempStateStackCount = 0; curStateInfo != null; ++this.mTempStateStackCount) {
                this.mTempStateStack[this.mTempStateStackCount] = curStateInfo;
                curStateInfo = curStateInfo.parentStateInfo;
            }

            this.mStateStackTopIndex = -1;
            this.moveTempStateStackToStateStack();
        }

        private Message getCurrentMessage() {
            return this.mMsg;
        }

        private IState getCurrentState() {
            return this.mStateStack[this.mStateStackTopIndex].state;
        }

        private StateMachine.SmHandler.StateInfo addState(State state, State parent) {
            if (this.mDbg) {
                this.mSm.log("addStateInternal: E state=" + state.getName() + ",parent=" + (parent == null ? "" : parent.getName()));
            }

            StateMachine.SmHandler.StateInfo parentStateInfo = null;
            if (parent != null) {
                parentStateInfo = (StateMachine.SmHandler.StateInfo)this.mStateInfo.get(parent);
                if (parentStateInfo == null) {
                    parentStateInfo = this.addState(parent, (State)null);
                }
            }

            StateMachine.SmHandler.StateInfo stateInfo = (StateMachine.SmHandler.StateInfo)this.mStateInfo.get(state);
            if (stateInfo == null) {
                stateInfo = new StateMachine.SmHandler.StateInfo();
                this.mStateInfo.put(state, stateInfo);
            }

            if (stateInfo.parentStateInfo != null && stateInfo.parentStateInfo != parentStateInfo) {
                throw new RuntimeException("state already added");
            } else {
                stateInfo.state = state;
                stateInfo.parentStateInfo = parentStateInfo;
                stateInfo.active = false;
                if (this.mDbg) {
                    this.mSm.log("addStateInternal: X stateInfo: " + stateInfo);
                }

                return stateInfo;
            }
        }

        private SmHandler(Looper looper, StateMachine sm) {
            super(looper);
            this.mHasQuit = false;
            this.mDbg = false;
            this.mLogRecords = new StateMachine.LogRecords();
            this.mStateStackTopIndex = -1;
            this.mHaltingState = new StateMachine.SmHandler.HaltingState();
            this.mQuittingState = new StateMachine.SmHandler.QuittingState();
            this.mStateInfo = new HashMap();
            this.mDeferredMessages = new ArrayList();
            this.mSm = sm;
            this.addState(this.mHaltingState, (State)null);
            this.addState(this.mQuittingState, (State)null);
        }

        private void setInitialState(State initialState) {
            if (this.mDbg) {
                this.mSm.log("setInitialState: initialState=" + initialState.getName());
            }

            this.mInitialState = initialState;
        }

        private void transitionTo(IState destState) {
            this.mDestState = (State)destState;
            if (this.mDbg) {
                this.mSm.log("transitionTo: destState=" + this.mDestState.getName());
            }

        }

        private void deferMessage(Message msg) {
            if (this.mDbg) {
                this.mSm.log("deferMessage: msg=" + msg.what);
            }

            Message newMsg = this.obtainMessage();
            newMsg.copyFrom(msg);
            this.mDeferredMessages.add(newMsg);
        }

        private void quit() {
            if (this.mDbg) {
                this.mSm.log("quit:");
            }

            this.sendMessage(this.obtainMessage(-1, mSmHandlerObj));
        }

        private void quitNow() {
            if (this.mDbg) {
                this.mSm.log("quitNow:");
            }

            this.sendMessageAtFrontOfQueue(this.obtainMessage(-1, mSmHandlerObj));
        }

        private boolean isQuit(Message msg) {
            return msg.what == -1 && msg.obj == mSmHandlerObj;
        }

        private boolean isDbg() {
            return this.mDbg;
        }

        private void setDbg(boolean dbg) {
            this.mDbg = dbg;
        }

        private class QuittingState extends State {
            private QuittingState() {
            }

            public boolean processMessage(Message msg) {
                return false;
            }
        }

        private class HaltingState extends State {
            private HaltingState() {
            }

            public boolean processMessage(Message msg) {
                SmHandler.this.mSm.haltedProcessMessage(msg);
                return true;
            }
        }

        private class StateInfo {
            State state;
            StateMachine.SmHandler.StateInfo parentStateInfo;
            boolean active;

            private StateInfo() {
            }

            public String toString() {
                return "state=" + this.state.getName() + ",active=" + this.active + ",parent=" + (this.parentStateInfo == null ? "null" : this.parentStateInfo.state.getName());
            }
        }
    }

    private static class LogRecords {
        private static final int DEFAULT_SIZE = 20;
        private Vector<StateMachine.LogRec> mLogRecVector;
        private int mMaxSize;
        private int mOldestIndex;
        private int mCount;
        private boolean mLogOnlyTransitions;

        private LogRecords() {
            this.mLogRecVector = new Vector();
            this.mMaxSize = 20;
            this.mOldestIndex = 0;
            this.mCount = 0;
            this.mLogOnlyTransitions = false;
        }

        synchronized void setSize(int maxSize) {
            this.mMaxSize = maxSize;
            this.mCount = 0;
            this.mLogRecVector.clear();
        }

        synchronized void setLogOnlyTransitions(boolean enable) {
            this.mLogOnlyTransitions = enable;
        }

        synchronized boolean logOnlyTransitions() {
            return this.mLogOnlyTransitions;
        }

        synchronized int size() {
            return this.mLogRecVector.size();
        }

        synchronized int count() {
            return this.mCount;
        }

        synchronized void cleanup() {
            this.mLogRecVector.clear();
        }

        synchronized StateMachine.LogRec get(int index) {
            int nextIndex = this.mOldestIndex + index;
            if (nextIndex >= this.mMaxSize) {
                nextIndex -= this.mMaxSize;
            }

            return nextIndex >= this.size() ? null : (StateMachine.LogRec)this.mLogRecVector.get(nextIndex);
        }

        synchronized void add(StateMachine sm, Message msg, String messageInfo, IState state, IState orgState, IState transToState) {
            ++this.mCount;
            if (this.mLogRecVector.size() < this.mMaxSize) {
                this.mLogRecVector.add(new StateMachine.LogRec(sm, msg, messageInfo, state, orgState, transToState));
            } else {
                StateMachine.LogRec pmi = (StateMachine.LogRec)this.mLogRecVector.get(this.mOldestIndex);
                ++this.mOldestIndex;
                if (this.mOldestIndex >= this.mMaxSize) {
                    this.mOldestIndex = 0;
                }

                pmi.update(sm, msg, messageInfo, state, orgState, transToState);
            }

        }
    }

    public static class LogRec {
        private StateMachine mSm;
        private long mTime;
        private int mWhat;
        private String mInfo;
        private IState mState;
        private IState mOrgState;
        private IState mDstState;

        LogRec(StateMachine sm, Message msg, String info, IState state, IState orgState, IState transToState) {
            this.update(sm, msg, info, state, orgState, transToState);
        }

        public void update(StateMachine sm, Message msg, String info, IState state, IState orgState, IState dstState) {
            this.mSm = sm;
            this.mTime = System.currentTimeMillis();
            this.mWhat = msg != null ? msg.what : 0;
            this.mInfo = info;
            this.mState = state;
            this.mOrgState = orgState;
            this.mDstState = dstState;
        }

        public long getTime() {
            return this.mTime;
        }

        public long getWhat() {
            return (long)this.mWhat;
        }

        public String getInfo() {
            return this.mInfo;
        }

        public IState getState() {
            return this.mState;
        }

        public IState getDestState() {
            return this.mDstState;
        }

        public IState getOriginalState() {
            return this.mOrgState;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("time=");
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(this.mTime);
            sb.append(String.format("%tm-%td %tH:%tM:%tS.%tL", c, c, c, c, c, c));
            sb.append(" processed=");
            sb.append(this.mState == null ? "<null>" : this.mState.getName());
            sb.append(" org=");
            sb.append(this.mOrgState == null ? "<null>" : this.mOrgState.getName());
            sb.append(" dest=");
            sb.append(this.mDstState == null ? "<null>" : this.mDstState.getName());
            sb.append(" what=");
            String what = this.mSm != null ? this.mSm.getWhatToString(this.mWhat) : "";
            if (TextUtils.isEmpty(what)) {
                sb.append(this.mWhat);
                sb.append("(0x");
                sb.append(Integer.toHexString(this.mWhat));
                sb.append(")");
            } else {
                sb.append(what);
            }

            if (!TextUtils.isEmpty(this.mInfo)) {
                sb.append(" ");
                sb.append(this.mInfo);
            }

            return sb.toString();
        }
    }
}
