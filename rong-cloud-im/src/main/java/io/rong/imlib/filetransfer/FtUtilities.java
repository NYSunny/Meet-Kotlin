//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import android.text.TextUtils;
import io.rong.imlib.filetransfer.FtConst.MimeType;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FtUtilities {
    public FtUtilities() {
    }

    public static MimeType getMimeType(String fileName) {
        MimeType mimeType;
        if (isImageFile(fileName)) {
            mimeType = MimeType.FILE_IMAGE;
        } else if (isAudioFile(fileName)) {
            mimeType = MimeType.FILE_AUDIO;
        } else if (isVideoFile(fileName)) {
            mimeType = MimeType.FILE_VIDEO;
        } else if (isHtmlFile(fileName)) {
            mimeType = MimeType.FILE_HTML;
        } else {
            mimeType = MimeType.FILE_TEXT_PLAIN;
        }

        return mimeType;
    }

    private static boolean isImageFile(String fileName) {
        String[] imageSuffixArray = new String[]{".bmp", ".cod", ".gif", ".ief", ".jpe", ".jpeg", ".jpg", ".jfif", ".svg", ".tif", ".tiff", ".ras", ".ico", ".pnm", ".pbm", ".pgm", ".ppm", ".xbm", ".xpm", ".xwd", ".rgb"};
        String[] var2 = imageSuffixArray;
        int var3 = imageSuffixArray.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String suffix = var2[var4];
            if (fileName.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAudioFile(String fileName) {
        String[] audioSuffixArray = new String[]{".au", ".snd", ".mid", ".rmi", ".aif", ".aifc", ".aiff", ".m3u", ".ra", ".ram", ".wav", ".aac"};
        String[] var2 = audioSuffixArray;
        int var3 = audioSuffixArray.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String suffix = var2[var4];
            if (fileName.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isVideoFile(String fileName) {
        String[] videoSuffixArray = new String[]{".rmvb", ".avi", ".mp4", ".mp2", ".mpa", ".mpe", ".mpeg", ".mpg", ".mpv2", ".mov", ".qt", ".lsf", ".lsx", ".asf", ".asr", ".asx", ".avi", ".movie"};
        String[] var2 = videoSuffixArray;
        int var3 = videoSuffixArray.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String suffix = var2[var4];
            if (fileName.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isHtmlFile(String fileName) {
        String[] htmlSuffixArray = new String[]{".html"};
        String[] var2 = htmlSuffixArray;
        int var3 = htmlSuffixArray.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String suffix = var2[var4];
            if (fileName.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    public static String generateKey(String mimeType) {
        String szKey = mimeType + "__RC-";
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date mDate = new Date(time);
        szKey = szKey + format.format(mDate);
        int random = (int)(Math.random() * 1000.0D) % 10000;
        szKey = szKey + "_" + random;
        szKey = szKey + "_" + time;
        return szKey;
    }

    public static String getMediaDir(int fileType) {
        String mediaPath = "image";
        switch(fileType) {
            case 1:
                mediaPath = "image";
                break;
            case 2:
                mediaPath = "audio";
                break;
            case 3:
                mediaPath = "video";
                break;
            case 4:
                mediaPath = "file";
        }

        return mediaPath;
    }

    public static String getCateDir(int categoryId) {
        String categoryPath = "private";
        switch(categoryId) {
            case 1:
                categoryPath = "private";
                break;
            case 2:
                categoryPath = "discussion";
                break;
            case 3:
                categoryPath = "group";
                break;
            case 4:
                categoryPath = "chatroom";
                break;
            case 5:
                categoryPath = "reception";
        }

        return categoryPath;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static String getFileKey(String fileUri) {
        String fileKey = fileUri;
        int pos = fileUri.indexOf("?");
        if (pos != -1) {
            fileKey = fileUri.substring(0, pos);
        }

        pos = fileKey.lastIndexOf("/");
        if (pos != -1) {
            fileKey = fileKey.substring(pos + 1);
        }

        fileKey = fileKey.replaceAll("%2F", "_");
        return fileKey;
    }

    public static String getFileName(String cachePath, String fileName) {
        String suffix = "";
        if (fileName == null) {
            return "";
        } else {
            String name;
            if (fileName.lastIndexOf(".") > 0) {
                suffix = fileName.substring(fileName.lastIndexOf("."));
                name = fileName.substring(0, fileName.lastIndexOf("."));
            } else {
                name = fileName;
            }

            String regEx = "[`!@#$%^&*()+=|{}':;',//[//].<>/?！@#￥%……&*（）——+|{}【】‘；：”“’。，、？~~]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(name);
            name = matcher.replaceAll("_");
            File file = new File(cachePath, name + suffix);

            String increaseName;
            for(int n = 0; file.exists(); file = new File(cachePath, increaseName)) {
                ++n;
                if (!TextUtils.isEmpty(suffix)) {
                    increaseName = name + "(" + n + ")" + suffix;
                } else {
                    increaseName = name + "(" + n + ")";
                }
            }

            return file.getPath();
        }
    }
}
