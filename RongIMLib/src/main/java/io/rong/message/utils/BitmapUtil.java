//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmapUtil {
    private static final String TAG = "Util";
    private static final int ASPECT_RATIO = 2;

    public BitmapUtil() {
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        String base64Str = null;
        ByteArrayOutputStream baos = null;

        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 40, baos);
                byte[] bitmapBytes = baos.toByteArray();
                base64Str = Base64.encodeToString(bitmapBytes, 2);
                RLog.d("base64Str", "" + base64Str.length());
                baos.flush();
                baos.close();
            }
        } catch (IOException var12) {
            RLog.e("Util", "IOException ", var12);
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException var11) {
                RLog.e("Util", "IOException ", var11);
            }

        }

        return base64Str;
    }

    public static Bitmap getBitmapFromBase64(String base64Str) {
        if (TextUtils.isEmpty(base64Str)) {
            return null;
        } else {
            byte[] bytes = Base64.decode(base64Str, 2);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }

    public static Bitmap getResizedBitmap(Context context, Uri uri, int widthLimit, int heightLimit) throws IOException {
        String path;
        if ("file".equals(uri.getScheme())) {
            path = uri.toString().substring(5);
        } else {
            if (!"content".equals(uri.getScheme())) {
                return null;
            }

            Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, (String)null, (String[])null, (String)null);
            if (cursor == null) {
                RLog.e("Util", "cursor is null");
                return null;
            }

            cursor.moveToFirst();
            path = cursor.getString(0);
            cursor.close();
        }

        ExifInterface exifInterface = new ExifInterface(path);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int orientation = exifInterface.getAttributeInt("Orientation", 0);
        int width;
        if (orientation == 6 || orientation == 8 || orientation == 5 || orientation == 7) {
            width = widthLimit;
            widthLimit = heightLimit;
            heightLimit = width;
        }

        width = options.outWidth;
        int height = options.outHeight;
        int sampleW = 1;

        int sampleH;
        for(sampleH = 1; width / 2 > widthLimit; sampleW <<= 1) {
            width /= 2;
        }

        while(height / 2 > heightLimit) {
            height /= 2;
            sampleH <<= 1;
        }

        options = new Options();
        int sampleSize;
        if (widthLimit != 2147483647 && heightLimit != 2147483647) {
            sampleSize = Math.max(sampleW, sampleH);
        } else {
            sampleSize = Math.max(sampleW, sampleH);
        }

        options.inSampleSize = sampleSize;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError var22) {
            RLog.e("Util", "OutOfMemoryError ", var22);
            options.inSampleSize <<= 1;
            bitmap = BitmapFactory.decodeFile(path, options);
        }

        Matrix matrix = new Matrix();
        if (bitmap == null) {
            return null;
        } else {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (orientation == 6 || orientation == 8 || orientation == 5 || orientation == 7) {
                int tmp = w;
                w = h;
                h = tmp;
            }

            switch(orientation) {
                case 2:
                    matrix.preScale(-1.0F, 1.0F);
                    break;
                case 3:
                    matrix.setRotate(180.0F, (float)w / 2.0F, (float)h / 2.0F);
                    break;
                case 4:
                    matrix.preScale(1.0F, -1.0F);
                    break;
                case 5:
                    matrix.setRotate(90.0F, (float)w / 2.0F, (float)h / 2.0F);
                    matrix.preScale(1.0F, -1.0F);
                    break;
                case 6:
                    matrix.setRotate(90.0F, (float)w / 2.0F, (float)h / 2.0F);
                    break;
                case 7:
                    matrix.setRotate(270.0F, (float)w / 2.0F, (float)h / 2.0F);
                    matrix.preScale(1.0F, -1.0F);
                    break;
                case 8:
                    matrix.setRotate(270.0F, (float)w / 2.0F, (float)h / 2.0F);
            }

            float xS = (float)widthLimit / (float)bitmap.getWidth();
            float yS = (float)heightLimit / (float)bitmap.getHeight();
            matrix.postScale(Math.min(xS, yS), Math.min(xS, yS));

            try {
                Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return result;
            } catch (OutOfMemoryError var21) {
                RLog.e("Util", "OutOfMemoryError ", var21);
                RLog.d("ResourceCompressHandler", "OOMHeight:" + bitmap.getHeight() + "Width:" + bitmap.getHeight() + "matrix:" + xS + " " + yS);
                return null;
            }
        }
    }

    public static Bitmap getNewResizedBitmap(Context context, @NonNull Uri uri, int limit) throws IOException {
        float standFloatValue = (float)limit * 1.0F;
        ExifInterface exifInterface = getExifInterfaceFromUri(context, uri);
        if (exifInterface == null) {
            RLog.e("Util", "getNewResizedBitmap exifInterface is null");
            return null;
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            getFactoryBitmap(context, uri, options);
            int orientation = exifInterface.getAttributeInt("Orientation", 0);
            options = new Options();
            int sampleSize = calculateInSampleSize(options, limit, limit);
            options.inSampleSize = sampleSize;

            Bitmap bitmap;
            try {
                bitmap = getFactoryBitmap(context, uri, options);
            } catch (OutOfMemoryError var19) {
                RLog.e("Util", "OutOfMemoryError ", var19);
                options.inSampleSize <<= 1;
                bitmap = getFactoryBitmap(context, uri, options);
            }

            Matrix matrix = new Matrix();
            if (bitmap == null) {
                return null;
            } else {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int widthCompress;
                int heightCompress;
                float xS;
                if (w <= limit && h <= limit) {
                    widthCompress = w;
                    heightCompress = h;
                } else if (w >= limit && h >= limit) {
                    widthCompress = limit;
                    heightCompress = limit;
                    xS = w >= h ? 1.0F * (float)w / (float)h : 1.0F * (float)h / (float)w;
                    if (xS <= 2.0F) {
                        if (w <= h) {
                            widthCompress = (int)((float)w * standFloatValue / (float)h);
                        } else {
                            heightCompress = (int)((float)h * standFloatValue / (float)w);
                        }
                    } else if (w >= h) {
                        widthCompress = (int)((float)w * standFloatValue / (float)h);
                    } else {
                        heightCompress = (int)((float)h * standFloatValue / (float)w);
                    }
                } else if ((w <= limit || h >= limit) && (w >= limit || h <= limit)) {
                    widthCompress = w;
                    heightCompress = h;
                } else {
                    widthCompress = limit;
                    heightCompress = limit;
                    xS = w >= h ? 1.0F * (float)w / (float)h : 1.0F * (float)h / (float)w;
                    if (xS <= 2.0F) {
                        if (w <= h) {
                            widthCompress = (int)((float)w * standFloatValue / (float)h);
                        } else {
                            heightCompress = (int)((float)h * standFloatValue / (float)w);
                        }
                    } else {
                        widthCompress = w;
                        heightCompress = h;
                    }
                }

                switch(orientation) {
                    case 2:
                        matrix.preScale(-1.0F, 1.0F);
                        break;
                    case 3:
                        matrix.setRotate(180.0F, (float)w / 2.0F, (float)h / 2.0F);
                        break;
                    case 4:
                        matrix.preScale(1.0F, -1.0F);
                        break;
                    case 5:
                        matrix.setRotate(90.0F, (float)w / 2.0F, (float)h / 2.0F);
                        matrix.preScale(1.0F, -1.0F);
                        break;
                    case 6:
                        matrix.setRotate(90.0F, (float)w / 2.0F, (float)h / 2.0F);
                        break;
                    case 7:
                        matrix.setRotate(270.0F, (float)w / 2.0F, (float)h / 2.0F);
                        matrix.preScale(1.0F, -1.0F);
                        break;
                    case 8:
                        matrix.setRotate(270.0F, (float)w / 2.0F, (float)h / 2.0F);
                }

                xS = (float)widthCompress / (float)bitmap.getWidth();
                float yS = (float)heightCompress / (float)bitmap.getHeight();
                matrix.postScale(Math.min(xS, yS), Math.min(xS, yS));

                try {
                    Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    return result;
                } catch (OutOfMemoryError var18) {
                    RLog.e("Util", "OutOfMemoryError ", var18);
                    RLog.d("ResourceCompressHandler", "OOMHeight:" + bitmap.getHeight() + "Width:" + bitmap.getHeight() + "matrix:" + xS + " " + yS);
                    return null;
                }
            }
        }
    }

    public static Bitmap interceptBitmap(String filePath, int w, int h) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        int xTopLeft = (widthOrg - w) / 2;
        int yTopLeft = (heightOrg - h) / 2;
        if (xTopLeft > 0 && yTopLeft > 0) {
            try {
                Bitmap result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, w, h);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                return result;
            } catch (OutOfMemoryError var9) {
                return null;
            }
        } else {
            RLog.w("Util", "ignore intercept [" + widthOrg + ", " + heightOrg + ":" + w + ", " + h + "]");
            return bitmap;
        }
    }

    private static ExifInterface getExifInterfaceFromUri(Context context, Uri uri) throws IOException {
        if (FileUtils.uriStartWithFile(uri)) {
            String path = uri.toString().substring(7);
            return new ExifInterface(path);
        } else if (FileUtils.uriStartWithContent(uri)) {
            if (VERSION.SDK_INT >= 24) {
                ParcelFileDescriptor r = context.getContentResolver().openFileDescriptor(uri, "r");
                return new ExifInterface(new FileInputStream(r.getFileDescriptor()));
            } else {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, (String)null, (String[])null, (String)null);
                if (cursor == null) {
                    RLog.e("Util", "cursor is null");
                    return null;
                } else {
                    cursor.moveToFirst();
                    String path = cursor.getString(0);
                    cursor.close();
                    return new ExifInterface(path);
                }
            }
        } else {
            return null;
        }
    }

    public static Bitmap getFactoryBitmap(Context context, Uri uri, Options options) {
        if (FileUtils.uriStartWithFile(uri)) {
            String path = uri.toString().substring(7);
            return BitmapFactory.decodeFile(path, options);
        } else if (FileUtils.uriStartWithContent(uri)) {
            ParcelFileDescriptor r = null;

            try {
                r = context.getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException var5) {
                return null;
            }

            return BitmapFactory.decodeFileDescriptor(r.getFileDescriptor(), (Rect)null, options);
        } else {
            return null;
        }
    }

    public static Bitmap getFactoryBitmap(Context context, Uri uri) {
        return getFactoryBitmap(context, uri, (Options)null);
    }

    public static Bitmap getThumbBitmap(Context context, @NonNull Uri uri, int sizeLimit, int minSize) throws IOException {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        ExifInterface exifInterface = getExifInterfaceFromUri(context, uri);
        if (exifInterface == null) {
            RLog.e("Util", "getThumbBitmap exifInterface is null");
            return null;
        } else {
            getFactoryBitmap(context, uri, options);
            int orientation = exifInterface.getAttributeInt("Orientation", 0);
            int width = options.outWidth;
            int height = options.outHeight;
            int longSide = width > height ? width : height;
            int shortSide = width > height ? height : width;
            float scale = (float)longSide / (float)shortSide;
            int sampleW = 1;
            int sampleH = 1;
            int sampleSize = 1;
            if (scale > (float)sizeLimit / (float)minSize) {
                while(shortSide / 2 > minSize) {
                    shortSide /= 2;
                    sampleSize <<= 1;
                }

                options = new Options();
                options.inSampleSize = sampleSize;
            } else {
                while(width / 2 > sizeLimit) {
                    width /= 2;
                    sampleW <<= 1;
                }

                while(height / 2 > sizeLimit) {
                    height /= 2;
                    sampleH <<= 1;
                }

                options = new Options();
                sampleSize = Math.max(sampleW, sampleH);
                options.inSampleSize = sampleSize;
            }

            Bitmap bitmap;
            try {
                bitmap = getFactoryBitmap(context, uri, options);
            } catch (OutOfMemoryError var26) {
                RLog.e("Util", "OutOfMemoryError ", var26);
                options.inSampleSize <<= 1;
                bitmap = getFactoryBitmap(context, uri, options);
            }

            Matrix matrix = new Matrix();
            if (bitmap == null) {
                return null;
            } else {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                if (orientation == 6 || orientation == 8 || orientation == 5 || orientation == 7) {
                    int tmp = w;
                    w = h;
                    h = tmp;
                }

                switch(orientation) {
                    case 2:
                        matrix.preScale(-1.0F, 1.0F);
                        break;
                    case 3:
                        matrix.setRotate(180.0F, (float)w / 2.0F, (float)h / 2.0F);
                        break;
                    case 4:
                        matrix.preScale(1.0F, -1.0F);
                        break;
                    case 5:
                        matrix.setRotate(90.0F, (float)w / 2.0F, (float)h / 2.0F);
                        matrix.preScale(1.0F, -1.0F);
                        break;
                    case 6:
                        matrix.setRotate(90.0F, (float)w / 2.0F, (float)h / 2.0F);
                        break;
                    case 7:
                        matrix.setRotate(270.0F, (float)w / 2.0F, (float)h / 2.0F);
                        matrix.preScale(1.0F, -1.0F);
                        break;
                    case 8:
                        matrix.setRotate(270.0F, (float)w / 2.0F, (float)h / 2.0F);
                }

                float sS = 0.0F;
                float xS = 0.0F;
                float yS = 0.0F;
                if (scale > (float)sizeLimit / (float)minSize) {
                    shortSide = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();
                    sS = (float)minSize / (float)shortSide;
                    matrix.postScale(sS, sS);
                } else {
                    xS = (float)sizeLimit / (float)bitmap.getWidth();
                    yS = (float)sizeLimit / (float)bitmap.getHeight();
                    matrix.postScale(Math.min(xS, yS), Math.min(xS, yS));
                }

                int x = 0;
                int y = 0;

                Bitmap result;
                try {
                    if (scale > (float)sizeLimit / (float)minSize) {
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            h = bitmap.getHeight();
                            w = h * sizeLimit / minSize;
                            x = (bitmap.getWidth() - w) / 2;
                            y = 0;
                        } else {
                            w = bitmap.getWidth();
                            h = w * sizeLimit / minSize;
                            x = 0;
                            y = (bitmap.getHeight() - h) / 2;
                        }
                    } else {
                        w = bitmap.getWidth();
                        h = bitmap.getHeight();
                    }

                    result = Bitmap.createBitmap(bitmap, x, y, w, h, matrix, true);
                } catch (OutOfMemoryError var27) {
                    RLog.e("Util", "OutOfMemoryError ", var27);
                    RLog.d("ResourceCompressHandler", "OOMHeight:" + bitmap.getHeight() + "Width:" + bitmap.getHeight() + "matrix:" + sS + " " + xS + " " + yS);
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }

                    return null;
                }

                if (!bitmap.isRecycled() && !bitmap.equals(result)) {
                    bitmap.recycle();
                }

                return result;
            }
        }
    }

    @RequiresApi(
            api = 17
    )
    public static Bitmap getBlurryBitmap(Context context, Bitmap bitmap, float radius, float scale) {
        RenderScript renderScript = RenderScript.create(context);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(scale * (float)bitmap.getWidth()), (int)(scale * (float)bitmap.getHeight()), false);
        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }

    private static int calculateInSampleSize(@NonNull Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float)height / (float)reqHeight);
            int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
