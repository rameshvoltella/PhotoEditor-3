package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.db.EffectDataSource;
import com.example.olga.photoeditor.models.EffectsLabel;
import com.example.olga.photoeditor.models.PhotoEffect;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.view.PropertyListView;

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
    private EffectDataSource mEffectDataSource;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Subscription mSubscription;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mStandardProperties = Property.getStandardProperties();
        mExtendProperties = Property.getExtendProperties();
    }

    public void initEditor(EffectDataSource effectDataSource) {
        mEffectDataSource = effectDataSource;
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
        //inverse current filter state
        PhotoEffect effect = mEffectDataSource.findEffect(flip);
        if (effect.getEffectValue() == 0.0f) {
            effect.setEffectValue(1.0f);
        } else {
            effect.setEffectValue(0.0f);
        }
        mEffectDataSource.updateEffect(effect);
    }

    public void userUpdateProperties() {
        mSubscription = getProperties()
                .repeat(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(effects -> {
                    for (int i = 0; i < effects.size(); i++) {
                        PhotoEffect effect = effects.get(i);
                        for (int x = 0; x < mStandardProperties.size(); x++) {
                            if (mStandardProperties.get(x).name().equals(effect.getEffectName())) {
                                mStandardProperties.get(x).setCurrentValue(effect.getEffectValue());
                            }
                        }
                        for (int y = 0; y < mExtendProperties.size(); y++) {
                            if (mExtendProperties.get(y).name().equals(effect.getEffectName())) {
                                mExtendProperties.get(y).setCurrentValue(effect.getEffectValue());
                            }

                        }
                    }
                });
    }

    private Observable<List<PhotoEffect>> getProperties() {
        return Observable.create((Observable.OnSubscribe<List<PhotoEffect>>) subscriber -> {
            try {
                subscriber.onNext(mEffectDataSource.getAllEffects());
                subscriber.onCompleted();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

}
