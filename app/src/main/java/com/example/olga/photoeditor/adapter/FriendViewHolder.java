package com.example.olga.photoeditor.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 25.06.16
 * Time: 13:38
 *
 * @author Olga
 */
public class FriendViewHolder extends CollectionRecycleAdapter.RecycleViewHolder<Friend> {

    @BindView(R.id.item_friend_cardview)
    CardView mItemCardView;

    @BindView(R.id.item_friend_image_view_avatar)
    ImageView avatarImageView;

    @BindView(R.id.item_friend_text_view_name)
    TextView nameTextView;

    @BindView(R.id.item_friend_text_view_online)
    TextView onlineTextView;

    @BindView(R.id.item_friend_check_friend)
    CheckBox mCheckBox;

    public FriendViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void create(View rootView) {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final Friend model) {
        int avatarSize = 60;
        Picasso.with(getRoot().getContext())
                .load(model.getPhotoUrl())
                .placeholder(R.mipmap.ic_user)
                .resize(avatarSize, avatarSize).onlyScaleDown()
                .into(avatarImageView);

        nameTextView.setText(model.getFirstName() + " " + model.getLastName());

        if (model.getOnline().equals("1")) {
            onlineTextView.setText(R.string.online);
        }

        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                model.setCheckFriend(true);
            } else {
                model.setCheckFriend(false);
            }
        });
    }
}


