package com.example.olga.photoeditor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photoeditor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 25.06.16
 * Time: 13:38
 *
 * @author Olga
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

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
