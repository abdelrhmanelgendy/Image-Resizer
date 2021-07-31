package com.example.imagesresizer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Resizer {

    private static Context context;

    public static void setContext(Context context) {
        Resizer.context = context;
    }

    private static File createFileDirec() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +"/"+ context.getResources().getString(R.string.app_name));
        boolean mkdir = file.mkdir();
        return file;
    }


    private static String saveBitMab(Bitmap bitmap) {
        File fileDirec = createFileDirec();
        long timeInMillis = System.currentTimeMillis();
        String filePath = fileDirec + "/" + timeInMillis + ".PNG";
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return filePath;
    }

    public static String reduceBitmab(Uri uri) {
        Bitmap bitmapByUri = getBitmapByUri(uri);

        Bitmap newBitMab = null;
        AssetFileDescriptor file = null;
        try {
            file = context.getContentResolver().openAssetFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file != null) {



            //* you can change these  MAX_SIZE values to meet your System requirements


            long sizeInKB = file.getLength() / 1024;
            if (sizeInKB <= 250) {
                newBitMab = bitmapByUri;
            } else if (sizeInKB > 250 && sizeInKB <= 500) {
                newBitMab = reduceBitmapSize(bitmapByUri, 150000);
            }

            else if (sizeInKB > 500&&sizeInKB<700) {
                newBitMab = reduceBitmapSize(bitmapByUri, 180000);
            }
            else if (sizeInKB >= 700 && sizeInKB <= 1000) {
                newBitMab = reduceBitmapSize(bitmapByUri, 200000);
            } else if (sizeInKB > 1000&&sizeInKB<=3000) {
                newBitMab = reduceBitmapSize(bitmapByUri, 300000);
            }
            else if (sizeInKB > 3000&&sizeInKB<=5000) {
                newBitMab = reduceBitmapSize(bitmapByUri, 400000);
            }
            else if (sizeInKB > 5000&&sizeInKB<=7000) {
                newBitMab = reduceBitmapSize(bitmapByUri, 500000);
            }
            else if (sizeInKB > 7000&&sizeInKB<=10000) {
                newBitMab = reduceBitmapSize(bitmapByUri, 600000);
            }
            else if (sizeInKB > 10000) {
                newBitMab = reduceBitmapSize(bitmapByUri, 700000);
            }
        }
        if (newBitMab != null) {
            String s = saveBitMab(newBitMab);
            return s;
        }
        return "null";
    }


    private static Bitmap reduceBitmapSize(Bitmap bitmap, int MAX_SIZE) {
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1)
            return bitmap;
        double ratio = Math.sqrt(ratioSquare);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
    }

    private static Bitmap getBitmapByUri(Uri uri) {
        Bitmap mBitmap = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                mBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mBitmap;
    }
}
