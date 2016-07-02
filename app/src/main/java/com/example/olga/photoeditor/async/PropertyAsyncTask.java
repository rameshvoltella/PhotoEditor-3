package com.example.olga.photoeditor.async;

import android.content.Context;

import com.example.olga.photoeditor.models.PropertyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 30.06.16
 * Time: 22:35
 *
 * @author Olga
 */
public class PropertyAsyncTask extends ListAsyncTask<List<PropertyData>> {
    private List<PropertyData> mProperties = new ArrayList<>();

    public PropertyAsyncTask(Context context, Listener<List<PropertyData>> propertyListener, List<PropertyData> properties) {
        super(context, propertyListener);
        mProperties.clear();
        mProperties.addAll(properties);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<PropertyData> doInBackground(Void... voids) {
        return mProperties;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<PropertyData> properties) {
        super.onPostExecute(properties);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void setProperties(List<PropertyData> properties) {
        mProperties = properties;
    }
}
