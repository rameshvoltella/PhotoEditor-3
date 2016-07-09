package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.olga.photoeditor.models.Filter;

import java.util.List;

/**
 * Date: 08.07.16
 * Time: 16:15
 *
 * @author Olga
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FiltersView extends MvpView {

    // ProgressBar
    void showProgress();

    void hideProgress();

    //RecyclerView
    void showFiltersList();

    void hideFiltersList();

    void setData(List<Filter> filters);

    //Empty and error
    void showEmpty();

    void hideEmpty();


}
