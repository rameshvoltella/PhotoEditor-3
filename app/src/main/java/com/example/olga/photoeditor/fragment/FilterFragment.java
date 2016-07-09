package com.example.olga.photoeditor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.adapter.CollectionRecycleAdapter;
import com.example.olga.photoeditor.adapter.FilterViewHolder;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.mvp.presenter.FiltersPresenter;
import com.example.olga.photoeditor.mvp.view.FiltersView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterFragment extends MvpFragment implements FiltersView {

    @BindView(R.id.fragment_filter_recycler_view_filters)
    RecyclerView mFilterRecyclerView;

    @BindView(R.id.fragment_filter_progress_bar_load)
    ProgressBar mProgressBar;

    @BindView(R.id.fragment_filter_text_view_no_data)
    TextView mEmptyView;

    @InjectPresenter
    FiltersPresenter mPresenter;

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
