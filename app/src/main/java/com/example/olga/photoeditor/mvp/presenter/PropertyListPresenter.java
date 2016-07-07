package com.example.olga.photoeditor.mvp.presenter;

import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.models.PropertyData;
import com.example.olga.photoeditor.mvp.view.PropertyListView;

/**
 * Date: 05.07.16
 * Time: 18:54
 *
 * @author Olga
 */
public class PropertyListPresenter extends MvpPresenter<PropertyListView> {

   public void userSelectStandardProperties(){
        getViewState().setData(PropertyData.getStandardProperties());
   }

    public void userSelectExtendProperties(){
        getViewState().setData(PropertyData.getExtendProperties());
    }


    public void userChangeValue() {

    }

}
