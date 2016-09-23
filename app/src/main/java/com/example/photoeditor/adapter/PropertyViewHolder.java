package com.example.photoeditor.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.photoeditor.R;
import com.example.photoeditor.models.Property;
import com.example.photoeditor.mvp.presenter.PropertiesPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 02.07.16
 * Time: 00:33
 *
 * @author Olga
 */
public class PropertyViewHolder extends CollectionRecycleAdapter.RecycleViewHolder<Property> {

    @BindView(R.id.item_property_textview_property)
    TextView mTextViewProperty;

    @BindView(R.id.item_property_textview_percent)
    TextView mTextViewPercent;

    @BindView(R.id.photo_item_property_seek_bar)
    SeekBar mSeekBarPercent;

    @BindView(R.id.item_property_image_view_property)
    ImageView mImageView;

    private PropertiesPresenter mPresenter;

    public PropertyViewHolder(View itemView, PropertiesPresenter presenter) {
        super(itemView);
        mPresenter = presenter;
    }

    @Override
    protected void create(View rootView) {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final Property model) {
        mTextViewProperty.setText(model.getPropertyName());
        mTextViewPercent.setText(model.getValue() + " %");
        mSeekBarPercent.setProgress(model.getValue());
        mImageView.setImageResource(model.getImageId());

        mSeekBarPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextViewPercent.setText(progress + "%");
                model.setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPresenter.userChangePropertiesValue(model);
            }
        });
    }
}
