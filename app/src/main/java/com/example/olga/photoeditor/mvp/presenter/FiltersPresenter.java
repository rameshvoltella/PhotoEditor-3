package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.Filter;
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

    private String mCurrentFilter;
    private List<Filter> mFilters;
    private FilterListener<String> mFilterListener;

    public void setFilterListener(FilterListener<String> filterListener) {
        mFilterListener = filterListener;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mCurrentFilter = Filter.NONE.name();
        mFilters = Filter.getFilterList();
    }

    public void userCheckFilter(int i) {
        String name = mFilters.get(i).name();
        if (mCurrentFilter != null && !mCurrentFilter.equals(name)) {
            mCurrentFilter = name;
            mFilterListener.userSetFilter(mCurrentFilter);
        }
    }

    public void userSelectFiltersTab() {
        getViewState().setFiltersList(mFilters);
    }

    public void userResetFilter() {
        mCurrentFilter = Filter.NONE.name();
    }

    public void userUpdateFiltersList() {
        Filter filter = Filter.valueOf(mCurrentFilter);
        String currentFilterName = filter.getFilterName();
        getViewState().checkCurrentFilter(currentFilterName);
    }

    //Listener
    public interface FilterListener<T> {
        void userSetFilter(T data);
    }

}

