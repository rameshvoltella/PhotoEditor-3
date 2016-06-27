package com.example.olga.photoeditor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.photoeditor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 25.06.16
 * Time: 13:38
 *
 * @author Olga
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    protected LayoutInflater mInflater;
    private Context mContext;
    private final List<Friend> mFriends;

    public FriendAdapter(Context context) {
        mContext = context.getApplicationContext();
        mInflater = LayoutInflater.from(context);
        mFriends = new ArrayList<>();
    }

    public void setCollection(List<Friend> collection) {

        mFriends.clear();
        if (collection != null) mFriends.addAll(collection);
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
        int avatarSize = 60;
        Picasso.with(mContext)
                .load(item.getPhotoUrl())
                .placeholder(R.mipmap.ic_user)
                .resize(avatarSize, avatarSize).onlyScaleDown()
                .into(holder.avatarImageView);

        holder.nameTextView.setText(item.getFirstName() + " " + item.getLastName());

        if (item.getOnline().equals("1")) {
            holder.onlineTextView.setText(R.string.online);
        }
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public List<Friend> getFriends() {
        return mFriends;
    }

    public void removeItem(int i){
        if (mFriends != null) {
            mFriends.remove(i);
            notifyItemRemoved(i);
        }
    }
}
