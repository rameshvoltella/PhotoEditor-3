package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.PropertyViewHolder;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.presenter.PropertyListPresenter;
import com.example.olga.photoeditor.mvp.view.PropertyListView;
import com.example.olga.photoeditor.ui.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 30.06.16
 * Time: 22:54
 *
 * @author Olga
 */
public abstract class LoaderPropertiesFragment extends MvpFragment implements PropertyListView {

    @BindView(R.id.fragment_property_list_button_flip_hor)
    ImageButton mFlipHorButton;

    @BindView(R.id.fragment_property_list_button_flip_vert)
    ImageButton mFlipVertButton;

    @BindView(R.id.property_list_recycleview_list)
    RecyclerView mPropertyRecyclerView;

    @InjectPresenter
    PropertyListPresenter mPresenter;

    public static CollectionRecycleAdapter<Property> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new CollectionRecycleAdapter<Property>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PropertyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_property, parent, false));
            }
        };

        mPropertyRecyclerView.setAdapter(mAdapter);
        mPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFlipHorButton.setOnClickListener(view1 -> MainActivity.setFlip("FLIPHOR"));

        mFlipVertButton.setOnClickListener(view1 -> MainActivity.setFlip("FLIPVERT"));

        return view;
    }

    @Override
    public void showProperties() {
        mPropertyRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(List<Property> properties) {
        mAdapter.setCollection(properties);
    }
}
