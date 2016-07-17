package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.olga.photoeditor.models.Property;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:49
 *
 * @author Olga
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PropertyListView extends MvpView {

    void showProperties();

    void setData(List<Property> properties);

}
