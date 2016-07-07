package com.example.olga.photoeditor.mvp.presenter;

import android.content.Context;

import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.db.FriendDataSource;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.olga.photoeditor.mvp.view.FriendListView;

/**
 * Date: 05.07.16
 * Time: 19:03
 *
 * @author Olga
 */
public class FriendListPresenter extends MvpPresenter<FriendListView> {

    private FriendDataSource mFriendDataSource;

    public void userLoadFriends(Context context){
        mFriendDataSource = new FriendDataSource(context);

    }

    public void userRemoveFriend(Friend friend){
        mFriendDataSource.deleteFriend(friend);

    }

    public void userCheckFriend(){

    }

    public void userClickFindFriend(){

    }

    public void userClickOk(){

    }

    public void userClickCancel(){

    }

}
