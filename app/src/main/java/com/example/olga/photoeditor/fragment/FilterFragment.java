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
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterFragment extends MvpFragment implements FiltersView {

    private static final String CURRENT_FILTER = "CURRENT_FILTER";

    @BindView(R.id.fragment_filter_button_save)
    Button mSaveButton;

    @BindView(R.id.fragment_filter_edit_text_name)
    EditText mNameText;

    @BindView(R.id.fragment_filter_recycler_view_filters)
    RecyclerView mFilterRecyclerView;

    @BindView(R.id.fragment_filter_progress_bar_load)
    ProgressBar mProgressBar;

    @BindView(R.id.fragment_filter_text_view_no_data)
    TextView mEmptyView;

    @InjectPresenter
    FiltersPresenter mPresenter;

    private Filter mCurrentFilter;

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
        mNameText.setVisibility(View.GONE);

        //Create and set adapter
        mAdapter = new CollectionRecycleAdapter<Filter>(getActivity()) {
            @Override
            public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new FilterViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_filter, parent, false));
            }
        };
        mFilterRecyclerView.setAdapter(mAdapter);
        mFilterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFilterRecyclerView.setOnClickListener(v -> mAdapter.notifyDataSetChanged());
        mPresenter.userLoadFilters(getActivity());

        //Apply currentFilter
        if (savedInstanceState != null) {
            mCurrentFilter = (Filter) savedInstanceState.getSerializable(CURRENT_FILTER);
            FiltersPresenter.userCheckFilter(mCurrentFilter);
        } else {
            mCurrentFilter = Filter.getCurrentFilter();
        }

        initSwipe();

        // Create listeners
        mSaveButton.setOnClickListener(view1 -> {
            if (!mNameText.getText().toString().equals("")) {
                changeCurrentFilter();
                mCurrentFilter.setFilterName(mNameText.getText().toString());
                mPresenter.userCreateFilter(mCurrentFilter);
                mNameText.setVisibility(View.GONE);
            } else {
                mNameText.setVisibility(View.VISIBLE);
                mNameText.requestFocus();
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

    private void changeCurrentFilter() {
        List<Double> values = new ArrayList<>();
        values.addAll(StandardPropertyFragment.getValues());
        values.addAll(ExtendPropertyFragment.getValues());
        mCurrentFilter.setBrightnessValue(values.get(0));
        mCurrentFilter.setContrastValue(values.get(1));
        mCurrentFilter.setSaturateValue(values.get(2));
        mCurrentFilter.setSharpenValue(values.get(3));
        if (values.size() == 12) {
            mCurrentFilter.setAutofixValue(values.get(4));
            mCurrentFilter.setBlackValue(values.get(5));
            mCurrentFilter.setWhiteValue(values.get(6));
            mCurrentFilter.setFillightValue(values.get(7));
            mCurrentFilter.setGrainValue(values.get(8));
            mCurrentFilter.setTemperatureValue(values.get(9));
            mCurrentFilter.setFisheyeValue(values.get(10));
            mCurrentFilter.setVignetteValue(values.get(11));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeCurrentFilter();
        mPresenter.userChangeCurrentFilter(mCurrentFilter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_FILTER, mCurrentFilter);
    }


}
