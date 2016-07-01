package com.example.olga.photoeditor.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Date: 30.06.16
 * Time: 18:46
 *
 * @author Olga
 */
public class PropertyData implements Serializable {

    public  static final PropertyData BRIGHTNESS = new PropertyData("Яркость", 1.0, 100.0, 1.0);
    public  static final PropertyData CONTRAST = new PropertyData("Контрастность", 1.0, 100.0, 1.0);
    public  static final PropertyData SATURATE = new PropertyData("Насыщенность", -1.0, 1.0, 0.0);
    public  static final PropertyData SHARPEN = new PropertyData("Резкость", 0.0, 1.0, 0.5);

    public  static final PropertyData AUTOFIX = new PropertyData("Автокоррекция", 0.0, 1.0, 0.0);
    public  static final PropertyData BLACK = new PropertyData("Уровень черного", 0.0, 1.0, 0.5);
    public  static final PropertyData WHITE = new PropertyData("Уровень белого", 0.0, 1.0, 0.5);
    public  static final PropertyData FILLIGHT = new PropertyData("Заполняющий свет", 0.0, 1.0, 0.0);
    public  static final PropertyData GRAIN = new PropertyData("Зернистость", 0.0, 1.0, 0.0);
    public  static final PropertyData TEMPERATURE = new PropertyData("Температура", 0.0, 1.0, 0.5);
    public  static final PropertyData FISHEYE = new PropertyData("Объектив", 0.0, 1.0, 0.0);
    public  static final PropertyData VIGNETTE = new PropertyData("Виньетка", 0.0, 1.0, 0.0);

    public static List<PropertyData> getStandartProperties()
    {
        List<PropertyData> products = new ArrayList<>(Arrays.asList(BRIGHTNESS, CONTRAST, SATURATE, SHARPEN));
        Collections.shuffle(products);
        return products;
    }

    public static List<PropertyData> getExtendProperties()
    {
        List<PropertyData> products = new ArrayList<>(Arrays.asList(AUTOFIX, BLACK, WHITE, FILLIGHT, GRAIN, TEMPERATURE, FISHEYE, VIGNETTE));
        Collections.shuffle(products);
        return products;
    }

    private String mPropertyName;

    private double mMinValue;

    private double mMaxValue;

    private double mDefaultValue;

    public PropertyData(String propertyName, double minValue, double maxValue, double defaultValue) {
        mPropertyName = propertyName;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mDefaultValue = defaultValue;
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
}
