package com.example.olga.photoeditor.fragment;

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
import com.example.olga.photoeditor.mvp.presenter.FiltersPresenter;
import com.example.olga.photoeditor.mvp.view.FiltersView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterFragment extends MvpFragment implements FiltersView {

    @BindView(R.id.fragment_filter_radio_group)
    RadioGroup mFilterRadioGroup;

    @BindView(R.id.fragment_filter_radio_button_current)
    RadioButton mCurrentRadioButton;

    @BindView(R.id.fragment_filter_radio_button_crossprocess)
    RadioButton mCrossprocessRadioButton;

    @BindView(R.id.fragment_filter_radio_button_documentary)
    RadioButton mDocumentaryRadioButton;

    @BindView(R.id.fragment_filter_radio_button_grayscale)
    RadioButton mGrayscaleRadioButton;

    @BindView(R.id.fragment_filter_radio_button_lomoish)
    RadioButton mLomoishRadioButton;

    @BindView(R.id.fragment_filter_radio_button_negative)
    RadioButton mNegativeRadioButton;

    @BindView(R.id.fragment_filter_radio_button_posterize)
    RadioButton mPosterizeRadioButton;

    @BindView(R.id.fragment_filter_radio_button_sepia)
    RadioButton mSepiaRadioButton;

    @InjectPresenter
    FiltersPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);

        mFilterRadioGroup.check(R.id.fragment_filter_radio_button_current);

        mFilterRadioGroup.setOnCheckedChangeListener((group, checkedId) -> mPresenter.userCheckFilter(checkedId));

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mFilterRadioGroup.clearCheck();
    }
}
