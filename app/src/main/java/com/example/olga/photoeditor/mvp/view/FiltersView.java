package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 08.07.16
 * Time: 16:15
 *
 * @author Olga
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FiltersView extends MvpView {

    void checkCurrentFilter(String name);

}
