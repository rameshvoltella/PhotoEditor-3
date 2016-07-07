package com.example.olga.photoeditor.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.PropertyViewHolder;
import com.example.olga.photoeditor.models.PropertyData;
import com.example.olga.photoeditor.mvp.presenter.PropertyListPresenter;
import com.example.olga.photoeditor.mvp.view.PropertyListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 30.06.16
 * Time: 22:54
 *
 * @author Olga
 */
public abstract class LoaderRecycleList extends MvpFragment implements PropertyListView {

    @BindView(R.id.property_list_recycleview_list)
    RecyclerView mPropertyRecyclerView;

    @InjectPresenter

    PropertyListPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.property_list, container, false);
        ButterKnife.bind(this, view);

        CollectionRecycleAdapter<PropertyData> adapter = new CollectionRecycleAdapter<PropertyData>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PropertyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.photo_item_property, parent, false));
            }
        };

        mPropertyRecyclerView.setAdapter(adapter);
        mPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void showProperties() {
        mPropertyRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProperties() {
        mPropertyRecyclerView.setVisibility(View.GONE);
    }

}
