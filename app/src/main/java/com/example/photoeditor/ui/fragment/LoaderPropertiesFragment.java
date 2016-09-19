package com.example.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
<<<<<<< HEAD:app/src/main/java/com/example/olga/photoeditor/ui/fragment/LoaderPropertiesFragment.java
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.PropertyViewHolder;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.models.Property;
import com.example.olga.photoeditor.mvp.presenter.PhotoEffectsPresenter;
import com.example.olga.photoeditor.mvp.presenter.PropertiesPresenter;
import com.example.olga.photoeditor.mvp.view.PropertyListView;
=======
import com.example.photoeditor.R;
import com.example.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.photoeditor.adapter.PropertyViewHolder;
import com.example.photoeditor.models.Filter;
import com.example.photoeditor.models.Property;
import com.example.photoeditor.mvp.presenter.PhotoEffectsPresenter;
import com.example.photoeditor.mvp.presenter.PropertiesPresenter;
import com.example.photoeditor.mvp.view.PropertyListView;
>>>>>>> master:app/src/main/java/com/example/photoeditor/ui/fragment/LoaderPropertiesFragment.java

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 30.06.16
 * Time: 22:54
 *
 * @author Olga
 */
public abstract class LoaderPropertiesFragment extends MvpSupportFragment implements PropertyListView {

    private static final String SET_LISTENER = "SET_LISTENER";

    @BindView(R.id.fragment_property_list_button_flip_hor)
    ImageButton mFlipHorButton;

    @BindView(R.id.fragment_property_list_button_flip_vert)
    ImageButton mFlipVertButton;

    @BindView(R.id.property_list_recycleview_list)
    RecyclerView mPropertyRecyclerView;

    protected String mPropertiesType;

    protected boolean mReset;

    @InjectPresenter
    PropertiesPresenter mPresenter;

    public CollectionRecycleAdapter<Property> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoEffectsPresenter activityPresenter = (PhotoEffectsPresenter) this.getArguments().getSerializable(SET_LISTENER);
        mPresenter.setPropertyListener(activityPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new CollectionRecycleAdapter<Property>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PropertyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_property, parent, false), mPresenter);
            }
        };
        mPropertyRecyclerView.setAdapter(mAdapter);
        mPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFlipHorButton.setOnClickListener(view1 -> mPresenter.userClickButton(Filter.FLIPHOR.name()));
        mFlipVertButton.setOnClickListener(view1 -> mPresenter.userClickButton(Filter.FLIPVERT.name()));

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

    public void resetProperties() {
        mReset = true;
    }

}
