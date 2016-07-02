package com.example.olga.photoeditor.fragment;

import com.example.olga.photoeditor.async.PropertyAsyncTask;
import com.example.olga.photoeditor.models.PropertyData;

import java.util.List;

/**
 * Date: 30.06.16
 * Time: 22:33
 *
 * @author Olga
 */
public class StandardPropertyFragment extends LoaderRecycleList {

    private PropertyAsyncTask mPropertyAsyncTask;

    @Override
    protected PropertyAsyncTask createPropertyAsyncTask() {
        final List<PropertyData> standartProperties = PropertyData.getStandartProperties();
        return mPropertyAsyncTask = new PropertyAsyncTask(getContext(), this, standartProperties);
    }

    @Override
    protected void reuseAsyncTask() {
        mPropertyAsyncTask = createPropertyAsyncTask();
        mPropertyAsyncTask.execute();
    }
}
