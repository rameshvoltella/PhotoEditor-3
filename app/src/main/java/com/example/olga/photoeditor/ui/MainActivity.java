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
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.fragment.FilterFragment;
import com.example.olga.photoeditor.fragment.StandardPropertyFragment;
import com.example.olga.photoeditor.models.Effects.GLToolbox;
import com.example.olga.photoeditor.models.Effects.TextureRenderer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    private static int GALLERY_REQUEST = 1;
    private static FilterFragment mFilterFragment;
    private static StandardPropertyFragment mStandardPropertyFragment;
    private static ExtendPropertyFragment mExtendPropertyFragment;
    private static FragmentManager fragmentManager;
    private BottomBar mBottomBar;
    private EditText mEditText;
    private CheckBox mCheckBox;
    private String folderToSave;

    private AlertDialog mPublicateDialog;


    //EffectFactory
    private static GLSurfaceView mEffectView;
    private static Effect mEffect;
    private static int[] mTextures = new int[2];
    private static int mImageWidth;
    private static int mImageHeight;
    private static String mCurrentEffect;
    private static double mValueCurrentEffect;
    private static int mDegCurrentEffect;

    private static EffectContext mEffectContext;
    private boolean mInitialized = false;
    private TextureRenderer mTexRenderer = new TextureRenderer();


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //EffectFactory
        mEffectView = (GLSurfaceView) findViewById(R.id.activity_main_image_view_photo);
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mFilterFragment = new FilterFragment();
        mStandardPropertyFragment = new StandardPropertyFragment();
        mExtendPropertyFragment = new ExtendPropertyFragment();
        fragmentManager = getFragmentManager();

        folderToSave = Environment.getExternalStorageDirectory().toString();

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

        //Bottom navbar
        mBottomBar = BottomBar.attachShy(mCoordinatorLayout, findViewById(R.id.activity_main_relative_layout_scroll_container), savedInstanceState);
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


        // Set behavior of Navigation drawer
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
                            savePhoto();
                            mDrawerLayout.closeDrawers();
                            return true;
                        }

                        case R.id.navigation_menu_item_publicate: {
                            hideDialog();
                            mEditText = new EditText(MainActivity.this);
                            mEditText.setHeight(R.dimen.bb_height);
                            mCheckBox = new CheckBox(MainActivity.this);
                            mCheckBox.setText(R.string.check_friends);
                            mPublicateDialog = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(getString(R.string.send_photo))
                                    .setView(mEditText)
                                    .setView(mCheckBox)
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                        showFriendList();
                                    })
                                    .setOnCancelListener(dialog -> hideDialog())
                                    .show();
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
            photoLoader("load", selectedImage);
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialog();
    }

    //EffectFactory
    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            photoLoader("default", null);
            mInitialized = true;
        }
        if (!mCurrentEffect.equals("NONE")) {
            initEffect();
            applyEffect();
        }

        renderResult();
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

    public static void setCurrentEffect(String propertyName, double defaultValue, int deg) {
        mCurrentEffect = propertyName;
        mValueCurrentEffect = defaultValue;
        mDegCurrentEffect = deg;

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
                mEffect.setParameter("angle", mDegCurrentEffect);
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

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (!mCurrentEffect.equals("NONE")) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
            mTextures[0] = mTextures[1];
        } else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    private void photoLoader(String label, Uri uri) {
        Bitmap bitmap = null;
        switch (label) {
            case "default": {
                // Generate textures
                GLES20.glGenTextures(2, mTextures, 0);

                // Load input bitmap
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.pinguin);

                mCurrentEffect = "NONE";
                break;
            }
            case "load": {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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


    private String savePhoto() {
        OutputStream fOut = null;
        Calendar calendar = Calendar.getInstance();

        try {
            File file = new File(folderToSave, Integer.toString(calendar.YEAR) + Integer.toString(calendar.MONTH) + Integer.toString(calendar.DAY_OF_MONTH) + Integer.toString(calendar.HOUR) + Integer.toString(calendar.MINUTE) + Integer.toString(calendar.SECOND) + ".jpg");

            Bitmap bitmap = mEffectView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    private void showFriendList() {
        if (mCheckBox.isChecked()) {
            Intent intent = new Intent(this, FriendListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.publication, Toast.LENGTH_LONG).show();
        }

    }

    private void hideDialog() {
        if (mPublicateDialog != null) {
            mPublicateDialog.dismiss();
        }

    }

}

