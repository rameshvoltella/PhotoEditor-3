package com.example.olga.photoeditor.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.mvp.presenter.FiltersPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 08.07.16
 * Time: 16:45
 *
 * @author Olga
 */
public class FilterViewHolder extends CollectionRecycleAdapter.RecycleViewHolder<Filter> {

    @BindView(R.id.item_filter_check_box)
    CheckBox mCheckBox;

    int mCheckedPosition;
    int mCurrentPosition;

    public FilterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void create(View rootView) {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final Filter model) {
        mCheckBox.setText(model.getFilterName());
        mCheckBox.setChecked(false);
        mCurrentPosition = getAdapterPosition();
        mCheckBox.setChecked(mCheckedPosition == mCurrentPosition);
        mCheckBox.setOnClickListener(v -> {
            if (mCurrentPosition == mCheckedPosition) {
                mCheckBox.setChecked(false);
                FiltersPresenter.userCheckFilter(Filter.getCurrentFilter());
                mCheckedPosition = -1;
            } else {
                mCheckedPosition = mCurrentPosition;
                mCheckBox.setChecked(true);
                FiltersPresenter.userCheckFilter(model);
            }
        });

    }
}
