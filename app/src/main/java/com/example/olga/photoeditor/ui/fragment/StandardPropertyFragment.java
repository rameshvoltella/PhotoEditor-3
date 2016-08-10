package com.example.olga.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.EffectsLabel;

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
        propertiesList = EffectsLabel.STANDARD.name();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.userSelectPropertiesTab(propertiesList);
    }
}
