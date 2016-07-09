package com.example.olga.photoeditor.models;

import java.io.Serializable;

/**
 * Date: 30.06.16
 * Time: 18:46
 *
 * @author Olga
 */
@SuppressWarnings("unused")
public class Property implements Serializable {

    private String mPropertyName;

    private double mMinValue;

    private double mMaxValue;

    private double mDefaultValue;

    private int mImageId;

    public Property(String propertyName, double minValue, double maxValue, double defaultValue, int imageUrl) {
        mPropertyName = propertyName;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mDefaultValue = defaultValue;
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

    public void setMinValue(double minValue) {
        mMinValue = minValue;
    }

    public double getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(double maxValue) {
        mMaxValue = maxValue;
    }

    public double getDefaultValue() {
        return mDefaultValue;
    }

    public void setDefaultValue(double defaultValue) {
        mDefaultValue = defaultValue;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }
}
