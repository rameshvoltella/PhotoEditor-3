package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.ui.fragment.ExtendPropertyFragment;
import com.example.olga.photoeditor.ui.fragment.StandardPropertyFragment;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.view.PropertyListView;
import com.example.olga.photoeditor.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
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
public class PropertyListPresenter extends MvpPresenter<PropertyListView> {

    static List<Property> mStandardProperties;
    static List<Property> mExtendProperties;
    static Subscription mSubscription;

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

    public void userSaveProperties(String string) {
        if (string.equals("standard")) {
            mStandardProperties.clear();
            mStandardProperties.addAll(StandardPropertyFragment.getStandardProperties());
        }
        if (string.equals("extend")) {
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
        mSubscription = getStandardProperties()
                .repeat(1)
                .subscribeOn(Schedulers.io())
                .map(properties1 -> {
                    getExtendProperties().subscribe(new Subscriber<List<Property>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<Property> extendProperties) {
                            if (extendProperties.size() == 7) {
                                properties1.addAll(extendProperties);
                            }
                        }
                    });
                    return properties1;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(properties -> {
                    List<Property> changedProperties = new ArrayList<>();
                    for (int i = 0; i < properties.size(); i++) {
                        Property property = properties.get(i);
                        if (property.getCurrentValue() != property.getDefaultValue()) {
                            changedProperties.add(properties.get(i));
                        }
                    }
                    MainActivity.setChangedProperties(changedProperties);
                });
    }

    private static Observable<List<Property>> getStandardProperties() {
        return Observable.create((Observable.OnSubscribe<List<Property>>) subscriber -> {
            subscriber.onNext(StandardPropertyFragment.getStandardProperties());
            subscriber.onCompleted();
        });
    }

    private static Observable<List<Property>> getExtendProperties() {
        return Observable.create((Observable.OnSubscribe<List<Property>>) subscriber -> {
            subscriber.onNext(ExtendPropertyFragment.getExtendProerties());
            subscriber.onCompleted();
        });
    }
}
