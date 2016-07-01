package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.olga.photoeditor.MainActivity;
import com.example.photoeditor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 30.06.16
 * Time: 22:32
 *
 * @author Olga
 */
public class DatabaseFragment extends Fragment {

    private String FRIEND_BUTTON = "FRIEND_BUTTON";

    @BindView(R.id.database_fragment_image_view_photo)
    ImageView mPhotoImageView;

    @BindView(R.id.database_fragment_button_Send)
    Button mSendButton;

    @BindView(R.id.database_fragment_checkbox_friends)
    CheckBox mFriendCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.database_fragment, container, false);
        ButterKnife.bind(this, view);

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFriendCheckBox.isChecked()) {
                    MainActivity.getFriendList();
                } else {
                    Toast.makeText(getContext(), R.string.publication, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }*/
}