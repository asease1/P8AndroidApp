package com.p82018.sw806f18.p8androidapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Util {


    /**
     * Utility method to convert an image file to a bitmap. This method contains
     * an optimization to handle OutOfMemory errors when large BitMaps are
     * encountered.
     *
     * @param filePath is the absolute file-system path for the image file
     * @param nWidth   The width in pixels to scale down (scale up) the image to
     * @param nHeight  The height in pixels to scale down (scale up) the image to
     * @return the BitMap representation of the image file
     */
    public static Bitmap convertImageFileToBitmap(String filePath, int nWidth, int nHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inScaled = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, nWidth, nHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }

}
