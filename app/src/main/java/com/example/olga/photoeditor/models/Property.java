package com.example.olga.photoeditor.models;

import com.example.olga.photoeditor.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 30.06.16
 * Time: 18:46
 *
 * @author Olga
 */
@SuppressWarnings("unused")
public class Property implements Serializable {

    //Standard properties
    private static final Property BRIGHTNESS = new Property("Яркость", 1.0f, 2.0f, 1.0f, 1.0f, R.drawable.ic_brightness);
    private static final Property CONTRAST = new Property("Контрастность", 1.0f, 2.0f, 1.0f, 1.0f, R.drawable.ic_contrast);
    private static final Property SATURATE = new Property("Насыщенность", -1.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_saturate);
    private static final Property SHARPEN = new Property("Резкость", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_sharpen);

    //Extend properties
    private static final Property AUTOFIX = new Property("Автокоррекция", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_fix);
    private static final Property BLACKWHITE = new Property("Уровень черного/белого", 0.0f, 1.0f, 0.5f, 0.5f, R.drawable.ic_filter_b_and_w);
    private static final Property FILLIGHT = new Property("Заполняющий свет", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_fillight);
    private static final Property GRAIN = new Property("Зернистость", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_grain);
    private static final Property TEMPERATURE = new Property("Температура", 0.0f, 1.0f, 0.5f, 0.5f, R.drawable.ic_sunny);
    private static final Property FISHEYE = new Property("Объектив", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_camera);
    private static final Property VIGNETTE = new Property("Виньетка", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_vignette);

    private String mPropertyName;

    private float mMinValue;

    private float mMaxValue;

    private float mDefaultValue;

    private float mCurrentValue;

    private int mImageId;

    public static List<Property> getStandardProperties() {
        return new ArrayList<>(Arrays.asList(BRIGHTNESS, CONTRAST, SATURATE, SHARPEN));
    }

    public static List<Property> getExtendProperties() {
        return new ArrayList<>(Arrays.asList(AUTOFIX, BLACKWHITE, FILLIGHT, GRAIN, TEMPERATURE, FISHEYE, VIGNETTE));
    }

    public Property(String propertyName, float minValue, float maxValue, float defaultValue, float currentValue, int imageUrl) {
        mPropertyName = propertyName;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mDefaultValue = defaultValue;
        mCurrentValue = currentValue;
        mImageId = imageUrl;
    }

    public String getPropertyName() {
        return mPropertyName;
    }

    public void setPropertyName(String propertyName) {
        mPropertyName = propertyName;
    }

    public double getMinValue() {
        return mMinValue;
    }

    public void setMinValue(float minValue) {
        mMinValue = minValue;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    public float getDefaultValue() {
        return mDefaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        mDefaultValue = defaultValue;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    public float getCurrentValue() {
        return mCurrentValue;
    }

    public void setCurrentValue(float currentValue) {
        mCurrentValue = currentValue;
    }

    public int getValue() {
        return (int) ((mCurrentValue - mMinValue) * 100/ (mMaxValue - mMinValue));
    }

    public void setValue(int currentValue) {
        mCurrentValue = (currentValue * (mMaxValue - mMinValue) / 100 + mMinValue);
    }
}
