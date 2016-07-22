package com.example.olga.photoeditor.mvp.presenter;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
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
    }

    public void userSelectFiltersTab(RadioGroup radioGroup, Context context) {
        filters = Filter.getFilterList();
        for (int i = 0; i < filters.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(filters.get(i).getFilterName());
            radioGroup.addView(radioButton, i);
            if (i == 0) radioButton.setChecked(true);
        }
    }

    public void userCheckFilter(int index) {
        MainActivity.setCurrentEffect(filters.get(index).getFilterLabel());

    }

}

