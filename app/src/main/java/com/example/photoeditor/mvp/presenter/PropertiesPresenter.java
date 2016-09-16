package com.example.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.photoeditor.models.EffectsLabel;
import com.example.photoeditor.models.Property;
import com.example.photoeditor.mvp.view.PropertyListView;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:54
 *
 * @author Olga
 */

@InjectViewState
public class PropertiesPresenter extends MvpPresenter<PropertyListView> {

    private List<Property> mStandardProperties;
    private List<Property> mExtendProperties;
    private PropertyListener<Property, String> mPropertyListener;

    public void setPropertyListener(PropertyListener<Property, String> propertyListener) {
        mPropertyListener = propertyListener;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mStandardProperties = Property.getStandardProperties();
        mExtendProperties = Property.getExtendProperties();
    }

    public void userResetProperties() {
        for (int i = 0; i < mStandardProperties.size(); i++) {
            mStandardProperties.get(i).setCurrentValue(mStandardProperties.get(i).getDefaultValue());
        }
        for (int i = 0; i < mExtendProperties.size(); i++) {
            mExtendProperties.get(i).setCurrentValue(mExtendProperties.get(i).getDefaultValue());
        }
    }

    public void userSelectPropertiesTab(String string) {
        EffectsLabel effectsLabel = EffectsLabel.valueOf(string);
        switch (effectsLabel) {
            case STANDARD:
                getViewState().setData(mStandardProperties);
                break;
            case EXTEND:
                getViewState().setData(mExtendProperties);
                break;
        }
    }

    public void userClickButton(String flip) {
        mPropertyListener.userSetFlip(flip);
    }

    public void userChangePropertiesValue(Property property) {
        mPropertyListener.userSetProperties(property);
    }

    // Listener
    public interface PropertyListener<T, S> {
        void userSetProperties(T data);

        void userSetFlip(S flip);
    }
}
