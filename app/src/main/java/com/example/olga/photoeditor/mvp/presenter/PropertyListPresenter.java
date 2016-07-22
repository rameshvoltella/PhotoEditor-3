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

    public void userUpdateProperties(String string) {
        if (string.equals("standard")) {
            getViewState().setData(mStandardProperties);
        }
        if (string.equals("extend")) {
            getViewState().setData(mExtendProperties);
        }
    }

    public static void userChangePropertiesValue() {
        List<Property> properties = StandardPropertyFragment.getStandardProerties();
        if (ExtendPropertyFragment.getExtendProerties().size() != 4) {
            properties.addAll(ExtendPropertyFragment.getExtendProerties());
        }
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            if (property.getCurrentValue() == property.getDefaultValue()) {
                properties.remove(i);
            }
        }
        MainActivity.setChangedProperties(properties);
    }
}
