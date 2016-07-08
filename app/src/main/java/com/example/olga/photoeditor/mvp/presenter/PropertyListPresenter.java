package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.PropertyData;
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
    List<PropertyData> mList;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void userSelectStandardProperties() {
        mList = PropertyData.getStandardProperties();
        getViewState().setData(mList);
    }

    public void userSelectExtendProperties() {
        mList = PropertyData.getExtendProperties();
        getViewState().setData(mList);
    }


    public void userChangeValue() {

    }

}
