package com.example.olga.photoeditor.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.FriendViewHolder;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.olga.photoeditor.mvp.presenter.FriendListPresenter;
import com.example.olga.photoeditor.mvp.view.FriendListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 05.07.16
 * Time: 19:31
 *
 * @author Olga
 */
public class FriendListActivity extends MvpActivity implements FriendListView {

    @BindView(R.id.friends_list_search_view)
    SearchView mSearchView;

    @BindView(R.id.friends_list_progress_bar_load)
    ProgressBar mLoadingBar;

    @BindView(R.id.friends_list_button_ok)
    Button mOkButton;

    @BindView(R.id.friends_list_button_cancel)
    Button mCancelButton;

    @BindView(R.id.friends_list_text_view_progress)
    TextView mProgressTextView;

    @BindView(R.id.friends_list_recycler_view_friends)
    RecyclerView mFriendsRecyclerView;

    @BindView(R.id.friends_list_text_view_nodata)
    TextView mNoDataTextView;

    @BindView(R.id.friends_list_text_view_failed)
    TextView mFailedTextView;

    private CollectionRecycleAdapter<Friend> mFriendAdapter;

    @InjectPresenter
    FriendListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_friends_list);
        ButterKnife.bind(this);

        //Search config

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconifiedByDefault(false);

        //Create adapter
        mFriendAdapter = new CollectionRecycleAdapter<Friend>(this) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new FriendViewHolder(LayoutInflater.from(FriendListActivity.this).inflate(R.layout.item_friend, parent, false));
            }
        };

        mFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFriendsRecyclerView.setAdapter(mFriendAdapter);

        initSwipe();

        mPresenter.userLoadFriends(this);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.userClickOk();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.userClickCancel();
            }
        });

    }

    @Override
    public void showProgress() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mProgressTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mLoadingBar.setVisibility(View.GONE);
        mProgressTextView.setVisibility(View.GONE);
    }

    @Override
    public void showFriendList() {
        mFriendsRecyclerView.setVisibility(View.VISIBLE);
        mOkButton.setVisibility(View.VISIBLE);
        mCancelButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFriendList() {
        mFriendsRecyclerView.setVisibility(View.GONE);
        mOkButton.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<Friend> friends) {
        mFriendAdapter.setCollection(friends);
    }

    @Override
    public void showEmpty() {
        mNoDataTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        mNoDataTextView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        mFailedTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        mFailedTextView.setVisibility(View.GONE);
    }

    private void initSwipe() {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        Friend friend = mFriendAdapter.getItem(viewHolder.getAdapterPosition());
                        mFriendAdapter.removeItem(viewHolder.getAdapterPosition());
                        mPresenter.userRemoveFriend(friend);
                    }
                });
        swipeToDismissTouchHelper.attachToRecyclerView(mFriendsRecyclerView);
    }
}


