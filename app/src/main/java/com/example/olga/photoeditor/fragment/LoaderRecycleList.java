package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.PropertyViewHolder;
import com.example.olga.photoeditor.async.PropertyAsyncTask;
import com.example.olga.photoeditor.models.PropertyData;
import com.example.photoeditor.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 30.06.16
 * Time: 22:54
 *
 * @author Olga
 */
public abstract class LoaderRecycleList extends Fragment implements PropertyAsyncTask.Listener<List<PropertyData>> {

    @BindView(R.id.property_list_recycleview_list)
    RecyclerView mPropertyRecyclerView;

    private CollectionRecycleAdapter<PropertyData> mAdapter;

    private PropertyAsyncTask mPropertyAsyncTask;

    protected abstract PropertyAsyncTask createPropertyAsyncTask();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new CollectionRecycleAdapter<PropertyData>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PropertyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.photo_item_property, parent, false));
            }
        };

        mPropertyRecyclerView.setAdapter(mAdapter);
        mPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPropertyAsyncTask = createPropertyAsyncTask();
        mPropertyAsyncTask.execute();

        return view;
    }

    @Override
    public void startProgress() {
        mPropertyRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void stopProgress() {
        mPropertyRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void failedProgress() {
    }

    @Override
    public void updateProgress(int progress) {
    }

    @Override
    public void setResult(List<PropertyData> data) {
        mAdapter.setCollection(data);
    }

    @SuppressWarnings("unused")
    protected void reuseAsyncTask() {
        mPropertyAsyncTask.setListener(this);
    }

    @SuppressWarnings("unused")
    protected void restartAsyncTask() {
        if (mPropertyAsyncTask != null) {
            mPropertyAsyncTask.cancel(true);
            mPropertyAsyncTask.setListener(null);
        }
        mPropertyAsyncTask = createPropertyAsyncTask();
        mPropertyAsyncTask.execute();
    }

}
