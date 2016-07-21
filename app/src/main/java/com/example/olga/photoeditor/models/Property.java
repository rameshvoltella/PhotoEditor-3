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
    private static final Property BRIGHTNESS = new Property("Яркость", 1.0, 2.0, 1.0, R.drawable.ic_brightness);
    private static final Property CONTRAST = new Property("Контрастность", 1.0, 2.0, 1.0, R.drawable.ic_contrast);
    private static final Property SATURATE = new Property("Насыщенность", -1.0, 1.0, 0.0, R.drawable.ic_saturate);
    private static final Property SHARPEN = new Property("Резкость", 0.0, 1.0, 0.0, R.drawable.ic_sharpen);

    //Extend properties
    private static final Property AUTOFIX = new Property("Автокоррекция", 0.0, 1.0, 0.0, R.drawable.ic_fix);
    private static final Property BLACKWHITE = new Property("Уровень черного/белого", 0.0, 1.0, 0.5, R.drawable.ic_filter_b_and_w);
    private static final Property FILLIGHT = new Property("Заполняющий свет", 0.0, 1.0, 0.0, R.drawable.ic_fillight);
    private static final Property GRAIN = new Property("Зернистость", 0.0, 1.0, 0.0, R.drawable.ic_grain);
    private static final Property TEMPERATURE = new Property("Температура", 0.0, 1.0, 0.5, R.drawable.ic_sunny);
    private static final Property FISHEYE = new Property("Объектив", 0.0, 1.0, 0.0, R.drawable.ic_camera);
    private static final Property VIGNETTE = new Property("Виньетка", 0.0, 1.0, 0.0, R.drawable.ic_vignette);

    private String mPropertyName;

    private double mMinValue;

    private double mMaxValue;

    private double mDefaultValue;

    private double mCurrentValue;

    private int mImageId;

    public static List<Property> getStandardProperties() {
        return new ArrayList<>(Arrays.asList(BRIGHTNESS, CONTRAST, SATURATE, SHARPEN));
    }

    public static List<Property> getExtendProperties() {
        return new ArrayList<>(Arrays.asList(AUTOFIX, BLACKWHITE, FILLIGHT, GRAIN, TEMPERATURE, FISHEYE, VIGNETTE));
    }

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

    public double getCurrentValue() {
        return mCurrentValue;
    }

    public void setCurrentValue(double currentValue) {
        mCurrentValue = currentValue;
    }
}
