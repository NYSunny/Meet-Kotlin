//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.rlog;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;

import io.rong.common.rlog.Compressor.ICompressListener;
import io.rong.common.rlog.LogFileWriter.IWriterListener;
import io.rong.common.rlog.LogFileWriter.IWriterOnStopListener;
import io.rong.common.rlog.RLogConfig.ZipConfig;
import io.rong.common.rlog.RLogReporter.IUploadListener;
import io.rong.common.rlog.RLogReporter.UploadConfig;

public class RLogFileProcessor {
    private static final String TAG = "RLogFileProcessor";
    private LogFileWriter mLogFileWriter;
    private Compressor mCompressor;
    private RLogConfig mConfig;
    private volatile boolean isUploading = false;
    private RLogReporter mReporter;
    int fileSize = 0;

    private RLogFileProcessor(RLogConfig pConfig) {
        this.mConfig = pConfig;
        this.mReporter = new RLogReporter();
        this.mLogFileWriter = new LogFileWriter(pConfig.getFilePath(), new IWriterListener() {
            public void onWriteFinish(long fileSize, String filePath) {
                if (fileSize > RLogFileProcessor.this.mConfig.getFileMaxSize()) {
                    RLogFileProcessor.this.mLogFileWriter.close();
                    long startTime = RLogFileProcessor.this.mConfig.getStartTime();
                    long endTime = System.currentTimeMillis();
                    String prefix = startTime + "_" + endTime;
                    String logName = prefix + ".log";
                    File file = new File(filePath);
                    File renameFile = new File(file.getParent(), logName);
                    boolean renameResult = file.renameTo(renameFile);
                    if (renameResult) {
                        RLogFileProcessor.this.mCompressor.compress(renameFile.getAbsolutePath(), (new File(file.getParent(), prefix + ".gz")).getAbsolutePath());
                    }
                }

            }

            public void onFileCreate(long createTime) {
                if (RLogFileProcessor.this.mConfig != null) {
                    RLogFileProcessor.this.mConfig.setStartTime(createTime);
                }

            }
        });
        this.mCompressor = new Compressor(new ICompressListener() {
            public void onCompressFinish(boolean result, String sourceFile, String targetFile) {
                RLogFileProcessor.this.processCompressFinish(result, sourceFile, targetFile, false);
            }
        });
    }

    private void processCompressFinish(boolean result, String sourceFile, String targetFile, boolean needUpload) {
        if (result) {
            if (!(new File(sourceFile)).delete()) {
                RLog.i("RLogFileProcessor", "processCompressFinish file is:" + sourceFile);
            }

            if (!this.isUploading) {
                ZipConfig zipConfig = this.mConfig.getZipConfig();
                if (zipConfig != null) {
                    File file = new File(targetFile);
                    zipConfig.setCurrentSize(zipConfig.getCurrentSize() + file.length());
                    zipConfig.addFile(file);

                    while(zipConfig.getCurrentSize() > this.mConfig.getZipMaxSize()) {
                        File first = (File)zipConfig.getZipFiles().pollFirst();
                        zipConfig.setCurrentSize(zipConfig.getCurrentSize() - first.length());
                        if (!first.delete()) {
                            RLog.i("RLogFileProcessor", "processCompressFinish file is:" + first.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            if (!(new File(sourceFile)).delete()) {
                RLog.i("RLogFileProcessor", "processCompressFinish file is:" + sourceFile);
            }

            if (!(new File(targetFile)).delete()) {
                RLog.i("RLogFileProcessor", "processCompressFinish file is:" + targetFile);
            }
        }

        if (needUpload) {
            this.uploadLog();
        }

    }

    private void uploadLog() {
        if (!this.isUploading) {
            ZipConfig zipConfig = this.mConfig.getZipConfig();
            if (zipConfig != null) {
                TreeSet<File> zipFiles = zipConfig.getZipFiles();
                this.fileSize = zipFiles.size();
                this.isUploading = true;
                Iterator var3 = zipFiles.iterator();

                while(var3.hasNext()) {
                    File currentFile = (File)var3.next();
                    String name = currentFile.getName();
                    String time = name.substring(0, name.length() - ".gz".length());
                    String[] arrTime = time.split("_");
                    UploadConfig uploadConfig = new UploadConfig(this.mConfig.getUploadUrl(), currentFile.getAbsolutePath(), arrTime[0], arrTime[1], this.mConfig.getSdkVersion(), this.mConfig.getAppKey(), this.mConfig.getUserId());
                    this.mReporter.report(uploadConfig, new IUploadListener() {
                        public void onUploadFinish(boolean result, String filePath) {
                            if (result && !(new File(filePath)).delete()) {
                                RLog.i("RLogFileProcessor", "uploadLog file is:" + filePath);
                            }

                            --RLogFileProcessor.this.fileSize;
                            if (RLogFileProcessor.this.fileSize == 0) {
                                RLogFileProcessor.this.mConfig.clearZipConfig();
                                RLogFileProcessor.this.mConfig.initZipConfig();
                                RLogFileProcessor.this.isUploading = false;
                            }

                        }
                    });
                }

            }
        }
    }

    public static RLogFileProcessor init(RLogConfig pConfig) {
        return new RLogFileProcessor(pConfig);
    }

    public void write(String log) {
        this.mLogFileWriter.write(log);
    }

    public void upload() {
        IWriterOnStopListener iWriterOnStopListener = new IWriterOnStopListener() {
            public void onStopWrite(String filePath) {
                RLogFileProcessor.this.mLogFileWriter.close();
                long startTime = RLogFileProcessor.this.mConfig.getStartTime();
                long endTime = System.currentTimeMillis();
                String prefix = startTime + "_" + endTime;
                String logName = prefix + ".log";
                File file = new File(filePath);
                File renameFile = new File(file.getParent(), logName);
                boolean renameResult = file.renameTo(renameFile);
                if (renameResult) {
                    RLogFileProcessor.this.mCompressor.compress(renameFile.getAbsolutePath(), (new File(file.getParent(), prefix + ".gz")).getAbsolutePath(), new ICompressListener() {
                        public void onCompressFinish(boolean result, String sourceFile, String targetFile) {
                            RLogFileProcessor.this.processCompressFinish(result, sourceFile, targetFile, true);
                        }
                    });
                }

            }
        };
        this.mLogFileWriter.stopWrite(iWriterOnStopListener);
    }
}
