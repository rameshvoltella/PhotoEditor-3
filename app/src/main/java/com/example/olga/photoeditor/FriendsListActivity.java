package com.example.olga.photoeditor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.photoeditor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    private static final String FRIENDS_DATA = "FRIENDS_DATA";
    private static List<Friend> mFriends;
    private FriendAdapter mFriendAdapter;
    private FriendsListAsyncTask mFriendsListAsyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        ButterKnife.bind(this);

        mFriends = new ArrayList<>();
        mFriendAdapter = new FriendAdapter(this);
        mFriendsRecyclerView.setAdapter(mFriendAdapter);
        mFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFriendsListAsyncTask = (FriendsListAsyncTask) getLastCustomNonConfigurationInstance();
        if (mFriendsListAsyncTask == null) {
            mFriendsListAsyncTask = new FriendsListAsyncTask(this, this);
            mFriendsListAsyncTask.execute();
        } else {
            mFriendsListAsyncTask.setFriendsListener(this);
            //noinspection ConstantConditions
            mFriends = (List<Friend>) savedInstanceState.getSerializable(FRIENDS_DATA);
            mFriendsListAsyncTask.onPostExecute(mFriends);
        }

        // init swipe to dismiss logic
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                mFriends.remove(viewHolder.getAdapterPosition());
                mFriendAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mFriendsRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFriendsListAsyncTask != null) {
            mFriendsListAsyncTask.cancel(true);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mFriendsListAsyncTask;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < mFriendAdapter.getItemCount(); i++) {
            friends.add(mFriendAdapter.getItem(i));
        }
        outState.putSerializable(FRIENDS_DATA, friends);
    }

    @Override
    public void startProgress() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mProgressTextView.setVisibility(View.VISIBLE);
        mFriendsRecyclerView.setVisibility(View.GONE);
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
        else mFriendAdapter.setCollection(friends);
    }

    static class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

        private Context mContext;
        private int mAvatarSize = 60;

        public FriendAdapter(Context context) {
            mContext = context;
        }

        public void setCollection(List<Friend> collection) {
            mFriends.addAll(collection);
            notifyDataSetChanged();
        }

        public Friend getItem(int position) {
            return mFriends.get(position);
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {

            Friend item = getItem(position);
            Picasso.with(mContext)
                    .load(item.getPhotoUrl())
                    .placeholder(R.mipmap.ic_user)
                    .resize(mAvatarSize, mAvatarSize).onlyScaleDown()
                    .into(holder.avatarImageView);

            holder.nameTextView.setText(item.getFirstName() + " " + item.getLastName());

            if (item.getOnline().equals("0")) {
                holder.onlineTextView.setText(R.string.offline);
            }

        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_friend_image_view_avatar)
        ImageView avatarImageView;

        @BindView(R.id.item_friend_text_view_name)
        TextView nameTextView;

        @BindView(R.id.item_friend_text_view_online)
        TextView onlineTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
