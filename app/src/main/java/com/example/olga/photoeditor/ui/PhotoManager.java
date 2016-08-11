package com.example.olga.photoeditor.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Date: 26.07.16
 * Time: 19:06
 *
 * @author Olga
 */
public class PhotoManager {

    // PhotoManager
    public static Bitmap savePixels(int height, int width, View view, GL10 gl) {
        int heightView = view.getHeight();
        int widthView = view.getWidth();
        int x, y, w, h;

        if ((heightView / height) < (widthView / width)) {
            h = heightView;
            w = width * heightView / height;
        } else {
            h = height * widthView / width;
            w = widthView;
        }

        x = (widthView - w) / 2;
        y = (heightView - h) / 2;

        int b[] = new int[w * h];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);

        gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - i - 1) * w + j] = pix1;
            }
        }
        return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
    }

    public static String savePhoto(Bitmap bitmap, String name, ContentResolver contentResolver, Context context) {
        String path = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(path + "/saved_images");
        //noinspection ResultOfMethodCallIgnored
        myDir.mkdirs();
        String fileName = name + ".jpg";
        try {
            OutputStream fOut = null;
            File file = new File(myDir, fileName);
            try {
                fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            } finally {
                {
                    if (fOut != null) fOut.close();
                }
            }
            MediaStore.Images.Media.insertImage(contentResolver, file.getAbsolutePath(), file.getName(), file.getName());
            Toast toast = Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    public static void loadPhoto(Bitmap bitmap, int[] textures, TextureRenderer texRenderer) {

        // Generate textures
        GLES20.glGenTextures(5, textures, 0);

        //noinspection ConstantConditions
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        texRenderer.updateTextureSize(imageWidth, imageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();

    }

}
