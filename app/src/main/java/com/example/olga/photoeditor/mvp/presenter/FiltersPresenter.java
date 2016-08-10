package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.db.EffectDataSource;
import com.example.olga.photoeditor.models.EffectsLabel;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.models.PhotoEffect;
import com.example.olga.photoeditor.mvp.view.FiltersView;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Date: 08.07.16
 * Time: 17:03
 *
 * @author Olga
 */

@InjectViewState
public class FiltersPresenter extends MvpPresenter<FiltersView> {

    private String mCurrentFilter;
    private List<Filter> mFilters;
    private EffectDataSource mEffectDataSource;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Subscription mSubscription;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mFilters = Filter.getFilterList();
    }

    public void initEditor(EffectDataSource effectDataSource) {
        mEffectDataSource = effectDataSource;
    }

    public void userSelectFiltersTab() {
        getViewState().setFiltersList(mFilters);
    }

    public void userCheckFilter(int i) {
        String name = mFilters.get(i).name();
        if (mCurrentFilter != null && !mCurrentFilter.equals(name)) {
            mEffectDataSource.updateEffect(new PhotoEffect(mCurrentFilter, EffectsLabel.FILTER.name(), 0.0f));
            mEffectDataSource.updateEffect(new PhotoEffect(name, EffectsLabel.FILTER.name(), 1.0f));
            mCurrentFilter = name;
        }
    }

    public void userUpdateFiltersList() {
        mSubscription = getProperties()
                .repeat(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(effects -> {
                    for (int i = 0; i < effects.size(); i++) {
                        for (int j = 0; j < mFilters.size(); j++) {
                            if (effects.get(i).getEffectName().equals(mFilters.get(j).name())
                                    && effects.get(i).getEffectValue() == 1.0f) {
                                mCurrentFilter = mFilters.get(i).name();
                                String currentFilterName = mFilters.get(i).getFilterName();
                                getViewState().checkCurrentFilter(currentFilterName);
                            }
                        }
                    }
                });
    }

    private Observable<List<PhotoEffect>> getProperties() {
        return Observable.create((Observable.OnSubscribe<List<PhotoEffect>>) subscriber -> {
            subscriber.onNext(mEffectDataSource.getAllEffects());
            subscriber.onCompleted();
        });
    }

}

