package com.example.olga.photoeditor.ui;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.ui.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.ui.fragment.FilterFragment;
import com.example.olga.photoeditor.ui.fragment.StandardPropertyFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 25.06.16
 * Time: 13:38
 *
 * @author Olga
 */

public class MainActivity extends PhotoEffectsActivity {

    private static final String SET_DATA = "SET_DATA";
    @BindView(R.id.activity_main_main_content)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;

    @BindView(R.id.activity_main_drawerlayout)
    DrawerLayout mDrawerLayout;

    // EnterName window
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

    private BottomBar mBottomBar;

    //Fragments
    private static FilterFragment mFilterFragment;
    private static StandardPropertyFragment mStandardPropertyFragment;
    private static ExtendPropertyFragment mExtendPropertyFragment;
    private static FragmentManager fragmentManager;

    private static int GALLERY_REQUEST = 1;

    private Animation mAnimationUp;
    private Animation mAnimationDown;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //init fragments
        mFilterFragment = new FilterFragment();
        mStandardPropertyFragment = new StandardPropertyFragment();
        mExtendPropertyFragment = new ExtendPropertyFragment();
        fragmentManager = getSupportFragmentManager();

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

        // menu
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_menu_item_select: {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            mDrawerLayout.closeDrawers();
                            return true;
                        }

                        case R.id.navigation_menu_item_save: {
                            messageAnimation(mAnimationUp, View.VISIBLE);
                            mDrawerLayout.closeDrawers();
                            return true;
                        }

                        default:
                            mDrawerLayout.closeDrawers();
                            return true;
                    }

                }
        );

        //enter name message
        mAnimationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        mAnimationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        mOkButton.setOnClickListener(v -> {
            if (mNameEditText.getText().toString().equals("")) {
                mNameEditText.setFocusable(true);
            } else {
                setPhotoName(mNameEditText.getText().toString());
                messageAnimation(mAnimationDown, View.GONE);
            }
        });
        mCancelButton.setOnClickListener(v -> messageAnimation(mAnimationDown, View.GONE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Set Listeners
        Bundle bundle = new Bundle();
        bundle.putSerializable(SET_DATA, mEffectDataSource);
        mStandardPropertyFragment.setArguments(bundle);
        mExtendPropertyFragment.setArguments(bundle);
        mFilterFragment.setArguments(bundle);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            selectPhoto(imageReturnedIntent.getData());
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

    @Override
    GLSurfaceView glSurfaceViewInitialization() {
        return (GLSurfaceView) findViewById(R.id.activity_main_image_view_photo);
    }

    private void messageAnimation(Animation animation, int visible) {
        mMessageLayout.startAnimation(animation);
        mMessageLayout.setVisibility(visible);
        mMessageLinearLayout.setVisibility(visible);
    }

}

