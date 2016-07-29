package com.example.olga.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.mvp.presenter.FiltersPresenter;
import com.example.olga.photoeditor.mvp.view.FiltersView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterFragment extends MvpFragment implements FiltersView {

    @BindView(R.id.fragment_filter_radio_group)
    RadioGroup mFilterRadioGroup;

    @BindView(R.id.fragment_filter_radio_button_0)
    RadioButton mCurrentRadioButton;

    @BindView(R.id.fragment_filter_radio_button_1)
    RadioButton mCrossprocessRadioButton;

    @BindView(R.id.fragment_filter_radio_button_2)
    RadioButton mDocumentaryRadioButton;

    @BindView(R.id.fragment_filter_radio_button_3)
    RadioButton mGrayscaleRadioButton;

    @BindView(R.id.fragment_filter_radio_button_4)
    RadioButton mLomoishRadioButton;

    @BindView(R.id.fragment_filter_radio_button_5)
    RadioButton mNegativeRadioButton;

    @BindView(R.id.fragment_filter_radio_button_6)
    RadioButton mPosterizeRadioButton;

    @BindView(R.id.fragment_filter_radio_button_7)
    RadioButton mSepiaRadioButton;

    @InjectPresenter
    FiltersPresenter mPresenter;

    private List<Filter> mFilters;
    private List<RadioButton> mRadioButtons;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);

        mRadioButtons = new ArrayList<>(Arrays.asList(mCurrentRadioButton, mCrossprocessRadioButton,
                mDocumentaryRadioButton, mGrayscaleRadioButton, mLomoishRadioButton,
                mNegativeRadioButton, mPosterizeRadioButton, mSepiaRadioButton));
        mFilters = Filter.getFilterList();

        for (int i = 0; i < mRadioButtons.size(); i++) {
            String filterName = mFilters.get(i).getFilterName();
            mRadioButtons.get(i).setText(filterName);
        }

        mFilterRadioGroup.setOnCheckedChangeListener((group, checkedId) -> setFilter(checkedId));

        mPresenter.userUpdateFiltersList();

        return view;
    }

    @Override
    public void checkCurrentFilter(String name) {
        for (int i = 0; i < mRadioButtons.size(); i++) {
            RadioButton radioButton = mRadioButtons.get(i);
            if (radioButton.getText().equals(name)) {
                mFilterRadioGroup.check(radioButton.getId());
            }
        }
    }

    private void setFilter(int id) {
        String filterLabel = null;
        for (int i = 0; i < mRadioButtons.size(); i++) {
            if (mRadioButtons.get(i).getId() == id) {
                filterLabel = mFilters.get(i).getFilterLabel();
            }
        }
        mPresenter.userSetFilter(filterLabel);
    }

}
