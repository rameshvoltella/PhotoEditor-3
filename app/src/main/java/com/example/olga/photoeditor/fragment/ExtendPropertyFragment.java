package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.Effects.Property;

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
        mPresenter.userSelectExtendProperties();
    }

    public static List<Double> getValues(){
        List<Property> properties = mAdapter.getCollection();
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            values.add(properties.get(i).getDefaultValue());
        }
        return values;
    }
}