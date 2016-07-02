package com.example.olga.photoeditor.fragment;

import com.example.olga.photoeditor.async.PropertyAsyncTask;
import com.example.olga.photoeditor.models.PropertyData;

import java.util.List;

/**
 * Date: 01.07.16
 * Time: 09:51
 *
 * @author Olga
 */
public class ExtendPropertyFragment extends LoaderRecycleList {

    private PropertyAsyncTask mPropertyAsyncTask;

    @Override
    protected PropertyAsyncTask createPropertyAsyncTask() {
        final List<PropertyData> standartProperties = PropertyData.getExtendProperties();
        return mPropertyAsyncTask = new PropertyAsyncTask(getContext(), this, standartProperties);
    }

    @Override
    protected void reuseAsyncTask() {
        mPropertyAsyncTask = createPropertyAsyncTask();
        mPropertyAsyncTask.execute();
    }
}