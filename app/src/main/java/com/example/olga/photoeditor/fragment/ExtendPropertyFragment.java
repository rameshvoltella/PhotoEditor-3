package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.Property;

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
    public void onResume() {
        super.onResume();
        mPresenter.userUpdateProperties("extend");
    }

    public static List<Property> getExtendProerties(){
        return mAdapter.getCollection();
    }
}