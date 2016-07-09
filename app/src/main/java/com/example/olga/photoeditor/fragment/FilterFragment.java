package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.FilterViewHolder;
import com.example.olga.photoeditor.models.Effects.Filter;
import com.example.olga.photoeditor.mvp.presenter.FiltersPresenter;
import com.example.olga.photoeditor.mvp.presenter.PropertyListPresenter;
import com.example.olga.photoeditor.mvp.view.FiltersView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterFragment extends MvpFragment implements FiltersView {

    @BindView(R.id.fragment_filter_button_documentary)
    Button mDocumentaryButton;

    @BindView(R.id.fragment_filter_button_grayscale)
    Button mGrayscaleButton;

    @BindView(R.id.fragment_filter_button_lomoish)
    Button mLomoishButton;

    @BindView(R.id.fragment_filter_button_negative)
    Button mNegativeButton;

    @BindView(R.id.fragment_filter_button_posterize)
    Button mPosterizeButton;

    @BindView(R.id.fragment_filter_button_sepia)
    Button mSepiaButton;

    @BindView(R.id.fragment_filter_recycler_view_filters)
    RecyclerView mFilterRecyclerView;

    @BindView(R.id.fragment_filter_progress_bar_load)
    ProgressBar mProgressBar;

    @BindView(R.id.fragment_filter_text_view_no_data)
    TextView mEmptyView;

    @InjectPresenter
    FiltersPresenter mPresenter;
    PropertyListPresenter mPropertyListPresenter;

    CollectionRecycleAdapter<Filter> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);

        mFilterRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);

        mAdapter = new CollectionRecycleAdapter<Filter>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new FilterViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_filter, parent, false));
            }
        };

        mFilterRecyclerView.setAdapter(mAdapter);
        mFilterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPresenter.userLoadFilters(getActivity());

        initSwipe();

        mDocumentaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPropertyListPresenter.userChangeValue("DOCUMENTARY", 0, 0);
                mPropertyListPresenter.applyEffect();
            }
        });

        mGrayscaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPropertyListPresenter.userChangeValue("GRAYSCALE", 0, 0);
                mPropertyListPresenter.applyEffect();
            }
        });

        mLomoishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPropertyListPresenter.userChangeValue("LOMOISH", 0, 0);
                mPropertyListPresenter.applyEffect();
            }
        });

        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPropertyListPresenter.userChangeValue("NEGATIVE", 0, 0);
                mPropertyListPresenter.applyEffect();
            }
        });

        mPosterizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPropertyListPresenter.userChangeValue("POSTERIZE", 0, 0);
                mPropertyListPresenter.applyEffect();
            }
        });

        mSepiaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPropertyListPresenter.userChangeValue("SEPIA", 0, 0);
                mPropertyListPresenter.applyEffect();
            }
        });

        return view;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFiltersList() {
        mFilterRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFiltersList() {
        mFilterRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<Filter> filters) {
        mAdapter.setCollection(filters);
    }

    @Override
    public void showEmpty() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        mEmptyView.setVisibility(View.GONE);
    }

    private void initSwipe() {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        Filter filter = mAdapter.getItem(viewHolder.getAdapterPosition());
                        mAdapter.removeItem(viewHolder.getAdapterPosition());
                        mPresenter.userRemoveFilter(filter);
                    }
                });
        swipeToDismissTouchHelper.attachToRecyclerView(mFilterRecyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
