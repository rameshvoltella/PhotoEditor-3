package com.example.olga.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.EffectsLabel;

/**
 * Date: 01.07.16
 * Time: 09:51
 *
 * @author Olga
 */
public class ExtendPropertyFragment extends LoaderPropertiesFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPropertiesType = EffectsLabel.EXTEND.name();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.userSelectPropertiesTab(mPropertiesType);
    }

}