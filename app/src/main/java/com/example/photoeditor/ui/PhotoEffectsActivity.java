package com.example.photoeditor.ui;

import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.photoeditor.mvp.presenter.PhotoEffectsPresenter;
import com.example.photoeditor.mvp.view.PhotoEffectsView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Date: 17.07.16
 * Time: 12:57
 *
 * @author Olga
 */
public abstract class PhotoEffectsActivity extends MvpAppCompatActivity implements GLSurfaceView.Renderer, PhotoEffectsView {

    //photo
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private EffectContext mEffectContext;
    private Bitmap mBitmap;
    private int[] mTextures = new int[5];
    private int mResultTexture;

    //labels
    private boolean mSaveFrame;
    private boolean mInitialized = false;

    //photo container
    private GLSurfaceView mEffectView;

    @InjectPresenter
    PhotoEffectsPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //surfaceView initialization and setting
        mEffectView = glSurfaceViewInitialization();
        //noinspection ConstantConditions
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //Set default/current photo
        mPresenter.userUpdatePhoto(this, (Bitmap) getLastCustomNonConfigurationInstance());
    }

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

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mBitmap;
    }

    //EffectFactory
    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mPresenter.userInitTextures(mTexRenderer, mTextures, mBitmap);
            mInitialized = true;
        }

        mPresenter.userCreateEffect(mEffectContext);

        renderResult(mResultTexture);

        if (mSaveFrame) {
            mPresenter.userSavePhoto(this, mEffectView, gl, getContentResolver());
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

    @Override
    public void showPhoto() {
        mInitialized = false;
        mEffectView.requestRender();
    }

    @Override
    public void savePhoto() {
        mSaveFrame = true;
        mEffectView.requestRender();
    }

    @Override
    public void setTextures(int textures[]) {
        mTextures = textures;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void changeTexture(int number, int data) {
        mTextures[number] = data;
    }

    @Override
    public void setResultTexture(int resultTexture) {
        mResultTexture = resultTexture;
    }

    @Override
    public void setEffect() {
        mEffectView.requestRender();
    }

    @Override
    public void applyEffect(int sourceTexture, int destinationTexture, Effect effect, int height, int width) {
        if (mEffectContext != null) {
            effect.apply(mTextures[sourceTexture], width, height, mTextures[destinationTexture]);
        }
    }

    abstract GLSurfaceView glSurfaceViewInitialization();

    public void selectPhoto(Uri selectedImage) {
        mPresenter.userSelectPhoto(getContentResolver(), selectedImage);
    }

    public void setPhotoName(String string) {
        mPresenter.userEnterPhotoName(string);
    }

    private void renderResult(int number) {
        mTexRenderer.renderTexture(mTextures[number]);
    }

}

