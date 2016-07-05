package com.example.olga.photoeditor.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpActivity;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.async.FriendsListAsyncTask;
import com.example.olga.photoeditor.db.FriendDataSource;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.olga.photoeditor.mvp.view.FriendListView;
import com.example.photoeditor.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 05.07.16
 * Time: 19:31
 *
 * @author Olga
 */
public class FriendListActivity extends MvpActivity implements FriendListView{

    private static final String ASYNC_TASK = "ASYNC_TASK";
    @BindView(R.id.friends_list_button_search)
    Button mSearchButton;

    @BindView(R.id.friends_list_search_view)
    SearchView mSearchView;

    @BindView(R.id.friends_list_button_save)
    Button mSaveAllButton;

    @BindView(R.id.friends_list_progress_bar_load)
    ProgressBar mLoadingBar;

    @BindView(R.id.friends_list_text_view_progress)
    TextView mProgressTextView;

    @BindView(R.id.friends_list_recycler_view_friends)
    RecyclerView mFriendsRecyclerView;

    @BindView(R.id.friends_list_text_view_nodata)
    TextView mNoDataTextView;

    @BindView(R.id.friends_list_text_view_failed)
    TextView mFailedTextView;

    private CollectionRecycleAdapter<Friend> mFriendAdapter;

    private FriendsListAsyncTask mFriendsListAsyncTask;

    private FriendDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.friends_list);
        ButterKnife.bind(this);
    }

    @Override
    public void showProgress() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mProgressTextView.setVisibility(View.VISIBLE);
        mNoDataTextView.setVisibility(View.GONE);
        mFailedTextView.setVisibility(View.GONE);

    }

    @Override
    public void hideProgress() {
        mLoadingBar.setVisibility(View.GONE);
        mProgressTextView.setVisibility(View.GONE);
    }

    @Override
    public void showFriendList() {
        mFriendsRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideFriendList() {
        mFriendsRecyclerView.setVisibility(View.GONE);
        mSaveAllButton.setVisibility(View.GONE);
        mSearchButton.setVisibility(View.GONE);
        mSearchView.setVisibility(View.GONE);
        mSaveAllButton.setEnabled(false);

    }

    @Override
    public void setData(List<Friend> friends) {

    }

    @Override
    public void showEmpty() {
        mNoDataTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showError() {
        mFailedTextView.setVisibility(View.VISIBLE);

    }
}
