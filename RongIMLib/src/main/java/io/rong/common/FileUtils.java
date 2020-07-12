package io.rong.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import io.rong.imlib.NativeClient;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.common.SavePathUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtils {
    private static String TAG = "FileUtils";
    public static final int FILE_SCHEME_LENGTH = 7;

    public FileUtils() {
    }

    public static InputStream getFileInputStream(String path) {
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException var3) {
            RLog.e(TAG, "getFileInputStream", var3);
        }

        return fileInputStream;
    }

    public static byte[] getByteFromUri(Uri uri) {
        if (uri == null) {
            RLog.e(TAG, "getByteFromUri uri should not be null!");
            return null;
        } else {
            InputStream input = getFileInputStream(uri.getPath());

            Object var3;
            try {
                int count = 0;

                while(true) {
                    if (count == 0) {
                        count = input.available();
                        if (count != 0) {
                            continue;
                        }
                    }

                    byte[] bytes = new byte[count];
                    int byteCount = input.read(bytes);
                    RLog.i(TAG, "byteCount = " + byteCount);
                    byte[] var5 = bytes;
                    return var5;
                }
            } catch (Exception var15) {
                var3 = null;
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException var14) {
                        RLog.e(TAG, "getByteFromUri", var14);
                    }
                }

            }

            return (byte[])var3;
        }
    }

    public static void writeByte(Uri uri, byte[] data) {
        if (uri != null && uri.getPath() != null && uri.getPath().lastIndexOf("/") != -1) {
            File fileFolder = new File(uri.getPath().substring(0, uri.getPath().lastIndexOf("/")));
            boolean successMkdir = fileFolder.mkdirs();
            if (!successMkdir) {
                RLog.e(TAG, "Created folders unSuccessfully");
            }

            File file = new File(uri.getPath());

            try {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                os.write(data);
                os.close();
            } catch (IOException var6) {
                RLog.e(TAG, "writeByte", var6);
            }

        }
    }

    public static File convertBitmap2File(Bitmap bm, String dir, String name) {
        if (bm != null && !TextUtils.isEmpty(dir)) {
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                RLog.e(TAG, "convertBitmap2File: dir does not exist! -" + dirFile.getAbsolutePath());
                boolean successMkdir = dirFile.mkdirs();
                if (!successMkdir) {
                    RLog.e(TAG, "Created folders unSuccessfully");
                }
            }

            File targetFile = new File(dirFile.getPath() + File.separator + name);
            if (targetFile.exists()) {
                boolean isDelete = targetFile.delete();
                RLog.e(TAG, "convertBitmap2File targetFile isDelete:" + isDelete);
            }

            File tmpFile = new File(dirFile.getPath() + File.separator + name + ".tmp");

            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                bm.compress(CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException var7) {
                RLog.e(TAG, "convertBitmap2File: Exception!", var7);
            }

            targetFile = new File(dirFile.getPath() + File.separator + name);
            return tmpFile.renameTo(targetFile) ? targetFile : tmpFile;
        } else {
            RLog.e(TAG, "convertBitmap2File bm or dir should not be null!");
            return null;
        }
    }

    public static File copyFile(File src, String path, String name) {
        if (src == null) {
            RLog.e(TAG, "copyFile src should not be null!");
            return null;
        } else if (!src.exists()) {
            RLog.e(TAG, "copyFile: src file does not exist! -" + src.getAbsolutePath());
            return null;
        } else {
            File dest = new File(path);
            if (!dest.exists()) {
                RLog.d(TAG, "copyFile: dir does not exist!");
                boolean successMkdir = dest.mkdirs();
                if (!successMkdir) {
                    RLog.e(TAG, "Created folders unSuccessfully");
                }
            }

            dest = new File(path + name);

            try {
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];

                int length;
                while((length = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }

                fos.flush();
                fos.close();
                fis.close();
                return dest;
            } catch (IOException var8) {
                RLog.e(TAG, "copyFile: Exception!", var8);
                return dest;
            }
        }
    }

    public static boolean copyFile(String srcPath, String path, String name) {
        if (TextUtils.isEmpty(srcPath)) {
            RLog.e(TAG, "copyFile src should not be null!");
            return false;
        } else {
            File src = new File(srcPath);
            if (!src.exists()) {
                RLog.e(TAG, "copyFile: src file does not exist! -" + src.getAbsolutePath());
                return false;
            } else {
                File dest = new File(path);
                if (!dest.exists()) {
                    RLog.d(TAG, "copyFile: dir does not exist!");
                    boolean successMkdir = dest.mkdirs();
                    if (!successMkdir) {
                        RLog.e(TAG, "Created folders unSuccessfully");
                    }
                }

                dest = new File(path, name);
                FileInputStream fis = null;
                FileOutputStream fos = null;

                boolean var8;
                try {
                    fis = new FileInputStream(src);
                    fos = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];

                    int length;
                    while((length = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, length);
                    }

                    fos.flush();
                    return true;
                } catch (IOException var22) {
                    RLog.e(TAG, "copyFile: Exception!", var22);
                    var8 = false;
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException var21) {
                        RLog.e(TAG, "copyFile fos close", var21);
                    }

                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException var20) {
                        RLog.e(TAG, "copyFile fis close", var20);
                    }

                }

                return var8;
            }
        }
    }

    public static boolean copyFileToInternal(Context context, Uri srcUri, String desPath, String name) {
        if (uriStartWithFile(srcUri)) {
            return copyFile(srcUri.toString(), desPath, name);
        } else {
            return uriStartWithContent(srcUri) ? copyFile(context, srcUri, (new File(desPath, name)).getAbsolutePath()) : false;
        }
    }

    public static boolean copyFile(Context context, Uri srcUri, String desPath) {
        if (!uriStartWithContent(srcUri)) {
            return false;
        } else {
            ParcelFileDescriptor pfd;
            try {
                pfd = context.getContentResolver().openFileDescriptor(srcUri, "r");
            } catch (FileNotFoundException var22) {
                RLog.e(TAG, "copyFile srcUri is error uri is " + srcUri.toString());
                return false;
            }

            if (pfd != null) {
                FileInputStream fis = null;
                FileOutputStream fos = null;

                boolean var7;
                try {
                    fis = new FileInputStream(pfd.getFileDescriptor());
                    fos = new FileOutputStream(desPath);
                    byte[] buffer = new byte[1024];

                    int length;
                    while((length = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, length);
                    }

                    fos.flush();
                    return true;
                } catch (IOException var23) {
                    RLog.e(TAG, "copyFile: Exception!", var23);
                    var7 = false;
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException var21) {
                            RLog.e(TAG, "copyFile: Exception!", var21);
                        }
                    }

                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException var20) {
                            RLog.e(TAG, "copyFile: Exception!", var20);
                        }
                    }

                }

                return var7;
            } else {
                return true;
            }
        }
    }

    public static String getFileNameWithPath(String path) {
        if (TextUtils.isEmpty(path)) {
            RLog.e(TAG, "getFileNameWithPath path should not be null!");
            return null;
        } else {
            int start = path.lastIndexOf("/");
            return start != -1 ? path.substring(start + 1) : null;
        }
    }

    public static byte[] file2byte(File file) {
        if (file == null) {
            RLog.e(TAG, "file2byte file should not be null!");
            return null;
        } else if (!file.exists()) {
            RLog.e(TAG, "file2byte: src file does not exist! -" + file.getAbsolutePath());
            return null;
        } else {
            byte[] buffer = null;

            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];

                int n;
                while((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }

                fis.close();
                bos.close();
                buffer = bos.toByteArray();
            } catch (Exception var6) {
                RLog.e(TAG, "file2byte: Exception!", var6);
            }

            return buffer;
        }
    }

    public static byte[] file2byte(Context context, Uri uri) {
        if (uriStartWithFile(uri)) {
            String path = uri.toString().substring(7);
            return file2byte(new File(path));
        } else {
            return uriStartWithContent(uri) ? contentFile2byte(context, uri) : null;
        }
    }

    public static byte[] contentFile2byte(Context context, Uri uri) {
        if (context != null && uri != null && uriStartWithContent(uri)) {
            ParcelFileDescriptor r;
            try {
                r = context.getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException var17) {
                RLog.e(TAG, "contentFile2byte file not found uri is " + uri.toString());
                return null;
            }

            byte[] buffer = null;
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(r.getFileDescriptor());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];

                int n;
                while((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }

                bos.close();
                buffer = bos.toByteArray();
            } catch (Exception var18) {
                RLog.e(TAG, "file2byte: Exception!", var18);
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException var16) {
                    RLog.e(TAG, "file2byte fis close", var16);
                }

            }

            return buffer;
        } else {
            RLog.e(TAG, "contentFile2byte params error");
            return null;
        }
    }

    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                RLog.d(TAG, "byte2File: dir does not exist!");
                boolean successMkdir = dir.mkdirs();
                if (!successMkdir) {
                    RLog.e(TAG, "Created folders unSuccessfully");
                }
            }

            file = new File(dir.getPath() + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception var20) {
            RLog.e(TAG, "byte2File: Exception!", var20);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var19) {
                    RLog.e(TAG, "byte2File: IOException!", var19);
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var18) {
                    RLog.e(TAG, "byte2File: IOException!", var18);
                }
            }

        }

        return file;
    }

    public static String getCachePath(Context context) {
        return getCachePath(context, "");
    }

    private static boolean hasFilePermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static String getCachePath(Context context, String dir) {
        if (!SavePathUtils.isSavePathEmpty()) {
            return SavePathUtils.getSavePath();
        } else {
            boolean sdCardExist = false;

            try {
                sdCardExist = "mounted".equals(Environment.getExternalStorageState());
            } catch (Exception var8) {
                RLog.e(TAG, "getCachePath", var8);
            }

            File cacheDir;
            if (VERSION.SDK_INT < 19 && !hasFilePermission(context)) {
                cacheDir = context.getCacheDir();
            } else {
                try {
                    cacheDir = context.getExternalCacheDir();
                } catch (ArrayIndexOutOfBoundsException var7) {
                    RLog.e(TAG, "getCachePath ArrayIndexOutOfBoundsException", var7);
                    cacheDir = null;
                }
            }

            if (!sdCardExist || cacheDir == null || !cacheDir.exists() && !cacheDir.mkdirs()) {
                cacheDir = context.getCacheDir();
            }

            File tarDir = new File(cacheDir.getPath() + File.separator + dir);
            boolean result;
            if (tarDir.exists() && tarDir.isFile()) {
                result = tarDir.delete();
                RLog.e(TAG, "getCachePath isDelete:" + result);
            }

            if (!tarDir.exists()) {
                result = tarDir.mkdir();
                RLog.w(TAG, "getCachePath = " + tarDir.getPath() + ", result = " + result);
                if (!result) {
                    if (VERSION.SDK_INT >= 23 && hasFilePermission(context)) {
                        tarDir = new File("/sdcard/cache/" + dir);
                        if (!tarDir.exists()) {
                            result = tarDir.mkdirs();
                        }
                    } else {
                        File filesDir = context.getFilesDir();
                        tarDir = new File(filesDir, dir);
                        if (!tarDir.exists()) {
                            result = tarDir.mkdirs();
                        }
                    }

                    RLog.e(TAG, "change path = " + tarDir.getPath() + ", result = " + result);
                }
            }

            return tarDir.getPath();
        }
    }

    public static String getCacheDirsPath(Context context, String dir) {
        if (!SavePathUtils.isSavePathEmpty()) {
            return SavePathUtils.getSavePath();
        } else {
            boolean sdCardExist = false;

            try {
                sdCardExist = "mounted".equals(Environment.getExternalStorageState());
            } catch (Exception var8) {
                RLog.e(TAG, "getCacheDirsPath ", var8);
            }

            File cacheDir;
            if (VERSION.SDK_INT < 19 && !hasFilePermission(context)) {
                cacheDir = context.getCacheDir();
            } else {
                try {
                    cacheDir = context.getExternalCacheDir();
                } catch (ArrayIndexOutOfBoundsException var7) {
                    RLog.e(TAG, "getCacheDirsPath ArrayIndexOutOfBoundsException", var7);
                    cacheDir = null;
                }
            }

            if (!sdCardExist || cacheDir == null || !cacheDir.exists() && !cacheDir.mkdirs()) {
                cacheDir = context.getCacheDir();
            }

            File tarDir = new File(cacheDir.getPath() + File.separator + dir);
            boolean result;
            if (tarDir.exists()) {
                if (tarDir.isFile()) {
                    result = tarDir.delete();
                    RLog.e(TAG, "getCacheDirsPath isDelete:" + result);
                }
            } else {
                result = tarDir.mkdirs();
                if (!result) {
                    RLog.e(TAG, "getCacheDirsPath created folders unSuccessfully");
                }
            }

            if (!tarDir.exists()) {
                result = tarDir.mkdir();
                RLog.w(TAG, "getCachePath = " + tarDir.getPath() + ", result = " + result);
                if (!result) {
                    if (VERSION.SDK_INT >= 23 && hasFilePermission(context)) {
                        tarDir = new File("/sdcard/cache/" + dir);
                        if (!tarDir.exists()) {
                            result = tarDir.mkdirs();
                        }
                    } else {
                        File filesDir = context.getFilesDir();
                        tarDir = new File(filesDir, dir);
                        if (!tarDir.exists()) {
                            result = tarDir.mkdirs();
                        }
                    }

                    RLog.e(TAG, "change path = " + tarDir.getPath() + ", result = " + result);
                }
            }

            return tarDir.getPath();
        }
    }

    public static String getTempFilePath(Context context, int messageId) {
        return getTempFilePath(context, messageId + "");
    }

    public static String getTempFilePath(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences("Statistics", 0);
        String appKey = sp.getString("appKey", "Null");
        String userId;
        if (SystemUtils.getCurrentProcessName(context).equals(context.getPackageName())) {
            userId = sp.getString("userId", "Null");
        } else {
            userId = NativeClient.getInstance().getCurrentUserId();
        }

        String subDir = "TempFile" + File.separator + appKey + File.separator + userId;
        String path = getCacheDirsPath(context, subDir) + File.separator + id + ".txt";
        return path;
    }

    public static String getInternalCachePath(Context context, String dir) {
        if (!SavePathUtils.isSavePathEmpty()) {
            return SavePathUtils.getSavePath();
        } else {
            File cacheDir = new File(context.getCacheDir().getPath() + File.separator + dir);
            if (!cacheDir.exists()) {
                boolean result = cacheDir.mkdir();
                RLog.w(TAG, "getInternalCachePath = " + cacheDir.getPath() + ", result = " + result);
            }

            return cacheDir.getPath();
        }
    }

    /** @deprecated */
    @Deprecated
    public static String getMediaDownloadDir(Context context) {
        return LibStorageUtils.getMediaDownloadDir(context);
    }

    /** @deprecated */
    @Deprecated
    public static String getMediaDownloadDir(Context context, String dir) {
        return LibStorageUtils.getMediaDownloadDir(context, dir);
    }

    public static long getFileSize(File file) {
        long size = 0L;

        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                size = (long)fis.available();
                if (fis != null) {
                    fis.close();
                }
            } else {
                RLog.d(TAG, "file doesn't exist");
            }
        } catch (Exception var4) {
            RLog.e(TAG, "getFileSize", var4);
        }

        return size;
    }

    public static void saveFile(String str, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                boolean successMkdir = dir.mkdirs();
                if (!successMkdir) {
                    RLog.e(TAG, "Created folders unSuccessfully");
                }

                boolean isCreateNewFile = file.createNewFile();
                RLog.e(TAG, "saveFile isCreateNewFile" + isCreateNewFile);
            }

            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception var6) {
            RLog.e(TAG, "saveFile", var6);
        }

    }

    public static String getStringFromFile(String path) {
        if (TextUtils.isEmpty(path)) {
            RLog.e(TAG, "getStringFromFile path should not be null!");
            return null;
        } else {
            File file = new File(path);
            if (!file.exists()) {
                RLog.e(TAG, "getStringFromFile file is not exists,path:" + path);
                return "";
            } else {
                BufferedReader reader = null;
                StringBuilder content = new StringBuilder();

                try {
                    FileInputStream in = new FileInputStream(path);
                    reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                } catch (IOException var14) {
                    RLog.e(TAG, "getStringFromFile IOException", var14);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException var13) {
                            RLog.e(TAG, "getStringFromFile IOException", var13);
                        }
                    }

                }

                return content.toString();
            }
        }
    }

    public static void removeFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                boolean isDelete = file.delete();
                RLog.e(TAG, "removeFile isDelete:" + isDelete);
            }
        } catch (Exception var3) {
            RLog.e(TAG, "removeFile Exception", var3);
        }

    }

    public static String getTempFileMD5(Context context, int messageId) {
        return getTempFileMD5(context, messageId + "");
    }

    public static String getTempFileMD5(Context context, String tag) {
        if (context == null) {
            return null;
        } else {
            SharedPreferences sp = context.getSharedPreferences("Statistics", 0);
            String appKey = sp.getString("appKey", "Null");
            String userId;
            if (SystemUtils.getCurrentProcessName(context).equals(context.getPackageName())) {
                userId = sp.getString("userId", "Null");
            } else {
                userId = NativeClient.getInstance().getCurrentUserId();
            }

            String result = DeviceUtils.ShortMD5(0, new String[]{appKey, userId, tag});
            return result;
        }
    }

    public static int readPictureDegree(Context context, String path) {
        short degree = 0;

        try {
            ExifInterface exifInterface = null;
            if (LibStorageUtils.isBuildAndTargetForQ(context)) {
                if (uriStartWithContent(Uri.parse(path))) {
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(path), "r");
                    if (pfd != null && VERSION.SDK_INT >= 24) {
                        exifInterface = new ExifInterface(pfd.getFileDescriptor());
                    }
                } else {
                    exifInterface = new ExifInterface(path);
                }
            } else {
                exifInterface = new ExifInterface(path);
            }

            if (exifInterface == null) {
                return 0;
            }

            int orientation = exifInterface.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    degree = 180;
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (Exception var5) {
            RLog.e(TAG, "readPictureDegree error");
        }

        return degree;
    }

    public static boolean isFileExistsWithUri(Context pContext, Uri pUri) {
        if (pUri == null) {
            return false;
        } else if (uriStartWithFile(pUri)) {
            String subPath = pUri.toString().substring(7);
            return (new File(subPath)).exists();
        } else if (uriStartWithContent(pUri)) {
            try {
                return pContext.getContentResolver().openFileDescriptor(pUri, "r") != null;
            } catch (FileNotFoundException var3) {
                return false;
            }
        } else {
            return pUri.toString().startsWith("/") ? (new File(pUri.toString())).exists() : false;
        }
    }

    public static boolean isValidateLocalUri(Uri pUri) {
        return uriStartWithFile(pUri) || uriStartWithContent(pUri);
    }

    public static boolean uriStartWithFile(Uri pUri) {
        return pUri != null && "file".equals(pUri.getScheme()) && pUri.toString().length() > 7;
    }

    public static boolean uriStartWithContent(Uri srcUri) {
        return srcUri != null && "content".equals(srcUri.getScheme());
    }

    public static FileInfo getFileInfoByUri(Context context, Uri uri) {
        if (uriStartWithContent(uri)) {
            return getFileInfoByContent(context, uri);
        } else {
            return uriStartWithFile(uri) ? getFileInfoByFile(uri) : null;
        }
    }

    private static FileInfo getFileInfoByFile(Uri uri) {
        if (uriStartWithFile(uri)) {
            String filePath = uri.toString().substring(7);
            File file = new File(filePath);
            if (file.exists()) {
                FileInfo fileInfo = new FileInfo();
                String name = file.getName();
                fileInfo.setSize(file.length());
                fileInfo.setName(name);
                int lastDotIndex = name.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    String fileSuffix = file.getName().substring(lastDotIndex + 1);
                    fileInfo.setType(fileSuffix);
                }

                return fileInfo;
            } else {
                return null;
            }
        } else {
            RLog.e(TAG, "getDocumentByFile uri is not file");
            return null;
        }
    }

    private static FileInfo getFileInfoByContent(Context context, Uri uri) {
        if (uriStartWithContent(uri)) {
            String[] projection = new String[]{"_display_name", "_size", "mime_type"};
            Cursor cursor = null;

            String name;
            try {
                FileInfo fileInfo = new FileInfo();
                cursor = context.getContentResolver().query(uri, projection, (String)null, (String[])null, (String)null);
                if (cursor == null) {
                    RLog.e(TAG, "getFileInfoByContent cursor is null");
                    return null;
                }

                if (!cursor.moveToFirst()) {
                    return null;
                }

                name = cursor.getString(0);
                long size = cursor.getLong(1);
                fileInfo.setName(name);
                fileInfo.setSize(size);
                FileInfo var15;
                if (TextUtils.isEmpty(name)) {
                    RLog.e(TAG, "getFileInfoByContent getName is empty");
                    var15 = null;
                    return var15;
                }

                int lastDotIndex = name.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    String fileSuffix = name.substring(lastDotIndex + 1);
                    fileInfo.setType(fileSuffix);
                }

                if (cursor.moveToNext()) {
                    RLog.e(TAG, "uri is error,cursor has second value,uri is" + uri);
                }

                var15 = fileInfo;
                return var15;
            } catch (Exception var13) {
                RLog.e(TAG, "getDocumentByContent is error", var13);
                name = null;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }

            return null;
        } else {
            RLog.e(TAG, "getDocumentByContent uri is not content");
            return null;
        }
    }
}
