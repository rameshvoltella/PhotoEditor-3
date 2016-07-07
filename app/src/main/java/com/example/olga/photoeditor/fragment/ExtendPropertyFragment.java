package com.example.olga.photoeditor.fragment;

import com.example.olga.photoeditor.models.PropertyData;

import java.util.List;

/**
 * Date: 01.07.16
 * Time: 09:51
 *
 * @author Olga
 */
public class ExtendPropertyFragment extends LoaderRecycleList {

    @Override
    public void setData(List<PropertyData> properties) {
        mPresenter.userSelectExtendProperties();
    }
}