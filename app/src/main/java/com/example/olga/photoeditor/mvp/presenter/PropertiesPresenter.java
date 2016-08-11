package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.EffectsLabel;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.view.PropertyListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Subscription mSubscription;
    private PropertyListener<List<Property>, String> mPropertyListener;

    public void setPropertyListener(PropertyListener<List<Property>, String> propertyListener) {
        mPropertyListener = propertyListener;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mStandardProperties = Property.getStandardProperties();
        mExtendProperties = Property.getExtendProperties();
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

    public void userChangePropertiesValue(String name, float value) {
        mSubscription = getCurrentProperties(name, value)
                .repeat(1)
                .subscribeOn(Schedulers.io())
                .map(properties1 -> {
                    List<Property> changedProperties = new ArrayList<>();
                    for (int i = 0; i < properties1.size(); i++) {
                        Property property = properties1.get(i);
                        if (property.getCurrentValue() != property.getDefaultValue()) {
                            changedProperties.add(properties1.get(i));
                        }
                    }
                    return changedProperties;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(properties -> {
                    mPropertyListener.userSetProperties(properties);
                });
    }

    private Observable<List<Property>> getCurrentProperties(String name, float value) {
        return Observable.create((Observable.OnSubscribe<List<Property>>) subscriber -> {
            subscriber.onNext(getProperties(name, value));
            subscriber.onCompleted();
        });
    }

    private List<Property> getProperties(String name, float value) {

        for (int i = 0; i < mStandardProperties.size(); i++) {
            if (mStandardProperties.get(i).getPropertyName().equals(name)) {
                mStandardProperties.get(i).setCurrentValue(value);
            }
        }
        for (int i = 0; i < mExtendProperties.size(); i++) {
            if (mExtendProperties.get(i).getPropertyName().equals(name)) {
                mExtendProperties.get(i).setCurrentValue(value);
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.addAll(mStandardProperties);
        properties.addAll(mExtendProperties);
        return properties;
    }

    // Listener
    public interface PropertyListener<T, S> {
        void userSetProperties(T data);

        void userSetFlip(S flip);
    }
}
