package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.olga.photoeditor.models.vkfriends.Friend;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:59
 *
 * @author Olga
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FriendListView extends MvpView {

    // ProgressBar
    void showProgress();

    void hideProgress();

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
