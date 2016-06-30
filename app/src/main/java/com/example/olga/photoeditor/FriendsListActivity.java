package com.example.olga.photoeditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.FriendViewHolder;
import com.example.olga.photoeditor.db.FriendDataSource;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.photoeditor.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 22.06.16
 * Time: 21:53
 *
 * @author Olga
 */
public class FriendsListActivity extends AppCompatActivity implements FriendsListAsyncTask.FriendsListener {

    @BindView(R.id.friends_list_button_online)
    Button mOnlineButton;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        ButterKnife.bind(this);
        mOnlineButton.setVisibility(View.GONE);
        mSaveAllButton.setVisibility(View.GONE);

        dataSource = new FriendDataSource(this);

        mFriendAdapter = new CollectionRecycleAdapter<Friend>(this) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new FriendViewHolder(LayoutInflater.from(FriendsListActivity.this).inflate(R.layout.item_friend, parent, false));
            }
        };

        mFriendsRecyclerView.setAdapter(mFriendAdapter);
        mFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        saveState();

        initSwipe();

        mOnlineButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFriendAdapter.setCollection(dataSource.getOnlineFriends());
                if (dataSource.getOnlineFriends().isEmpty()) noData();
            }
        });

        mSaveAllButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < mFriendAdapter.getCollection().size(); i++) {
                    dataSource.saveFriend(mFriendAdapter.getCollection().get(i));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.load_from_database:
                if (databaseIsEmpty()) {
                    noData();
                    Toast.makeText(FriendsListActivity.this, R.string.database_empty, Toast.LENGTH_SHORT).show();
                }
                else {
                    noData();
                    loadFromDatabase();
                }
                return true;

            case R.id.load_from_network:
                noData();
                loadFromNetwork(10);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mFriendsListAsyncTask != null) {
            mFriendsListAsyncTask.cancel(true);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mFriendsListAsyncTask;
    }

    @Override
    public void startProgress() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mProgressTextView.setVisibility(View.VISIBLE);
        mFriendsRecyclerView.setVisibility(View.GONE);
        mNoDataTextView.setVisibility(View.GONE);
        mSaveAllButton.setVisibility(View.GONE);
        mOnlineButton.setVisibility(View.GONE);
        mOnlineButton.setEnabled(false);
        mSaveAllButton.setEnabled(false);
    }

    @Override
    public void stopProgress() {
        mLoadingBar.setVisibility(View.GONE);
        mProgressTextView.setVisibility(View.GONE);
        mFriendsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void failedProgress() {
        mLoadingBar.setVisibility(View.GONE);
        mProgressTextView.setVisibility(View.GONE);
        mFailedTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateProgress(int progress) {
        mProgressTextView.setText(getString(R.string.progress_format, progress));
    }

    @Override
    public void setResult(List<Friend> friends) {
        if (friends.isEmpty()) mNoDataTextView.setVisibility(View.VISIBLE);
        else {
            mNoDataTextView.setVisibility(View.GONE);
            mFriendAdapter.setCollection(friends);
            mOnlineButton.setEnabled(true);
            mSaveAllButton.setEnabled(true);
        }
    }

    private void noData() {
        mLoadingBar.setVisibility(View.GONE);
        mProgressTextView.setVisibility(View.GONE);
        mFriendsRecyclerView.setVisibility(View.GONE);
        mNoDataTextView.setVisibility(View.VISIBLE);
    }

    private boolean databaseIsEmpty() {
        return (dataSource.getAllFriends().size() == 0);
    }

    private void loadFromDatabase() {
        mFriendsListAsyncTask = new FriendsListAsyncTask(this, this);
        mFriendsListAsyncTask.execute();
        mOnlineButton.setVisibility(View.VISIBLE);
    }

    private void loadFromNetwork(int count) {
        mFriendsListAsyncTask = new FriendsListAsyncTask(this, this, count);
        mFriendsListAsyncTask.execute();
        mSaveAllButton.setVisibility(View.VISIBLE);
    }

    private void saveState(){
        mFriendsListAsyncTask = (FriendsListAsyncTask) getLastCustomNonConfigurationInstance();
        if (mFriendsListAsyncTask == null) noData();
        else mFriendsListAsyncTask.setFriendsListener(this);
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

                        dataSource.deleteFriend(mFriendAdapter.getCollection().get(viewHolder.getAdapterPosition()));

                        mFriendAdapter.removeItem(viewHolder.getAdapterPosition());

                        mFriendsListAsyncTask.setFriends(mFriendAdapter.getCollection());
                        if (mFriendAdapter.getCollection().isEmpty()) noData();
                    }
                });
        swipeToDismissTouchHelper.attachToRecyclerView(mFriendsRecyclerView);
    }

}
