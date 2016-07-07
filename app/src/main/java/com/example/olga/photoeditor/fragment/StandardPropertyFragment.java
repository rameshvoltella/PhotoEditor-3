package com.example.olga.photoeditor.fragment;

import com.example.olga.photoeditor.models.PropertyData;

import java.util.List;

/**
 * Date: 30.06.16
 * Time: 22:33
 *
 * @author Olga
 */
public class StandardPropertyFragment extends LoaderRecycleList {

    @Override
    public void setData(List<PropertyData> properties) {
        mPresenter.userSelectStandardProperties();
    }
}
