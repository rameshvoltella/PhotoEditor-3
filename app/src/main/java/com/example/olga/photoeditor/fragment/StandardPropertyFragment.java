package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.olga.photoeditor.models.Effects.Property;

import java.util.ArrayList;
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
        mPresenter.userSelectStandardProperties();
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
