package com.example.olga.photoeditor.adapter;

import android.view.View;
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
public class FriendViewHolder extends CollectionRecycleAdapter.RecycleViewHolder<Friend>  implements View.OnClickListener{

    @BindView(R.id.item_friend_image_view_avatar)
    ImageView avatarImageView;

    @BindView(R.id.item_friend_text_view_name)
    TextView nameTextView;

    @BindView(R.id.item_friend_text_view_online)
    TextView onlineTextView;

    public IMyViewHolderClicks mListener;

    public FriendViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void create(View rootView) {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Friend model) {
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
    }

    @Override
    public void onClick(View v) {
        mListener.selectItem();
    }

    public interface IMyViewHolderClicks {
        public void selectItem();
    }
}


