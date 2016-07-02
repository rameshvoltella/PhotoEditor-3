package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.FriendViewHolder;
import com.example.olga.photoeditor.async.FriendsListAsyncTask;
import com.example.olga.photoeditor.db.FriendDataSource;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.photoeditor.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 01.07.16
 * Time: 09:55
 *
 * @author Olga
 */
public class FriendListFragment extends Fragment implements FriendsListAsyncTask.Listener<List<Friend>> {

    private static final String ASYNC_TASK = "ASYNC_TASK";
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFriendsListAsyncTask = (FriendsListAsyncTask) arguments.getSerializable(ASYNC_TASK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friends_list, container, false);
        ButterKnife.bind(this, view);

        mOnlineButton.setVisibility(View.GONE);
        mSaveAllButton.setVisibility(View.GONE);
        noData();

        dataSource = new FriendDataSource(getActivity());

        mFriendAdapter = new CollectionRecycleAdapter<Friend>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new FriendViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_friend, parent, false));
            }
        };

        mFriendsRecyclerView.setAdapter(mFriendAdapter);
        mFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mFriendsListAsyncTask == null) noData();
        else mFriendsListAsyncTask.setListener(this);

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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friend_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.load_from_database:
                if (databaseIsEmpty()) {
                    noData();
                    Toast.makeText(getContext(), R.string.database_empty, Toast.LENGTH_SHORT).show();
                } else {
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
        mFriendsListAsyncTask = new FriendsListAsyncTask(getContext(), this);
        mFriendsListAsyncTask.execute();
        mOnlineButton.setVisibility(View.VISIBLE);
    }

    private void loadFromNetwork(int count) {
        mFriendsListAsyncTask = new FriendsListAsyncTask(getContext(), this, count);
        mFriendsListAsyncTask.execute();
        mSaveAllButton.setVisibility(View.VISIBLE);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ASYNC_TASK, mFriendsListAsyncTask);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFriendsListAsyncTask.setListener(null);
    }

    public FriendsListAsyncTask getFriendsListAsyncTask() {
        return mFriendsListAsyncTask;
    }
}
