package com.example.olga.photoeditor.mvp.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.provider.MediaStore;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.db.EffectDataSource;
import com.example.olga.photoeditor.models.EffectsLabel;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.models.PhotoEffect;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.view.PhotoEffectsView;
import com.example.olga.photoeditor.ui.EffectsInitializer;
import com.example.olga.photoeditor.ui.PhotoManager;
import com.example.olga.photoeditor.ui.TextureRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;


/**
 * Date: 03.08.16
 * Time: 19:46
 *
 * @author Olga
 */

@InjectViewState
public class PhotoEffectsPresenter extends MvpPresenter<PhotoEffectsView> implements EffectDataSource.ResultListener<PhotoEffect> {

    private int mImageWidth;
    private int mImageHeight;
    private String mPhotoName;
    //filters and properties
    private String mCurrentFilter;
    private List<Property> mChangedProperties = new ArrayList<>();
    private static int[] mFlip;
    private Context mContext;
    EffectDataSource mEffectDataSource;

    public void initEditor(Context context) {
        mContext = context;
        if (mEffectDataSource == null) {
            mEffectDataSource = new EffectDataSource(context, this);
            resetAllEffects();
        }
        getViewState().setEffectsData(mEffectDataSource);
    }

    public void userUpdatePhoto(Bitmap bitmap) {

        if (bitmap == null) {
            resetAllEffects();
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pinguin);
        }

        getViewState().setBitmap(bitmap);
        getViewState().showPhoto();
    }

    public void userSelectPhoto(ContentResolver contentResolver, Uri selectedImage) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage);
            resetAllEffects();
            getViewState().setBitmap(bitmap);
            getViewState().showPhoto();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userEnterPhotoName(String name) {
        mPhotoName = name;
        getViewState().savePhoto();
    }

    public void userSavePhoto(GLSurfaceView glSurfaceView, GL10 gl, ContentResolver contentResolver) {
        Bitmap photo = PhotoManager.savePixels(mImageHeight, mImageWidth, glSurfaceView, gl);
        PhotoManager.savePhoto(photo, mPhotoName, contentResolver, mContext);
    }

    public void userInitTextures(TextureRenderer texRenderer, int textures[], Bitmap bitmap) {
        texRenderer.init();
        PhotoManager.loadPhoto(bitmap, textures, texRenderer);
        getViewState().setTextures(textures);
        mImageHeight = bitmap.getHeight();
        mImageWidth = bitmap.getWidth();
        getViewState().setResultTexture(0);
    }

    public void userCreateEffect(EffectContext effectContext) {
        Effect effect;
        Effect effects[];
        int resultTexture = 0;

        //apply flip
        if (mFlip[0] % 2 == 1) {
            effect = EffectsInitializer.initFilters(Filter.FLIPHOR.name(), effectContext);
            getViewState().applyEffect(resultTexture, ++resultTexture, effect, mImageHeight, mImageWidth);
            releaseEffect(effect);
        }

        if (mFlip[1] % 2 == 1) {
            effect = EffectsInitializer.initFilters(Filter.FLIPVERT.name(), effectContext);
            getViewState().applyEffect(resultTexture, ++resultTexture, effect, mImageHeight, mImageWidth);
            releaseEffect(effect);
        }

        //apply properties new value
        if (mChangedProperties.size() != 0) {
            effects = EffectsInitializer.initEffects(mChangedProperties, effectContext);
            getViewState().applyEffect(resultTexture, ++resultTexture, effects[0], mImageHeight, mImageWidth); //apply first effect
            int sourceTexture, destinationTexture;
            int textures[] = new int[2];
            textures[0] = resultTexture;
            textures[1] = resultTexture + 1;
            releaseEffect(effects[0]);

            for (int n = 1; n < effects.length; n++) {
                sourceTexture = textures[0];
                destinationTexture = textures[1];
                //apply next effect
                getViewState().applyEffect(sourceTexture, destinationTexture, effects[n], mImageHeight, mImageWidth);
                //save textures
                getViewState().changeTexture(resultTexture, destinationTexture + 1);
                getViewState().changeTexture(resultTexture + 1, sourceTexture + 1);
                textures[0] = destinationTexture;
                textures[1] = sourceTexture;
                releaseEffect(effects[n]);
            }
        }

        //apply filters
        if (mCurrentFilter != null && !mCurrentFilter.equals(Filter.NONE.name())) {
            effect = EffectsInitializer.initFilters(mCurrentFilter, effectContext);
            getViewState().applyEffect(resultTexture, ++resultTexture, effect, mImageHeight, mImageWidth);
            releaseEffect(effect);
        }
        getViewState().setResultTexture(resultTexture);
    }

    private void resetAllEffects() {
        mFlip = new int[2];
        mFlip[0] = 0;
        mFlip[1] = 0;
        mChangedProperties.clear();
        mCurrentFilter = Filter.NONE.name();
        //get all default properties
        List<Property> properties = Property.getStandardProperties();
        properties.addAll(Property.getExtendProperties());
        //get all default filters
        List<Filter> filters = Filter.getFilterList();
        //add flip effect
        filters.add(Filter.FLIPVERT);
        filters.add(Filter.FLIPHOR);
        //add/update to database
        mEffectDataSource.deleteAllEffects();
        for (int i = 1; i < properties.size(); i++) {
            mEffectDataSource.createEffect(new PhotoEffect(properties.get(i).name(), EffectsLabel.PROPERTY.name(), properties.get(i).getCurrentValue()));
        }
        mEffectDataSource.updateEffect(new PhotoEffect(filters.get(0).name(), EffectsLabel.FILTER.name(), 1.0f));
        for (int i = 1; i < filters.size(); i++) {
            mEffectDataSource.createEffect(new PhotoEffect(filters.get(i).name(), EffectsLabel.FILTER.name(), 0.0f));
        }
    }

    private void releaseEffect(Effect effect) {
        if (effect != null) {
            effect.release();
        }
    }

    @Override
    public void updateEffectValue(PhotoEffect effect) {
        if (effect.getEffectType().equals(EffectsLabel.PROPERTY.name())) {
            Property property = Property.valueOf(effect.getEffectName());
            property.setCurrentValue(effect.getEffectValue());
            for (int i = 0; i < mChangedProperties.size(); i++) {
                if (mChangedProperties.get(i).name().equals(effect.getEffectName())) {
                    mChangedProperties.remove(i);
                    break;
                }
            }
            mChangedProperties.add(property);
        }
        if (effect.getEffectType().equals(EffectsLabel.FILTER.name())) {
            Filter filter = Filter.valueOf(effect.getEffectName());
            switch (filter) {
                case FLIPVERT:
                    ++mFlip[1];
                    break;
                case FLIPHOR:
                    ++mFlip[0];
                    break;
                default:
                    mCurrentFilter = effect.getEffectName();
                    break;
            }
        }
        getViewState().setEffect();
    }
}
