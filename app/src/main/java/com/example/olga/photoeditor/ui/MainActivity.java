package com.example.olga.photoeditor.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.fragment.FilterFragment;
import com.example.olga.photoeditor.fragment.StandardPropertyFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 25.06.16
 * Time: 13:38
 *
 * @author Olga
 */

public class MainActivity extends PhotoEffects {

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
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pinguin);

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
        mBottomBar.setActiveTabColor(R.color.green);
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
                            showMessage();
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
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                mInitialized = false;
                mEffectView.requestRender();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


}

