package com.example.olga.photoeditor;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photoeditor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_image_view_photo)
    ImageView mPhotoImageView;

    @BindView(R.id.activity_main_button_Send)
    Button mSendButton;

    @BindView(R.id.activity_main_checkbox_friends)
    CheckBox mFriendCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFriendCheckBox.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, FriendsListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.publication, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
