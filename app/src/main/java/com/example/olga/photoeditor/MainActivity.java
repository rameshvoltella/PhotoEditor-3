package com.example.olga.photoeditor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.olga.photoeditor.async.FriendsListAsyncTask;
import com.example.olga.photoeditor.fragment.DatabaseFragment;
import com.example.olga.photoeditor.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.fragment.FriendListFragment;
import com.example.olga.photoeditor.fragment.StandardPropertyFragment;
import com.example.photoeditor.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_main_viewpager)
    ViewPager viewPager;

    @BindView(R.id.activity_main_tabs)
    TabLayout tabs;

    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;

    @BindView((R.id.activity_main_drawerlayout))
    DrawerLayout mDrawerLayout;

    private static final String ASYNC_TASK = "ASYNC_TASK";
    private static String FRIEND_LIST_TAG = "FRIEND_LIST_TAG";
    private static FriendListFragment mFriendListFragment;
    private static DatabaseFragment mDatabaseFragment;
    private static StandardPropertyFragment mStandardPropertyFragment;
    private static ExtendPropertyFragment mExtendPropertyFragment;
    private static FragmentManager fragmentManager;
    private static  FriendsListAsyncTask mFriendsListAsyncTask;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDatabaseFragment = new DatabaseFragment();
        mStandardPropertyFragment = new StandardPropertyFragment();
        mExtendPropertyFragment = new ExtendPropertyFragment();
        mFriendListFragment = new FriendListFragment();
        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);

        // Setting ViewPager for each Tabs
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        tabs.setupWithViewPager(viewPager);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            //noinspection ConstantConditions
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        switch (menuItem.getItemId()) {

                            case R.id.navigation_menu_item_database: {
                                viewPager.setCurrentItem(0, false);
                                mDrawerLayout.closeDrawers();
                                return true;
                            }

                            case R.id.navigation_menu_item_standart_properties: {
                                removeFriendList();
                                viewPager.setCurrentItem(1, false);
                                mDrawerLayout.closeDrawers();
                                return true;
                            }

                            case R.id.navigation_menu_item_extend_properties: {
                                removeFriendList();
                                viewPager.setCurrentItem(2, false);
                                mDrawerLayout.closeDrawers();
                                return true;
                            }

                            default:
                                removeFriendList();
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                    }
                });

        mFriendsListAsyncTask = (FriendsListAsyncTask) getLastCustomNonConfigurationInstance();

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
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mFriendListFragment.getFriendsListAsyncTask();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static void checkFriendList() {
        if (fragmentManager.findFragmentByTag(FRIEND_LIST_TAG) != null) {
            if (mFriendsListAsyncTask != null && !mFriendListFragment.isInLayout()) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(ASYNC_TASK, mFriendsListAsyncTask);
                mFriendListFragment.setArguments(arguments);
            }
            getFriendList();
        }
    }

    public static void getFriendList() {
        fragmentManager.beginTransaction()
                .replace(R.id.database_fragment_frame_layout_container, mFriendListFragment, FRIEND_LIST_TAG)
                .addToBackStack(null)
                .commit();
    }

    public static void removeFriendList() {
        if (mFriendListFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .remove(mFriendListFragment)
                    .commit();
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(mDatabaseFragment, "Database");
        adapter.addFragment(mStandardPropertyFragment, "Standart");
        adapter.addFragment(mExtendPropertyFragment, "Extend");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeFriendList();
    }
}

