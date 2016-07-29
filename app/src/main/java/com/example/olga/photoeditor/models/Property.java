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
public enum Property implements Serializable {

    //Standard properties
    BRIGHTNESS("BRIGHTNESS", "Яркость", 1.0f, 2.0f, 1.0f, 1.0f, R.drawable.ic_brightness),
    CONTRAST("CONTRAST", "Контрастность", 1.0f, 2.0f, 1.0f, 1.0f, R.drawable.ic_contrast),
    SATURATE("SATURATE", "Насыщенность", -1.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_saturate),
    SHARPEN("SHARPEN", "Резкость", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_sharpen),

    //Extend properties
    AUTOFIX("AUTOFIX", "Автокоррекция", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_fix),
    BLACKWHITE("BLACKWHITE", "Уровень черного/белого", 0.0f, 1.0f, 0.5f, 0.5f, R.drawable.ic_filter_b_and_w),
    FILLIGHT("FILLIGHT", "Заполняющий свет", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_fillight),
    GRAIN("GRAIN", "Зернистость", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_grain),
    TEMPERATURE("TEMPERATURE", "Температура", 0.0f, 1.0f, 0.5f, 0.5f, R.drawable.ic_sunny),
    FISHEYE("FISHEYE", "Объектив", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_camera),
    VIGNETTE("VIGNETTE", "Виньетка", 0.0f, 1.0f, 0.0f, 0.0f, R.drawable.ic_vignette);

    public static List<Property> getStandardProperties() {
        return new ArrayList<>(Arrays.asList(BRIGHTNESS, CONTRAST, SATURATE, SHARPEN));
    }

    public static List<Property> getExtendProperties() {
        return new ArrayList<>(Arrays.asList(AUTOFIX, BLACKWHITE, FILLIGHT, GRAIN, TEMPERATURE, FISHEYE, VIGNETTE));
    }

    private String mPropertyName;

    private String mPropertyDescription;

    private float mMinValue;

    private float mMaxValue;

    private float mDefaultValue;

    private float mCurrentValue;

    private int mImageId;

    Property(String propertyName, String propertyDescription, float minValue, float maxValue, float defaultValue, float currentValue, int imageUrl) {
        mPropertyName = propertyName;
        mPropertyDescription = propertyDescription;
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

    public String getPropertyDescription() {
        return mPropertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        mPropertyDescription = propertyDescription;
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
