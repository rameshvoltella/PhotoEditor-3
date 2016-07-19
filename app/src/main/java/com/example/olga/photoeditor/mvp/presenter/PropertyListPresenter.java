package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.Property;
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

    static List<Property> mStandardProperties;
    static List<Property> mExtendProperties;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mStandardProperties = Property.getStandardProperties();
        mExtendProperties = Property.getExtendProperties();
    }

    public void userSelectProperties(String string) {
        if (string.equals("standard")) {
            getViewState().setData(mStandardProperties);
        }
        if (string.equals("extend")) {
            getViewState().setData(mExtendProperties);
        }
    }

    public void userUpdateProperties(String string) {
        if (string.equals("standard")) {
            getViewState().setData(mStandardProperties);
        }
        if (string.equals("extend")) {
            getViewState().setData(mExtendProperties);
        }
    }

}
