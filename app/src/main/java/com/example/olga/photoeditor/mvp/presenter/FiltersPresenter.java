package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.mvp.view.FiltersView;
import com.example.olga.photoeditor.ui.MainActivity;


/**
 * Date: 08.07.16
 * Time: 17:03
 *
 * @author Olga
 */

@InjectViewState
public class FiltersPresenter extends MvpPresenter<FiltersView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void userSetFilter(String name) {
        MainActivity.setCurrentFilter(name);
    }

    public void userUpdateFiltersList() {
        String currentFilterLabel = MainActivity.getmCurrentFilter();
        Filter filter = Filter.valueOf(currentFilterLabel);
        String currentFilterName = filter.getFilterName();
        getViewState().checkCurrentFilter(currentFilterName);
    }

}

