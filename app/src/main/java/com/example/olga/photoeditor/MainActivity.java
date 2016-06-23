package com.example.olga.photoeditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.photoeditor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_image_view_photo)
    ImageView mPhotoImageView;

    @BindView(R.id.activity_main_button_Send)
    Button mSendButton;

    @BindView(R.id.activity_main_linear_layout_interface)
    LinearLayout mLinearLayout;

    @BindView(R.id.activity_main_checkbox_friends)
    CheckBox mFriendCheckBox;

    @BindView(R.id.activity_main_text_view_publication)
    TextView mPublicationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mFriendCheckBox.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, FriendsListActivity.class);
                    startActivity(intent);
                }
                else {
                    mLinearLayout.setVisibility(LinearLayout.GONE);
                    mPublicationTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
