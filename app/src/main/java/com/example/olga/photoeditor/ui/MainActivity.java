package com.example.olga.photoeditor.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.fragment.FilterFragment;
import com.example.olga.photoeditor.fragment.StandardPropertyFragment;
import com.example.olga.photoeditor.models.GLToolbox;
import com.example.olga.photoeditor.models.TextureRenderer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 25.06.16
 * Time: 13:38
 *
 * @author Olga
 */

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    @BindView(R.id.activity_main_main_content)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;

    @BindView(R.id.activity_main_drawerlayout)
    DrawerLayout mDrawerLayout;

    private BottomBar mBottomBar;

    //Fragments
    private static FilterFragment mFilterFragment;
    private static StandardPropertyFragment mStandardPropertyFragment;
    private static ExtendPropertyFragment mExtendPropertyFragment;
    private static FragmentManager fragmentManager;

    private static int GALLERY_REQUEST = 1;

    //EffectFactory
    private static GLSurfaceView mEffectView;
    private static Effect mEffect;
    private static int[] mTextures = new int[2];
    private static int mImageWidth;
    private static int mImageHeight;
    private static String mLastEffect;
    private static String mCurrentEffect;
    private static double mValueCurrentEffect;
    private static EffectContext mEffectContext;
    private boolean mInitialized = false;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private volatile boolean saveFrame;

    private String mPath;


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //init EffectFactory
        mEffectView = (GLSurfaceView) findViewById(R.id.activity_main_image_view_photo);
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mEffectView.setPreserveEGLContextOnPause(true);

        mPath = Environment.getExternalStorageDirectory().toString();

        //init fragments
        mFilterFragment = new FilterFragment();
        mStandardPropertyFragment = new StandardPropertyFragment();
        mExtendPropertyFragment = new ExtendPropertyFragment();
        fragmentManager = getFragmentManager();

        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            //noinspection ConstantConditions
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Bottom navigation bar
        mBottomBar = BottomBar.attachShy(mCoordinatorLayout, findViewById(R.id.activity_main_relative_layout_scroll_container), savedInstanceState);
        mBottomBar.useDarkTheme();
        mBottomBar.setActiveTabColor(R.color.white);
        mBottomBar.setItems(R.menu.bottom_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottom_menu_standard) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.activity_main_frame_layout_container, mStandardPropertyFragment)
                            .addToBackStack(null)
                            .commit();
                } else if (menuItemId == R.id.bottom_menu_extend) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.activity_main_frame_layout_container, mExtendPropertyFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.activity_main_frame_layout_container, mFilterFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        // menu
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if (menuItem.isChecked()) menuItem.setChecked(false);
                    else menuItem.setChecked(true);

                    switch (menuItem.getItemId()) {

                        case R.id.navigation_menu_item_select: {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            mDrawerLayout.closeDrawers();
                            return true;
                        }

                        case R.id.navigation_menu_item_save: {
                            saveFrame = true;
                            mEffectView.requestRender();
                            mDrawerLayout.closeDrawers();
                            return true;
                        }

                        default:

                            mDrawerLayout.closeDrawers();
                            return true;
                    }
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            loadPhoto("load", selectedImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState((outState));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //EffectFactory
    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadPhoto("default", null);
            mInitialized = true;
        }

        if (saveFrame) {
            savePhoto(savePixels(mEffectView, gl));
            saveFrame = false;
        } else {
            if (!mCurrentEffect.equals("NONE")) {
                initEffect();
                applyEffect();

                mLastEffect = mCurrentEffect;
            }

            renderResult();
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
        if (!mCurrentEffect.equals(mLastEffect) && !mLastEffect.equals("NONE")) {
            mTextures[0] = mTextures[1];
        }
        mValueCurrentEffect = defaultValue;
        mEffectView.requestRender();
    }

    private void initEffect() {

        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        /**
         * Initialize the correct effect based on the selected menu/action item
         */

        switch (mCurrentEffect) {
            //Standard Properties
            case "Яркость":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", (float) mValueCurrentEffect);
                break;

            case "Контрастность":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", (float) mValueCurrentEffect);
                break;

            case "Насыщенность":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", (float) mValueCurrentEffect);
                break;


            case "Резкость":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SHARPEN);
                mEffect.setParameter("scale", (float) mValueCurrentEffect);
                break;

            //Extend Properties
            case "Автокоррекция":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", (float) mValueCurrentEffect);
                break;

            case "Уровень черного":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", (float) mValueCurrentEffect);
                break;

            case "Уровень белого":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("white", (float) mValueCurrentEffect);
                break;

            case "Заполняющий свет":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", (float) mValueCurrentEffect);
                break;

            case "Зернистость":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", (float) mValueCurrentEffect);
                break;

            case "Температура":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", (float) mValueCurrentEffect);
                break;

            case "Объектив":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", (float) mValueCurrentEffect);
                break;

            case "Виньетка":
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", (float) mValueCurrentEffect);
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

            default:
                break;
        }

    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (!mCurrentEffect.equals("NONE")) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        } else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    private static Bitmap savePixels(View view, GL10 gl) {
        int h = mImageHeight;
        int w = mImageWidth;
        int x = (view.getWidth() - w) / 2;
        int y = (view.getHeight() + h) / 2;
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
        Calendar now = Calendar.getInstance();
        try {
            OutputStream fOut = null;
            String fileName = "photo" + Integer.toString(now.get(Calendar.HOUR)) + Integer.toString(now.get(Calendar.MINUTE)) + Integer.toString(now.get(Calendar.SECOND)) + Integer.toString(now.get(Calendar.MILLISECOND)) + ".jpg";
            File file = new File(mPath, fileName);
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

    private void loadPhoto(String label, Uri uri) {
        Bitmap bitmap = null;
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);

        mCurrentEffect = "NONE";
        mLastEffect = "NONE";

        switch (label) {
            case "default": {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pinguin);
                break;
            }
            case "load": {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

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

}

