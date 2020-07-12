//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobServiceEngine;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.common.rlog.RLog;

public abstract class RongJobIntentService extends Service {
    static final String TAG = "RongJobIntentService";
    static final boolean DEBUG = false;
    CompatJobEngine mJobImpl;
    WorkEnqueuer mCompatWorkEnqueuer;
    CommandProcessor mCurProcessor;
    boolean mInterruptIfStopped = false;
    boolean mStopped = false;
    boolean mDestroyed = false;
    final ArrayList<CompatWorkItem> mCompatQueue;
    static final Object sLock = new Object();
    static final HashMap<ComponentName, WorkEnqueuer> sClassWorkEnqueuer = new HashMap();

    public RongJobIntentService() {
        if (VERSION.SDK_INT >= 26) {
            this.mCompatQueue = null;
        } else {
            this.mCompatQueue = new ArrayList();
        }

    }

    public void onCreate() {
        super.onCreate();
        if (VERSION.SDK_INT >= 26) {
            this.mJobImpl = new JobServiceEngineImpl(this);
            this.mCompatWorkEnqueuer = null;
        } else {
            this.mJobImpl = null;
            ComponentName cn = new ComponentName(this, this.getClass());
            this.mCompatWorkEnqueuer = getWorkEnqueuer(this, cn, false, 0);
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.mCompatQueue != null) {
            this.mCompatWorkEnqueuer.serviceStartReceived();
            synchronized(this.mCompatQueue) {
                this.mCompatQueue.add(new CompatWorkItem(intent != null ? intent : new Intent(), startId));
                this.ensureProcessorRunningLocked(true);
                return 3;
            }
        } else {
            return 2;
        }
    }

    public IBinder onBind(Intent intent) {
        if (this.mJobImpl != null) {
            IBinder engine = this.mJobImpl.compatGetBinder();
            return engine;
        } else {
            return null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mCompatQueue != null) {
            synchronized(this.mCompatQueue) {
                this.mDestroyed = true;
                this.mCompatWorkEnqueuer.serviceProcessingFinished();
            }
        }

    }

    public static void enqueueWork(Context context, Class cls, int jobId, Intent work) {
        enqueueWork(context, new ComponentName(context, cls), jobId, work);
    }

    public static void enqueueWork(Context context, ComponentName component, int jobId, Intent work) {
        if (work == null) {
            throw new IllegalArgumentException("work must not be null");
        } else {
            synchronized(sLock) {
                WorkEnqueuer we = getWorkEnqueuer(context, component, true, jobId);
                we.ensureJobId(jobId);
                we.enqueueWork(work);
            }
        }
    }

    static WorkEnqueuer getWorkEnqueuer(Context context, ComponentName cn, boolean hasJobId, int jobId) {
        WorkEnqueuer we = (WorkEnqueuer)sClassWorkEnqueuer.get(cn);
        if (we == null) {
            if (VERSION.SDK_INT >= 26) {
                if (!hasJobId) {
                    throw new IllegalArgumentException("Can't be here without a job id");
                }

                we = new JobWorkEnqueuer(context, cn, jobId);
            } else {
                we = new CompatWorkEnqueuer(context, cn);
            }

            sClassWorkEnqueuer.put(cn, we);
        }

        return (WorkEnqueuer)we;
    }

    protected abstract void onHandleWork(Intent var1);

    public void setInterruptIfStopped(boolean interruptIfStopped) {
        this.mInterruptIfStopped = interruptIfStopped;
    }

    public boolean isStopped() {
        return this.mStopped;
    }

    public boolean onStopCurrentWork() {
        return true;
    }

    boolean doStopCurrentWork() {
        if (this.mCurProcessor != null) {
            this.mCurProcessor.cancel(this.mInterruptIfStopped);
        }

        this.mStopped = true;
        return this.onStopCurrentWork();
    }

