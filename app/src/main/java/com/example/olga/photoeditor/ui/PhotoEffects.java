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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Property;

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
    private boolean mInitialized = false;

    //filters
    private static Effect mEffect;
    private static String mCurrentEffect;

    //change properties value
    private static Effect mEffects[];
    private static boolean isEffectApply;

    //flip
    private static int[] mFlip;
    private static boolean isFlipApply;

    //photo
    private static int[] mTextures = new int[4];
    private static int mImageWidth;
    private static int mImageHeight;
    private static TextureRenderer mTexRenderer = new TextureRenderer();
    private Bitmap mBitmap;
    protected volatile boolean mSaveFrame;

    //photo container
    protected static GLSurfaceView mEffectView;

    @BindView(R.id.activity_main_layout_message)
    FrameLayout mMessageLayout;

    @BindView(R.id.activity_main_linear_layout_message)
    LinearLayout mMessageLinearLayout;

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
        mInitialized = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEffectView.onResume();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mBitmap;
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
        if (mFlip[0] > 0 ) {
            initFilters("FLIPHOR");
            applyEffect("FLIP");
        }
        if (mFlip[1] > 0 ) {
            initFilters("FLIPVERT");
            applyEffect("FLIP");
        }
        //apply properties new value
        if (mEffects != null) {
            applyEffect("PROPERTY");
        }
        //apply filters
        if (!mCurrentEffect.equals("NONE")) {
            initFilters(mCurrentEffect);
            applyEffect("FILTER");
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
            float value = properties.get(i).getCurrentValue();
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
                        mEffects[i].setParameter("white", (1.0f - value));
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

    public static void setFlip(String flip) {
        switch (flip) {
            case "FLIPVERT":
                ++mFlip[1];
            case "FLIPHOR":
                ++mFlip[0];
        }
        mEffectView.requestRender();
    }

    private void initFilters(String currentEffect) {

        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }

        switch (currentEffect) {

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

    private void applyEffect(String label) {
        int sourceTexture, destinationTexture;
        switch (label) {

            case "FLIP":
                if (mFlip[0]%2 == 1 || mFlip[1]%2 == 1){
                    mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
                    isFlipApply = true;
                } else {
                    isFlipApply = false;
                }

                break;

            case "PROPERTY":
                int i = 0;
                if (isFlipApply) {
                    i = 1;
                }

                mEffects[0].apply(mTextures[i], mImageWidth, mImageHeight, mTextures[2]); //apply first effect
                for (int n = 1; n < mEffects.length; n++) {
                    sourceTexture = mTextures[2];
                    destinationTexture = mTextures[3];
                    mEffects[n].apply(sourceTexture, mImageWidth, mImageHeight, destinationTexture);
                    mTextures[2] = destinationTexture;
                    mTextures[3] = sourceTexture;
                }
                isEffectApply = true;
                break;

            case "FILTER":
                int j = 0;
                if (isFlipApply) {
                    j = 1;
                }
                if (isEffectApply) {
                    j = 2;
                }
                mEffect.apply(mTextures[j], mImageWidth, mImageHeight, mTextures[3]);
                break;

            default:
                break;
        }

    }

    protected static void renderResult() {
        // render the result of applyEffect()
        if (!mCurrentEffect.equals("NONE")) {
            mTexRenderer.renderTexture(mTextures[3]);
        } else {
            if (isEffectApply) {
                mTexRenderer.renderTexture(mTextures[2]);
            } else {
                if (isFlipApply) {
                    mTexRenderer.renderTexture(mTextures[1]);
                } else {
                    // if no effect is chosen, just render the original bitmap
                    mTexRenderer.renderTexture(mTextures[0]);
                }
            }
        }
    }

    // PhotoManager
    private static Bitmap savePixels(int height, int width, View view, GL10 gl) {
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
            Toast toast = Toast.makeText(PhotoEffects.this, "Сохранено", Toast.LENGTH_SHORT);
            toast.show();
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

    protected void messageAnimation(Animation animation, int visible) {
        mMessageLayout.startAnimation(animation);
        mMessageLayout.setVisibility(visible);
        mMessageLinearLayout.setVisibility(visible);
    }

    protected void clearFilters(){
        mFlip = new int[2];
        mFlip[0] = 0;
        mFlip[1] = 0;
        mEffects = null;
        mCurrentEffect = "NONE";
        isEffectApply = false;
        isFlipApply = false;
        mInitialized = false;
    }

    protected void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
    }

    protected Bitmap getBitmap() {
        return mBitmap;
    }
}

