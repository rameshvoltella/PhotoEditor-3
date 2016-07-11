package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.Effects.Filter;
import com.example.olga.photoeditor.models.Effects.Property;
import com.example.olga.photoeditor.mvp.view.PropertyListView;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:54
 *
 * @author Olga
 */

@InjectViewState
public class PropertyListPresenter extends MvpPresenter<PropertyListView> {
    List<Property> mList;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void userSelectStandardProperties() {
        mList = Filter.getStandardProperties();
        getViewState().setData(mList);
    }

    public void userSelectExtendProperties() {
        mList = Filter.getExtendProperties();
        getViewState().setData(mList);
    }

}
