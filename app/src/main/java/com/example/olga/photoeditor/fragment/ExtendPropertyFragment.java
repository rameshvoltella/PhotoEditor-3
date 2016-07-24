package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.Property;

import java.util.ArrayList;
import java.util.List;

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
        mPresenter.userSelectProperties("extend");
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.userSaveProperties("extend");
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.userUpdateProperties("extend");
    }

    public static List<Property> getExtendProerties(){
        List<Property> properties = new ArrayList<>();
        properties.addAll(mAdapter.getCollection());
        return properties;
    }
}