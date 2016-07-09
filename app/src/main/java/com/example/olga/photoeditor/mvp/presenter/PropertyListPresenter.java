package com.example.olga.photoeditor.mvp.presenter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Effects.Filter;
import com.example.olga.photoeditor.models.Effects.GLToolbox;
import com.example.olga.photoeditor.models.Effects.Property;
import com.example.olga.photoeditor.models.Effects.TextureRenderer;
import com.example.olga.photoeditor.mvp.view.PropertyListView;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:54
 *
 * @author Olga
 */

@InjectViewState
public class PropertyListPresenter extends MvpPresenter<PropertyListView> {
    List<Property> mList;


    //EffectFactory
    private EffectContext mEffectContext;
    private Effect mEffect;
    private int[] mTextures = new int[2];
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private String mCurrentEffect;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void userSelectStandardProperties() {
        mList = Filter.getStandardProperties();
        getViewState().setData(mList);
    }

    public void userSelectExtendProperties() {
        mList = Filter.getExtendProperties();
        getViewState().setData(mList);
    }

    public void userChangeValue(String propertyName, double defaultValue, int deg) {

        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        /**
         * Initialize the correct effect based on the selected menu/action item
         */
        mCurrentEffect = propertyName;
        switch (mCurrentEffect) {
            //Standard Properties
            case "BRIGHTNESS":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", defaultValue);
                break;

            case "CONTRAST":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", defaultValue);
                break;

            case "SATURATE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", defaultValue);
                break;


            case "SHARPEN":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SHARPEN);
                mEffect.setParameter("scale", defaultValue);
                break;

            //Extend Properties
            case "AUTOFIX":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", defaultValue);
                break;

            case "BLACK":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", defaultValue);
                break;

            case "WHITE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("white", defaultValue);
                break;

            case "FILLIGHT":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", defaultValue);
                break;

            case "GRAIN":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", defaultValue);
                break;

            case "TEMPERATURE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", defaultValue);
                break;

            case "FISHEYE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", defaultValue);
                break;

            case "VIGNETTE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", defaultValue);
                break;

            //Buttons Properties #1
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

            case "ROTATE":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", deg);
                break;


            //Buttons Properties #2

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

    public void userLoadPhoto() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);

        // Load input bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(),
                 R.drawable.pinguin);
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();

        mCurrentEffect = "NONE";
    }

    public void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    public void renderResult() {
        if (!mCurrentEffect.equals("NONE")) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        }
        else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }


}
