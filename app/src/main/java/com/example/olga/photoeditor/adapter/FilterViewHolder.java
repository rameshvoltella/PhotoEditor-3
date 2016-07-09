package com.example.olga.photoeditor.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.Effects.Filter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 08.07.16
 * Time: 16:45
 *
 * @author Olga
 */
public class FilterViewHolder extends CollectionRecycleAdapter.RecycleViewHolder<Filter> {

    @BindView(R.id.item_filter_text_view_filter_name)
    TextView mFilterName;

    @BindView(R.id.item_filter_checkbox)
    CheckBox mFilterCheckBox;

    public FilterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void create(View rootView) {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final Filter model) {

        mFilterName.setText(model.getFilterName());
        mFilterCheckBox.setChecked(false);


    }
}
