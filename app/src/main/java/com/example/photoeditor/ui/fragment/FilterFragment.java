package com.example.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.example.photoeditor.R;
import com.example.photoeditor.models.Filter;
import com.example.photoeditor.mvp.presenter.FiltersPresenter;
import com.example.photoeditor.mvp.presenter.PhotoEffectsPresenter;
import com.example.photoeditor.mvp.view.FiltersView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterFragment extends MvpSupportFragment implements FiltersView {

    private static final String SET_LISTENER = "SET_LISTENER";
    private static final String FILTER = "FILTER";
    @BindView(R.id.fragment_filter_radio_group)
    RadioGroup mFilterRadioGroup;

    @BindView(R.id.fragment_filter_radio_button_0)
    RadioButton mRadioButton0;

    @BindView(R.id.fragment_filter_radio_button_1)
    RadioButton mRadioButton1;

    @BindView(R.id.fragment_filter_radio_button_2)
    RadioButton mRadioButton2;

    @BindView(R.id.fragment_filter_radio_button_3)
    RadioButton mRadioButton3;

    @BindView(R.id.fragment_filter_radio_button_4)
    RadioButton mRadioButton4;

    @BindView(R.id.fragment_filter_radio_button_5)
    RadioButton mRadioButton5;

    @BindView(R.id.fragment_filter_radio_button_6)
    RadioButton mRadioButton6;

    @BindView(R.id.fragment_filter_radio_button_7)
    RadioButton mRadioButton7;

    @InjectPresenter(tag = FILTER, type = PresenterType.GLOBAL)
    FiltersPresenter mPresenter;

    private List<RadioButton> mRadioButtons;

    private boolean mReset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoEffectsPresenter activityPresenter = (PhotoEffectsPresenter) this.getArguments().getSerializable(SET_LISTENER);
        mPresenter.setFilterListener(activityPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);
        mRadioButtons = new ArrayList<>(Arrays.asList(mRadioButton0, mRadioButton1, mRadioButton2,
                mRadioButton3, mRadioButton4, mRadioButton5, mRadioButton6, mRadioButton7));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.userSelectFiltersTab();
        if (mReset) {
            mPresenter.userResetFilter();
            mReset = false;
        }
        mPresenter.userUpdateFiltersList();
        mFilterRadioGroup.setOnCheckedChangeListener((group, checkedId) -> setFilter(checkedId));
    }

    @Override
    public void setFiltersList(List<Filter> filters) {
        for (int i = 0; i < filters.size(); i++) {
            mRadioButtons.get(i).setText(filters.get(i).getFilterName());
        }
    }

    @Override
    public void checkCurrentFilter(String name) {
        for (int i = 0; i < mRadioButtons.size(); i++) {
            RadioButton radioButton = mRadioButtons.get(i);
            if (radioButton.getText().toString().equals(name)) {
                mFilterRadioGroup.check(radioButton.getId());
            }
        }
    }

    public void resetFilters() {
        mReset = true;
    }

    private void setFilter(int id) {
        for (int i = 0; i < mRadioButtons.size(); i++) {
            if (mRadioButtons.get(i).getId() == id) {
                mPresenter.userCheckFilter(i);
            }
        }
    }

}
