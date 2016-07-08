package com.example.olga.photoeditor.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.olga.photoeditor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 30.06.16
 * Time: 22:32
 *
 * @author Olga
 */
public class DatabaseFragment extends Fragment {

    @BindView(R.id.database_fragment_button_Send)
    Button mSendButton;

    @BindView(R.id.database_fragment_checkbox_friends)
    CheckBox mFriendCheckBox;

    @BindView(R.id.database_fragment_frame_layout_container)
    FrameLayout mContainerLayuot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.database_fragment, container, false);
        ButterKnife.bind(this, view);

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFriendCheckBox.isChecked()) {
                    //MainActivity.getFriendList();
                } else {
                    Toast.makeText(getActivity(), R.string.publication, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //MainActivity.removeFriendList();
    }
}

