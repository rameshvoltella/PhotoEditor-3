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
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.models.TextureRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.List;

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

    private static EffectContext mEffectContext;
    protected boolean mInitialized = false;

    //filters
    private static Effect mEffect;
    protected static String mCurrentEffect;

    //change properties value
    private static Effect mEffects[];

    //flip
    private static boolean mFlipHor;
    private static boolean mFlipVert;
    protected static String mFlip;

    //photo
    private static int[] mTextures = new int[4];
    private static int mImageWidth;
    private static int mImageHeight;
    private static TextureRenderer mTexRenderer = new TextureRenderer();
    protected Bitmap mBitmap;
    private volatile boolean mSaveFrame;

    //photo container
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
        }

        // Apply users photo properties and filters
        //apply flip
        if (!mFlip.equals("NONE")) {
            initFilters(mFlip);
            applyEffect(0, 0);
            mFlip = "NONE";
        }
        //apply properties new value
        if (mEffects != null) {
            applyEffect(0, 1);
        }
        //apply filters
        if (!mCurrentEffect.equals("NONE")) {
            initFilters(mCurrentEffect);
            applyEffect(1, 3);
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

    public static void setCurrentEffect(String propertyName) {
        mCurrentEffect = propertyName;
        mEffectView.requestRender();
    }

    public static void setChangedProperties(List<Property> properties) {
        mEffects = null;
        mEffects = new Effect[properties.size()];
        EffectFactory effectFactory = mEffectContext.getFactory();

        for (int i = 0; i < properties.size(); i++) {
            String currentEffect = properties.get(i).getPropertyName();
            float value = (float) properties.get(i).getCurrentValue();
            if (mEffects[i] != null) {
                mEffects[i].release();
            }

            switch (currentEffect) {
                //Standard Properties
                case "Яркость":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                    mEffects[i].setParameter("brightness", value);
                    break;

                case "Контрастность":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                    mEffects[i].setParameter("contrast", value);
                    break;

                case "Насыщенность":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
                    mEffects[i].setParameter("scale", value);
                    break;

                case "Резкость":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
                    mEffects[i].setParameter("scale", value);
                    break;

                //Extend Properties
                case "Автокоррекция":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
                    mEffects[i].setParameter("scale", value);
                    break;

                case "Уровень черного/белого":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
                    if (value >= 0.5) {
                        mEffects[i].setParameter("black", value);
                    } else {
                        mEffects[i].setParameter("white", (1.0 - value));
                    }
                    break;

                case "Заполняющий свет":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                    mEffects[i].setParameter("strength", value);
                    break;

                case "Зернистость":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                    mEffects[i].setParameter("strength", value);
                    break;

                case "Температура":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
                    mEffects[i].setParameter("scale", value);
                    break;

                case "Объектив":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
                    mEffects[i].setParameter("scale", value);
                    break;

                case "Виньетка":
                    mEffects[i] = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
                    mEffects[i].setParameter("scale", value);
                    break;

                default:
                    break;
            }
        }
        mEffectView.requestRender();
    }

    public static void setFlipHor() {
        mFlipHor = !mFlipHor;
        mFlip = "FLIPHOR";
        mEffectView.requestRender();
    }

    public static void setFlipVert() {
        mFlipVert = !mFlipVert;
        mFlip = "FLIPVERT";
        mEffectView.requestRender();
    }

    private void initFilters(String currentEffect) {

        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }

        switch (currentEffect) {

            case "FLIPVERT": mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);

            case "FLIPHOR":mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);

            case "CROSSPROCESS":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CROSSPROCESS);
                break;

            case "DOCUMENTARY":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DOCUMENTARY);
                break;

            case "GRAYSCALE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAYSCALE);
                break;

            case "LOMOISH":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_LOMOISH);
                break;

            case "NEGATIVE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_NEGATIVE);
                break;

            case "POSTERIZE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_POSTERIZE);
                break;

            case "SEPIA":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SEPIA);
                break;

            default:
                break;
        }

    }

    private void applyEffect(int i, int j) {
        if (!mCurrentEffect.equals("NONE")) {
            mEffect.apply(mTextures[i], mImageWidth, mImageHeight, mTextures[j]);
        }
        if (mEffects != null) {
            mEffects[0].apply(mTextures[i], mImageWidth, mImageHeight, mTextures[j]); //apply first effect
            for (int n = 1; n < mEffects.length; n++) {
                int sourceTexture = mTextures[1];
                int destinationTexture = mTextures[2];
                mEffects[n].apply(sourceTexture, mImageWidth, mImageHeight, destinationTexture);
                mTextures[1] = destinationTexture;
                mTextures[2] = sourceTexture;
            }
        }
    }

    protected static void renderResult() {
        if (!mCurrentEffect.equals("NONE") && !mCurrentEffect.equals("FLIPVERT") && !mCurrentEffect.equals("FLIPHOR")) {
            mTexRenderer.renderTexture(mTextures[3]);
        } else {
            if (mEffects != null) {
                // if no effect is chosen, just render the original bitmap
                mTexRenderer.renderTexture(mTextures[1]);
            } else {
                // render the result of applyEffect()
                mTexRenderer.renderTexture(mTextures[0]);
            }
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
        GLES20.glGenTextures(4, mTextures, 0);

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

