package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.example.olga.photoeditor.models.vkfriends.Friend;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:59
 *
 * @author Olga
 */
public interface FriendListView extends MvpView {

    // ProgressBar
    void showProgress();

    void hideProgress();

    void setProgress(int percent);

    //RecyclerView
    void showFriendList();

    void hideFriendList();

    void setData(List<Friend> friends);

    //Empty and error
    void showEmpty();

    void hideEmpty();

    void showError();

    void hideError();

}
