package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.Property;

import java.util.List;

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
        mPresenter.userSelectProperties("standard");
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.userUpdateProperties("standard");
    }

    public static List<Property> getStandardProperties(){
        return mAdapter.getCollection();
    }
}
