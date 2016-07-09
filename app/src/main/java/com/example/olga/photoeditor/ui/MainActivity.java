package com.example.olga.photoeditor.ui;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.effect.EffectContext;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.example.olga.photoeditor.models.Effects.TextureRenderer;
import com.example.olga.photoeditor.mvp.presenter.PropertyListPresenter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

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

    @BindView(R.id.activity_main_image_view_photo)
    GLSurfaceView mEffectView;

    private static FilterFragment mFilterFragment;
    private static StandardPropertyFragment mStandardPropertyFragment;
    private static ExtendPropertyFragment mExtendPropertyFragment;
    private static FragmentManager fragmentManager;
    private BottomBar mBottomBar;
    private EditText mEditText;
    private CheckBox mCheckBox;

    private AlertDialog mPublicateDialog;

    //EffectFactory
    PropertyListPresenter mPresenter;
    private EffectContext mEffectContext;
    private boolean mInitialized = false;
    private TextureRenderer mTexRenderer = new TextureRenderer();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //EffectFactory
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);      

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
                new NavigationView.OnNavigationItemSelectedListener()

                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        switch (menuItem.getItemId()) {

                            case R.id.navigation_menu_item_select: {

                                mDrawerLayout.closeDrawers();
                                return true;
                            }

                            case R.id.navigation_menu_item_save: {

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
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                showFriendList();
                                            }
                                        })
                                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                hideDialog();
                                            }
                                        })
                                        .show();
                                mDrawerLayout.closeDrawers();
                                return true;
                            }

                            default:

                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                    }
                }
        );
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

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            mPresenter.userLoadPhoto();
            mInitialized = true;
        }
        mPresenter.renderResult();
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
}

