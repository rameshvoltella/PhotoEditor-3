package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.Filter;
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

    public static void userUpdateValues(Filter filter) {
        mStandardProperties.get(0).setDefaultValue(filter.getBrightnessValue());
        mStandardProperties.get(1).setDefaultValue(filter.getContrastValue());
        mStandardProperties.get(2).setDefaultValue(filter.getSaturateValue());
        mStandardProperties.get(3).setDefaultValue(filter.getSharpenValue());
        mExtendProperties.get(0).setDefaultValue(filter.getAutofixValue());
        mExtendProperties.get(1).setDefaultValue(filter.getBlackValue());
        mExtendProperties.get(2).setDefaultValue(filter.getWhiteValue());
        mExtendProperties.get(3).setDefaultValue(filter.getFillightValue());
        mExtendProperties.get(4).setDefaultValue(filter.getGrainValue());
        mExtendProperties.get(5).setDefaultValue(filter.getTemperatureValue());
        mExtendProperties.get(6).setDefaultValue(filter.getFisheyeValue());
        mExtendProperties.get(7).setDefaultValue(filter.getVignetteValue());
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
