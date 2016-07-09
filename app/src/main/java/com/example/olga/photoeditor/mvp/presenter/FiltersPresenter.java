package com.example.olga.photoeditor.mvp.presenter;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.db.FilterDataSource;
import com.example.olga.photoeditor.models.Effects.Filter;
import com.example.olga.photoeditor.mvp.view.FiltersView;

import java.util.List;

/**
 * Date: 08.07.16
 * Time: 17:03
 *
 * @author Olga
 */

@InjectViewState
public class FiltersPresenter extends MvpPresenter<FiltersView> {

    private FilterDataSource mFilterDataSource;
    private Context mContext;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void userLoadFilters(Context context) {
        mContext = context;
        mFilterDataSource = new FilterDataSource(mContext);

        List<Filter> filterList = mFilterDataSource.getAllFilters();

        if (filterList.isEmpty()) {
            filterList = Filter.getStandartFilters();
            for (int i = 0; i < filterList.size(); i++) {
                mFilterDataSource.saveFilter(filterList.get(i));
            }
        }
        getViewState().hideProgress();
        getViewState().showFiltersList();
        getViewState().setData(filterList);
    }

    public void userCheckFilter() {
    }

    public void userCreateFilter() {
    }

    public void userRemoveFilter(Filter filter) {
        mFilterDataSource.deleteFilter(filter);
    }


}
