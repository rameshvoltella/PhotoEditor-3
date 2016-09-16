package com.example.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.photoeditor.models.EffectsLabel;

/**
 * Date: 30.06.16
 * Time: 22:33
 *
 * @author Olga
 */
public class StandardPropertyFragment extends LoaderPropertiesFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPropertiesType = EffectsLabel.STANDARD.name();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mReset) {
            mPresenter.userResetProperties();
            mReset = false;
        }
        mPresenter.userSelectPropertiesTab(mPropertiesType);
    }

}
