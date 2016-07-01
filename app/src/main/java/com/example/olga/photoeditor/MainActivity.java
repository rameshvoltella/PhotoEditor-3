package com.example.olga.photoeditor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.olga.photoeditor.async.FriendsListAsyncTask;
import com.example.olga.photoeditor.fragment.DatabaseFragment;
import com.example.olga.photoeditor.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.fragment.FriendListFragment;
import com.example.olga.photoeditor.fragment.StandartPropertyFragment;
import com.example.photoeditor.R;

public class MainActivity extends AppCompatActivity {

    private static final String FRIEND_LIST_TAG = "FRIEND_LIST_TAG";
    private static final String ASYNC_TASK = "ASYNC_TASK";
    private static DatabaseFragment mDatabaseFragment;
    private static FriendListFragment mFriendListFragment;
    private static StandartPropertyFragment mStandartPropertyFragment;
    private static ExtendPropertyFragment mExtendPropertyFragment;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseFragment = new DatabaseFragment();
        mStandartPropertyFragment = new StandartPropertyFragment();
        mExtendPropertyFragment = new ExtendPropertyFragment();
        mFriendListFragment = new FriendListFragment();

        SaveInstanseArguments();
        fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(FRIEND_LIST_TAG) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_frame_layout_fragment, mDatabaseFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            getFriendList();
        }

    }

    public static void getFriendList() {
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_frame_layout_fragment, mFriendListFragment, FRIEND_LIST_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void SaveInstanseArguments(){
        FriendsListAsyncTask friendsListAsyncTask = (FriendsListAsyncTask) getLastCustomNonConfigurationInstance();
        if (friendsListAsyncTask != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(ASYNC_TASK, friendsListAsyncTask);
            mFriendListFragment.setArguments(arguments);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mFriendListFragment.getFriendsListAsyncTask();
    }
}
