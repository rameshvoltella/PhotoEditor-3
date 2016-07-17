package com.example.olga.photoeditor.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.db.FilterDataSource;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.mvp.view.FiltersView;
import com.example.olga.photoeditor.ui.MainActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
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

    private FilterDataSource mFilterDataSource;
    Subscription mSubscription;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void userLoadFilters(Context context) {
        mFilterDataSource = new FilterDataSource(context);

        mSubscription = getFiltersList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Filter>>() {
                    @Override
                    public void onCompleted() {
                        getViewState().hideProgress();
                        getViewState().hideEmpty();
                        getViewState().showFiltersList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgress();
                        getViewState().showEmpty();
                    }

                    @Override
                    public void onNext(List<Filter> filters) {
                        filters = mFilterDataSource.getAllFilters();
                        if ((filters.isEmpty())) {
                            filters = Filter.getStandardFilters();
                            for (int i = 0; i < filters.size(); i++) {
                                mFilterDataSource.saveFilter(filters.get(i));
                            }
                        }
                        getViewState().setData(filters);
                    }
                });
    }

    public static void userCheckFilter(Filter filter) {
        MainActivity.setCurrentEffect("Яркость", filter.getBrightnessValue());
        MainActivity.setCurrentEffect("Контрастность", filter.getContrastValue());
        MainActivity.setCurrentEffect("Насыщенность", filter.getSaturateValue());
        MainActivity.setCurrentEffect("Резкость", filter.getSharpenValue());
        MainActivity.setCurrentEffect("Автокоррекция", filter.getAutofixValue());
        MainActivity.setCurrentEffect("Уровень черного", filter.getBlackValue());
        MainActivity.setCurrentEffect("Уровень белого", filter.getWhiteValue());
        MainActivity.setCurrentEffect("Заполняющий свет", filter.getFillightValue());
        MainActivity.setCurrentEffect("Зернистость", filter.getGrainValue());
        MainActivity.setCurrentEffect("Температура", filter.getTemperatureValue());
        MainActivity.setCurrentEffect("Объектив", filter.getFisheyeValue());
        MainActivity.setCurrentEffect("Виньетка", filter.getVignetteValue());
        PropertyListPresenter.userUpdateValues(filter);
    }

    public void userCreateFilter(Filter filter) {
        mFilterDataSource.saveFilter(filter);
        mSubscription = getFiltersList()
                .timeout(1, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Filter>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideFiltersList();
                        getViewState().showEmpty();
                    }

                    @Override
                    public void onNext(List<Filter> filters) {
                        getViewState().setData(filters);
                    }
                });
    }

    public void userRemoveFilter(Filter filter) {
        mFilterDataSource.deleteFilter(filter);
    }

    @NonNull
    private Observable<List<Filter>> getFiltersList() {
        return Observable.create((Observable.OnSubscribe<List<Filter>>) subscriber ->
        {
            subscriber.onNext(mFilterDataSource.getAllFilters());
            subscriber.onCompleted();
            subscriber.onError(new Exception(""));
        });
    }

    public void userChangeCurrentFilter(Filter currentFilter) {
        currentFilter.setFilterName("current");
        mFilterDataSource.saveFilter(currentFilter);
    }
}
