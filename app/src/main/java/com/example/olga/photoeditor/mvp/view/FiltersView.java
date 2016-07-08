package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.example.olga.photoeditor.models.vkfriends.Friend;

import java.util.List;

/**
 * Date: 08.07.16
 * Time: 16:15
 *
 * @author Olga
 */
public interface FiltersView extends MvpView {

    // ProgressBar
    void showProgress();

    void hideProgress();

    //RecyclerView
    void showFiltersList();

    void hideFiltersList();

    void setData(List<Friend> friends);

    //Empty and error
    void showEmpty();

    void hideEmpty();


}
