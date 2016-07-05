package com.example.olga.photoeditor.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.example.olga.photoeditor.models.PropertyData;

import java.util.List;

/**
 * Date: 05.07.16
 * Time: 18:49
 *
 * @author Olga
 */
public interface PropertyListView extends MvpView {

    void showProperties();
    void setData(List<PropertyData> properties);


}
