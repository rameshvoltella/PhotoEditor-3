package com.example.olga.photoeditor.ui;

import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.GLToolbox;
import com.example.olga.photoeditor.models.TextureRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;

/**
 * Date: 17.07.16
 * Time: 12:57
 *
 * @author Olga
 */
public abstract class PhotoEffects extends AppCompatActivity implements GLSurfaceView.Renderer {

    private static Effect mEffect;
    private static int[] mTextures = new int[2];
    private static int mImageWidth;
    private static int mImageHeight;
    private static String mCurrentEffect;
    private static double mValueCurrentEffect;
    private static EffectContext mEffectContext;
    private static TextureRenderer mTexRenderer = new TextureRenderer();
    private volatile boolean mSaveFrame;
    private volatile boolean mFilterApply;
    protected boolean mInitialized = false;
    protected Bitmap mBitmap;

    protected static GLSurfaceView mEffectView;

    @BindView(R.id.activity_main_layout_message)
    FrameLayout mMessageLayout;

    @BindView(R.id.activity_main_edit_text_file_name)
    EditText mNameEditText;

    @BindView(R.id.activity_main_button_ok)
    Button mOkButton;

    @BindView(R.id.activity_main_button_cancel)
    Button mCancelButton;


    @Override
    protected void onPause() {
        super.onPause();
        mEffectView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEffectView.onResume();
    }

    //EffectFactory
    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadPhoto(mBitmap);
            mInitialized = true;
            renderResult();
        }

        if (!mCurrentEffect.equals("NONE")) {
            initEffect(mCurrentEffect, mValueCurrentEffect);
            applyEffect();
            if (mFilterApply) {
                mTextures[0] = mTextures[1];
                mFilterApply = false;
            }
        }

        renderResult();

        if (mSaveFrame) {
            savePhoto(savePixels(mImageHeight, mImageWidth, mEffectView, gl));
            mSaveFrame = false;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    public static void setCurrentEffect(String propertyName, double defaultValue) {

        mCurrentEffect = propertyName;
        mValueCurrentEffect = defaultValue;
        mEffectView.requestRender();
    }

    private void initEffect(String currentEffect, double value) {

        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }

        switch (currentEffect) {
            //Standard Properties
            case "Яркость":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", (float) value);
                break;

            case "Контрастность":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", (float) value);
                break;

            case "Насыщенность":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", (float) value);
                break;

            case "Резкость":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SHARPEN);
                mEffect.setParameter("scale", (float) value);
                break;

            //Extend Properties
            case "Автокоррекция":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", (float) value);
                break;

            case "Уровень черного/белого":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                if (value >= 0.5) {
                    mEffect.setParameter("black", (float) value);
                } else {
                    mEffect.setParameter("white", (float) (1.0 - value));
                }
                break;

            case "Заполняющий свет":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", (float) value);
                break;

            case "Зернистость":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", (float) value);
                break;

            case "Температура":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", (float) value);
                break;

            case "Объектив":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", (float) value);
                break;

            case "Виньетка":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", (float) value);
                break;

            //Flip
            case "FLIPVERT":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);
                break;

            case "FLIPHOR":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);
                break;

            //Filters
            case "CROSSPROCESS":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CROSSPROCESS);
                mFilterApply = true;
                break;

            case "DOCUMENTARY":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DOCUMENTARY);
                mFilterApply = true;
                break;

            case "GRAYSCALE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAYSCALE);
                mFilterApply = true;
                break;

            case "LOMOISH":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_LOMOISH);
                mFilterApply = true;
                break;

            case "NEGATIVE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_NEGATIVE);
                mFilterApply = true;
                break;

            case "POSTERIZE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_POSTERIZE);
                mFilterApply = true;
                break;

            case "SEPIA":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SEPIA);
                mFilterApply = true;
                break;

            default:
                break;
        }

    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    protected static void renderResult() {
        if (!mCurrentEffect.equals("NONE")) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        } else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    // PhotoManager
    private static Bitmap savePixels(int height, int width, View view, GL10 gl) {
        int heightView = view.getHeight();
        int widthView = view.getWidth();
        int x, y, w, h;
        if (heightView / height < widthView / width) {
            h = heightView;
            w = width * heightView / height;
            x = (widthView - w) / 2;
            y = h;
        } else {
            h = height * widthView / width;
            w = widthView;
            x = w;
            y = (heightView + h) / 2;
        }

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

    private String savePhoto(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(path + "/saved_images");
        //noinspection ResultOfMethodCallIgnored
        myDir.mkdirs();
        String fileName = mNameEditText.getText() + ".jpg";
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
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    protected void loadPhoto(Bitmap bitmap) {

        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);
        mCurrentEffect = "NONE";

        //noinspection ConstantConditions
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();

    }

    //Enter Name Message
    protected void showMessage() {
        Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        mMessageLayout.startAnimation(animationUp);
        mMessageLayout.setVisibility(View.VISIBLE);
        mOkButton.setOnClickListener(v -> {
            if (mNameEditText.getText().toString().equals("")) {
                mNameEditText.setFocusable(true);
            } else {
                mSaveFrame = true;
                mCurrentEffect = "NONE";
                //mEffectView.requestRender();
                mMessageLayout.startAnimation(animationDown);
                mMessageLayout.setVisibility(View.GONE);
            }
        });
        mCancelButton.setOnClickListener(v -> {
            mMessageLayout.startAnimation(animationDown);
            mMessageLayout.setVisibility(View.GONE);

        });
    }
}

