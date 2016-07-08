package com.example.olga.photoeditor.models;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.olga.photoeditor.R;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Date: 08.07.16
 * Time: 14:01
 *
 * @author Olga
 */
public class Filter {

    public String filterName;

    public static final PropertyData BRIGHTNESS = new PropertyData("Яркость", 1.0, 100.0, 1.0, R.drawable.ic_brightness);
    public static final PropertyData CONTRAST = new PropertyData("Контрастность", 1.0, 100.0, 1.0, R.drawable.ic_contrast);
    public static final PropertyData SATURATE = new PropertyData("Насыщенность", -1.0, 1.0, 0.0, R.drawable.ic_saturate);
    public static final PropertyData SHARPEN = new PropertyData("Резкость", 0.0, 1.0, 0.5, R.drawable.ic_sharpen);

    public static final PropertyData AUTOFIX = new PropertyData("Автокоррекция", 0.0, 1.0, 0.0, R.drawable.ic_fix);
    public static final PropertyData BLACK = new PropertyData("Уровень черного", 0.0, 1.0, 0.5, R.drawable.ic_filter_b_and_w);
    public static final PropertyData WHITE = new PropertyData("Уровень белого", 0.0, 1.0, 0.5, R.drawable.ic_filter_b_and_w);
    public static final PropertyData FILLIGHT = new PropertyData("Заполняющий свет", 0.0, 1.0, 0.0, R.drawable.ic_fillight);
    public static final PropertyData GRAIN = new PropertyData("Зернистость", 0.0, 1.0, 0.0, R.drawable.ic_grain);
    public static final PropertyData TEMPERATURE = new PropertyData("Температура", 0.0, 1.0, 0.5, R.drawable.ic_sunny);
    public static final PropertyData FISHEYE = new PropertyData("Объектив", 0.0, 1.0, 0.0, R.drawable.ic_camera);
    public static final PropertyData VIGNETTE = new PropertyData("Виньетка", 0.0, 1.0, 0.0, R.drawable.ic_vignette);

    public Filter(String name, double brightness, double contrast, double saturate, double sharpen, double autofix, double black,
                  double white, double fillight, double grain, double temperature, double fisheye, double vignette) {
        filterName = name;
        BRIGHTNESS.setDefaultValue(brightness);
        CONTRAST.setDefaultValue(contrast);
        SATURATE.setDefaultValue(saturate);
        SHARPEN.setDefaultValue(sharpen);
        AUTOFIX.setDefaultValue(autofix);
        BLACK.setDefaultValue(black);
        WHITE.setDefaultValue(white);
        FILLIGHT.setDefaultValue(fillight);
        GRAIN.setDefaultValue(grain);
        TEMPERATURE.setDefaultValue(temperature);
        FISHEYE.setDefaultValue(fisheye);
        VIGNETTE.setDefaultValue(vignette);
    }

    public static List<PropertyData> getStandardProperties() {
        List<PropertyData> products = new ArrayList<>(Arrays.asList(BRIGHTNESS, CONTRAST, SATURATE, SHARPEN));
        Collections.shuffle(products);
        return products;
    }

    public static List<PropertyData> getExtendProperties() {
        List<PropertyData> products = new ArrayList<>(Arrays.asList(AUTOFIX, BLACK, WHITE, FILLIGHT, GRAIN, TEMPERATURE, FISHEYE, VIGNETTE));
        Collections.shuffle(products);
        return products;
    }

    public static double getBRIGHTNESS() {
        return BRIGHTNESS.getDefaultValue();
    }

    public static void setBRIGHTNESS(double defaultValue) {
        BRIGHTNESS.setDefaultValue(defaultValue);
    }

    public static double getCONTRAST() {
        return CONTRAST.getDefaultValue();
    }

    public static void setCONTRAST(double defaultValue) {
        CONTRAST.setDefaultValue(defaultValue);
    }

    public static double getSATURATE() {
        return SATURATE.getDefaultValue();
    }

    public static void setSATURATE(double defaultValue) {
        SATURATE.setDefaultValue(defaultValue);
    }

    public static double getSHARPEN() {
        return SHARPEN.getDefaultValue();
    }

    public static void setSHARPEN(double defaultValue) {
        SHARPEN.setDefaultValue(defaultValue);
    }

    public static double getAUTOFIX() {
        return AUTOFIX.getDefaultValue();
    }

    public static void setAUTOFIX(double defaultValue) {
        AUTOFIX.setDefaultValue(defaultValue);
    }

    public static double getBLACK() {
        return BLACK.getDefaultValue();
    }

    public static void setBLACK(double defaultValue) {
        BLACK.setDefaultValue(defaultValue);
    }

    public static double getWHITE() {
        return WHITE.getDefaultValue();
    }

    public static void setWHITE(double defaultValue) {
        WHITE.setDefaultValue(defaultValue);
    }

    public static double getFILLIGHT() {
        return FILLIGHT.getDefaultValue();
    }

    public static void setFILLIGHT(double defaultValue) {
        FILLIGHT.setDefaultValue(defaultValue);
    }

    public static double getGRAIN() {
        return GRAIN.getDefaultValue();
    }

    public static void setGRAIN(double defaultValue) {
        GRAIN.setDefaultValue(defaultValue);
    }

    public static double getTEMPERATURE() {
        return TEMPERATURE.getDefaultValue();
    }

    public static void setTEMPERATURE(double defaultValue) {
        TEMPERATURE.setDefaultValue(defaultValue);
    }

    public static double getFISHEYE() {
        return FISHEYE.getDefaultValue();
    }

    public static void setFISHEYE(double defaultValue) {
        FISHEYE.setDefaultValue(defaultValue);
    }

    public static double getVIGNETTE() {
        return VIGNETTE.getDefaultValue();
    }

    public static void setVIGNETTE(double defaultValue) {
        VIGNETTE.setDefaultValue(defaultValue);
    }

    public Filter defaultFilter = new Filter("default", 1.0, 1.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    public Filter waldenFilter = new Filter("walden", 1.2, 0.9, 1.7, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    public Filter toasterFilter = new Filter("toaster", 1.0, 0.67, 2.5, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    public Filter kelvinFilter = new Filter("kelvin", 1.3, 1.1, 2.4, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    public Filter willowtFilter = new Filter("willow", 1.2, 0.85, 0.02, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);


    public static void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) throws SQLException, java.sql.SQLException {
        TableUtils.createTable(connectionSource, Friend.class);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //
    }

}
