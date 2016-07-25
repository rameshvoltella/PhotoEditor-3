package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.mvp.view.FiltersView;
import com.example.olga.photoeditor.ui.MainActivity;

import java.util.List;


/**
 * Date: 08.07.16
 * Time: 17:03
 *
 * @author Olga
 */

@InjectViewState
public class FiltersPresenter extends MvpPresenter<FiltersView> {
    private List<Filter> filters;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        filters = Filter.getFilterList();
    }

    public void userCheckFilter(int id) {
        int i = 0;
        switch (id) {
            case R.id.fragment_filter_radio_button_current:
                i = 0;
                break;

            case R.id.fragment_filter_radio_button_crossprocess:
                i = 1;
                break;

            case R.id.fragment_filter_radio_button_documentary:
                i = 2;
                break;

            case R.id.fragment_filter_radio_button_grayscale:
                i = 3;
                break;

            case R.id.fragment_filter_radio_button_lomoish:
                i = 4;
                break;

            case R.id.fragment_filter_radio_button_negative:
                i = 5;
                break;

            case R.id.fragment_filter_radio_button_posterize:
                i = 6;
                break;

            case R.id.fragment_filter_radio_button_sepia:
                i = 7;
                break;
        }
        MainActivity.setCurrentEffect(filters.get(i).getFilterLabel());
        getViewState().selectFilter(id);
    }

}