    void ensureProcessorRunningLocked(boolean reportStarted) {
        if (this.mCurProcessor == null) {
            this.mCurProcessor = new CommandProcessor();
            if (this.mCompatWorkEnqueuer != null && reportStarted) {
                this.mCompatWorkEnqueuer.serviceProcessingStarted();
            }

            this.mCurProcessor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

    }

    void processorFinished() {
        if (this.mCompatQueue != null) {
            synchronized(this.mCompatQueue) {
                this.mCurProcessor = null;
                if (this.mCompatQueue != null && this.mCompatQueue.size() > 0) {
                    this.ensureProcessorRunningLocked(false);
                } else if (!this.mDestroyed) {
                    this.mCompatWorkEnqueuer.serviceProcessingFinished();
                }
            }
        }

    }

    GenericWorkItem dequeueWork() {
        if (this.mJobImpl != null) {
            try {
                return this.mJobImpl.dequeueWork();
            } catch (SecurityException var3) {
                RLog.e("RongJobIntentService", "dequeueWork", var3);
                return null;
            }
        } else {
            synchronized(this.mCompatQueue) {
                return this.mCompatQueue.size() > 0 ? (GenericWorkItem)this.mCompatQueue.remove(0) : null;
            }
        }
    }

    final class CommandProcessor extends AsyncTask<Void, Void, Void> {
        CommandProcessor() {
        }

        protected Void doInBackground(Void... params) {
            GenericWorkItem work;
            while((work = RongJobIntentService.this.dequeueWork()) != null) {
                RongJobIntentService.this.onHandleWork(work.getIntent());
                work.complete();
            }

            return null;
        }

        protected void onCancelled(Void aVoid) {
            RongJobIntentService.this.processorFinished();
        }

        protected void onPostExecute(Void aVoid) {
            RongJobIntentService.this.processorFinished();
        }
    }

    final class CompatWorkItem implements GenericWorkItem {
        final Intent mIntent;
        final int mStartId;

        CompatWorkItem(Intent intent, int startId) {
            this.mIntent = intent;
            this.mStartId = startId;
        }

        public Intent getIntent() {
            return this.mIntent;
        }

        public void complete() {
            RongJobIntentService.this.stopSelf(this.mStartId);
        }
    }

    interface GenericWorkItem {
        Intent getIntent();

        void complete();
    }

    static final class JobWorkEnqueuer extends WorkEnqueuer {
        private final JobInfo mJobInfo;
        private final JobScheduler mJobScheduler;

        @SuppressLint({"NewApi"})
        JobWorkEnqueuer(Context context, ComponentName cn, int jobId) {
            super(context, cn);
            this.ensureJobId(jobId);
            Builder b = new Builder(jobId, this.mComponentName);
            this.mJobInfo = b.setOverrideDeadline(0L).build();
            this.mJobScheduler = (JobScheduler)context.getApplicationContext().getSystemService("jobscheduler");
        }

        @SuppressLint({"NewApi"})
        void enqueueWork(Intent work) {
            try {
                this.mJobScheduler.enqueue(this.mJobInfo, new JobWorkItem(work));
            } catch (Exception var3) {
                RLog.e("RongJobIntentService", "enqueueWork", var3);
            }

        }
    }

    @SuppressLint({"NewApi"})
    static final class JobServiceEngineImpl extends JobServiceEngine implements CompatJobEngine {
        static final String TAG = "JobServiceEngineImpl";
        static final boolean DEBUG = false;
        final RongJobIntentService mService;
        final Object mLock = new Object();
        JobParameters mParams;

        JobServiceEngineImpl(RongJobIntentService service) {
            super(service);
            this.mService = service;
        }

        public IBinder compatGetBinder() {
            return this.getBinder();
        }

        public boolean onStartJob(JobParameters params) {
            this.mParams = params;
            this.mService.ensureProcessorRunningLocked(false);
            return true;
        }

        public boolean onStopJob(JobParameters params) {
            boolean result = this.mService.doStopCurrentWork();
            synchronized(this.mLock) {
                this.mParams = null;
                return result;
            }
        }

        public GenericWorkItem dequeueWork() {
            JobWorkItem work;
            synchronized(this.mLock) {
                if (this.mParams == null) {
                    return null;
                }

                work = this.mParams.dequeueWork();
            }

            if (work != null) {
                work.getIntent().setExtrasClassLoader(this.mService.getClassLoader());
                return new WrapperWorkItem(work);
            } else {
                return null;
            }
        }

        final class WrapperWorkItem implements GenericWorkItem {
            final JobWorkItem mJobWork;

            WrapperWorkItem(JobWorkItem jobWork) {
                this.mJobWork = jobWork;
            }

            @SuppressLint({"NewApi"})
            public Intent getIntent() {
                return this.mJobWork.getIntent();
            }

            @SuppressLint({"NewApi"})
            public void complete() {
                synchronized(JobServiceEngineImpl.this.mLock) {
                    if (JobServiceEngineImpl.this.mParams != null) {
                        try {
                            JobServiceEngineImpl.this.mParams.completeWork(this.mJobWork);
                        } catch (Exception var4) {
                            RLog.e("JobServiceEngineImpl", "WrapperWorkItem complete:" + var4.toString());
                        }
                    }

                }
            }
        }
    }

    static final class CompatWorkEnqueuer extends WorkEnqueuer {
        private final Context mContext;
        private final WakeLock mLaunchWakeLock;
        private final WakeLock mRunWakeLock;
        boolean mLaunchingService;
        boolean mServiceProcessing;

        CompatWorkEnqueuer(Context context, ComponentName cn) {
            super(context, cn);
            this.mContext = context.getApplicationContext();
            PowerManager pm = (PowerManager)context.getSystemService("power");
            this.mLaunchWakeLock = pm.newWakeLock(1, cn.getClassName() + ":launch");
            this.mLaunchWakeLock.setReferenceCounted(false);
            this.mRunWakeLock = pm.newWakeLock(1, cn.getClassName() + ":run");
            this.mRunWakeLock.setReferenceCounted(false);
        }

        void enqueueWork(Intent work) {
            Intent intent = new Intent(work);
            intent.setComponent(this.mComponentName);
            if (this.mContext.startService(intent) != null) {
                synchronized(this) {
                    if (!this.mLaunchingService) {
                        this.mLaunchingService = true;
                        if (!this.mServiceProcessing) {
                            this.mLaunchWakeLock.acquire(60000L);
                        }
                    }
                }
            }

        }

        public void serviceStartReceived() {
            synchronized(this) {
                this.mLaunchingService = false;
            }
        }

        public void serviceProcessingStarted() {
            synchronized(this) {
                if (!this.mServiceProcessing) {
                    this.mServiceProcessing = true;
                    this.mRunWakeLock.acquire(600000L);
                    this.mLaunchWakeLock.release();
                }

            }
        }

        public void serviceProcessingFinished() {
            synchronized(this) {
                if (this.mServiceProcessing) {
                    if (this.mLaunchingService) {
                        this.mLaunchWakeLock.acquire(60000L);
                    }

                    this.mServiceProcessing = false;
                    this.mRunWakeLock.release();
                }

            }
        }
    }

    interface CompatJobEngine {
        IBinder compatGetBinder();

        GenericWorkItem dequeueWork();
    }

    abstract static class WorkEnqueuer {
        final ComponentName mComponentName;
        boolean mHasJobId;
        int mJobId;

        WorkEnqueuer(Context context, ComponentName cn) {
            this.mComponentName = cn;
        }

        void ensureJobId(int jobId) {
            if (!this.mHasJobId) {
                this.mHasJobId = true;
                this.mJobId = jobId;
            } else if (this.mJobId != jobId) {
                throw new IllegalArgumentException("Given job ID " + jobId + " is different than previous " + this.mJobId);
            }

        }

        abstract void enqueueWork(Intent var1);

        public void serviceStartReceived() {
        }

        public void serviceProcessingStarted() {
        }

        public void serviceProcessingFinished() {
        }
    }
}
