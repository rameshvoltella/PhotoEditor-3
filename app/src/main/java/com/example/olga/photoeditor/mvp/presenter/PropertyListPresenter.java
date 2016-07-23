package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.fragment.StandardPropertyFragment;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.view.PropertyListView;
import com.example.olga.photoeditor.ui.MainActivity;

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

    public void userSaveProperties() {
        mStandardProperties.clear();
        mStandardProperties.addAll(StandardPropertyFragment.getStandardProperties());
        if (ExtendPropertyFragment.getExtendProerties().size() == 7) {
            mExtendProperties.clear();
            mExtendProperties.addAll(ExtendPropertyFragment.getExtendProerties());
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

    public static void userChangePropertiesValue() {
        List<Property> changedProperties = StandardPropertyFragment.getStandardProperties();
        List<Property> properties = ExtendPropertyFragment.getExtendProerties();
        if (properties.size() == 7) {
            changedProperties.addAll(properties);
        }
        properties.clear();
        for (int i = 0; i < changedProperties.size(); i++) {
            Property property = properties.get(i);
            if (property.getCurrentValue() != property.getDefaultValue()) {
                properties.add(changedProperties.get(i));
            }
        }
        if (properties.size() != 0) {
            MainActivity.setChangedProperties(properties);
        }
    }
}
