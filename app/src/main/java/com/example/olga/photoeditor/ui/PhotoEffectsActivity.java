package com.example.olga.photoeditor.ui;

import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Property;

import java.util.ArrayList;
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
public abstract class PhotoEffectsActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private static EffectContext mEffectContext;
    protected boolean mInitialized = false;

    //filters
    private static Effect mEffect;
    private static String mCurrentFilter;

    //change properties value
    private static List<Property> mChangedProperties = new ArrayList<>();
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
            PhotoManager.loadPhoto(mBitmap, mTextures, mTexRenderer);
            mImageHeight = mBitmap.getHeight();
            mImageWidth = mBitmap.getWidth();
            mInitialized = true;
        }

        // Apply users photo properties and filters
        //apply flip
        if (mFlip[0] > 0) {
            releaseEffect();
            mEffect = EffectsInitializer.initFilters("FLIPHOR", mEffectContext);
            applyEffect("FLIP");
        }
        if (mFlip[1] > 0) {
            releaseEffect();
            mEffect = EffectsInitializer.initFilters("FLIPVERT", mEffectContext);
            applyEffect("FLIP");
        }
        //apply properties new value
        if (mChangedProperties.size() != 0) {
            mEffects = null;
            mEffects = EffectsInitializer.initEffects(mChangedProperties, mEffectContext);
            applyEffect("PROPERTY");
        }
        //apply filters
        if (!mCurrentFilter.equals("NONE")) {
            releaseEffect();
            mEffect = EffectsInitializer.initFilters(mCurrentFilter, mEffectContext);
            applyEffect("FILTER");
        }

        renderResult();

        if (mSaveFrame) {
            Bitmap photo = PhotoManager.savePixels(mImageHeight, mImageWidth, mEffectView, gl);
            PhotoManager.savePhoto(photo, mNameEditText.getText().toString(), getContentResolver(), PhotoEffectsActivity.this);
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

    public static void setCurrentFilter(String filterName) {
        mCurrentFilter = filterName;
        mEffectView.requestRender();
    }

    public static String getmCurrentFilter() {
        return mCurrentFilter;
    }

    public static void setChangedProperties(List<Property> properties) {
        mChangedProperties = properties;
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

    public void clearFilters() {
        mFlip = new int[2];
        mFlip[0] = 0;
        mFlip[1] = 0;
        mEffects = null;
        mCurrentFilter = "NONE";
        isEffectApply = false;
        isFlipApply = false;
    }

    public void messageAnimation(Animation animation, int visible) {
        mMessageLayout.startAnimation(animation);
        mMessageLayout.setVisibility(visible);
        mMessageLinearLayout.setVisibility(visible);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    private static void renderResult() {
        // render the result of applyEffect()
        if (!mCurrentFilter.equals("NONE")) {
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

    private void applyEffect(String label) {
        int sourceTexture, destinationTexture;
        switch (label) {

            case "FLIP":
                if (mFlip[0] % 2 == 1 || mFlip[1] % 2 == 1) {
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

    private void releaseEffect() {
        if (mEffect != null) {
            mEffect.release();
        }
    }

}

