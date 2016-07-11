package com.example.olga.photoeditor.models.Effects;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.olga.photoeditor.R;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 08.07.16
 * Time: 14:01
 *
 * @author Olga
 */

@DatabaseTable(tableName = Filter.TABLE)
public class Filter implements Serializable {
    public static final String TABLE = "Filter";

    //Standard properties
    public static final Property BRIGHTNESS = new Property("Яркость", 1.0, 5.0, 1.0, R.drawable.ic_brightness);
    public static final Property CONTRAST = new Property("Контрастность", 1.0, 5.0, 1.0, R.drawable.ic_contrast);
    public static final Property SATURATE = new Property("Насыщенность", -1.0, 1.0, 0.0, R.drawable.ic_saturate);
    public static final Property SHARPEN = new Property("Резкость", 0.0, 1.0, 0.5, R.drawable.ic_sharpen);

    //Extend properties
    public static final Property AUTOFIX = new Property("Автокоррекция", 0.0, 1.0, 0.0, R.drawable.ic_fix);
    public static final Property BLACK = new Property("Уровень черного", 0.0, 1.0, 0.5, R.drawable.ic_filter_b_and_w);
    public static final Property WHITE = new Property("Уровень белого", 0.0, 1.0, 0.5, R.drawable.ic_filter_b_and_w);
    public static final Property FILLIGHT = new Property("Заполняющий свет", 0.0, 1.0, 0.0, R.drawable.ic_fillight);
    public static final Property GRAIN = new Property("Зернистость", 0.0, 1.0, 0.0, R.drawable.ic_grain);
    public static final Property TEMPERATURE = new Property("Температура", 0.0, 1.0, 0.5, R.drawable.ic_sunny);
    public static final Property FISHEYE = new Property("Объектив", 0.0, 1.0, 0.0, R.drawable.ic_camera);
    public static final Property VIGNETTE = new Property("Виньетка", 0.0, 1.0, 0.0, R.drawable.ic_vignette);

    //Table constructor
    public static class Column {
        public static final String ID = "id";
        public static final String FILTER_NAME = "filter_name";
        public static final String BRIGHTNESS = "Яркость";
        public static final String CONTRAST = "Контрастность";
        public static final String SATURATE = "Насыщенность";
        public static final String SHARPEN = "Резкость";

        public static final String AUTOFIX = "Автокоррекция";
        public static final String BLACK = "Уровень черного";
        public static final String WHITE = "Уровень белого";
        public static final String FILLIGHT = "Заполняющий свет";
        public static final String GRAIN = "Зернистость";
        public static final String TEMPERATURE = "Температура";
        public static final String FISHEYE = "Объектив";
        public static final String VIGNETTE = "Виньетка";

    }

    @DatabaseField(columnName = Column.ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = Column.FILTER_NAME)
    private String filterName;

    @DatabaseField(columnName = Column.BRIGHTNESS)
    private double brightnessValue;

    @DatabaseField(columnName = Column.CONTRAST)
    private double contrastValue;

    @DatabaseField(columnName = Column.SATURATE)
    private double saturateValue;

    @DatabaseField(columnName = Column.SHARPEN)
    private double sharpenValue;

    @DatabaseField(columnName = Column.AUTOFIX)
    private double autofixValue;

    @DatabaseField(columnName = Column.BLACK)
    private double blackValue;

    @DatabaseField(columnName = Column.WHITE)
    private double whiteValue;

    @DatabaseField(columnName = Column.FILLIGHT)
    private double fillightValue;

    @DatabaseField(columnName = Column.GRAIN)
    private double grainValue;

    @DatabaseField(columnName = Column.TEMPERATURE)
    private double temperatureValue;

    @DatabaseField(columnName = Column.FISHEYE)
    private double fisheyeValue;

    @DatabaseField(columnName = Column.VIGNETTE)
    private double vignetteValue;


    public Filter() {/**/}

    public Filter(String filterName, double brightnessValue, double contrastValue, double saturateValue,
                  double sharpenValue, double autofixValue, double blackValue, double whiteValue,
                  double fillightValue, double grainValue, double temperatureValue, double fisheyeValue,
                  double vignetteValue) {
        this.filterName = filterName;
        this.brightnessValue = brightnessValue;
        this.contrastValue = contrastValue;
        this.saturateValue = saturateValue;
        this.sharpenValue = sharpenValue;
        this.autofixValue = autofixValue;
        this.blackValue = blackValue;
        this.whiteValue = whiteValue;
        this.fillightValue = fillightValue;
        this.grainValue = grainValue;
        this.temperatureValue = temperatureValue;
        this.fisheyeValue = fisheyeValue;
        this.vignetteValue = vignetteValue;
    }

    private static Filter defaultFilter = new Filter("default", 1.0, 1.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    private static Filter waldenFilter = new Filter("walden", 1.2, 0.9, 1.7, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    private static Filter toasterFilter = new Filter("toaster", 1.0, 0.67, 2.5, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    private static Filter kelvinFilter = new Filter("kelvin", 1.3, 1.1, 2.4, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);
    private static Filter willowtFilter = new Filter("willow", 1.2, 0.85, 0.02, 0.5, 0.0, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0);


    public static List<Property> getStandardProperties() {
        List<Property> properties = new ArrayList<>(Arrays.asList(BRIGHTNESS, CONTRAST, SATURATE, SHARPEN));
        return properties;
    }

    public static List<Property> getExtendProperties() {
        List<Property> properties = new ArrayList<>(Arrays.asList(AUTOFIX, BLACK, WHITE, FILLIGHT, GRAIN, TEMPERATURE, FISHEYE, VIGNETTE));
        return properties;
    }

    public static List<Filter> getStandartFilters() {
        List<Filter> list = new ArrayList<>(Arrays.asList(defaultFilter, waldenFilter, toasterFilter, kelvinFilter, willowtFilter));
        return list;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBrightnessValue() {
        return brightnessValue;
    }

    public void setBrightnessValue(double brightnessValue) {
        this.brightnessValue = brightnessValue;
    }

    public double getContrastValue() {
        return contrastValue;
    }

    public void setContrastValue(double contrastValue) {
        this.contrastValue = contrastValue;
    }

    public double getSaturateValue() {
        return saturateValue;
    }

    public void setSaturateValue(double saturateValue) {
        this.saturateValue = saturateValue;
    }

    public double getSharpenValue() {
        return sharpenValue;
    }

    public void setSharpenValue(double sharpenValue) {
        this.sharpenValue = sharpenValue;
    }

    public double getAutofixValue() {
        return autofixValue;
    }

    public void setAutofixValue(double autofixValue) {
        this.autofixValue = autofixValue;
    }

    public double getBlackValue() {
        return blackValue;
    }

    public void setBlackValue(double blackValue) {
        this.blackValue = blackValue;
    }

    public double getWhiteValue() {
        return whiteValue;
    }

    public void setWhiteValue(double whiteValue) {
        this.whiteValue = whiteValue;
    }

    public double getFillightValue() {
        return fillightValue;
    }

    public void setFillightValue(double fillightValue) {
        this.fillightValue = fillightValue;
    }

    public double getGrainValue() {
        return grainValue;
    }

    public void setGrainValue(double grainValue) {
        this.grainValue = grainValue;
    }

    public double getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(double temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public double getFisheyeValue() {
        return fisheyeValue;
    }

    public void setFisheyeValue(double fisheyeValue) {
        this.fisheyeValue = fisheyeValue;
    }

    public double getVignetteValue() {
        return vignetteValue;
    }

    public void setVignetteValue(double vignetteValue) {
        this.vignetteValue = vignetteValue;
    }

    public static Filter getDefaultFilter() {
        return defaultFilter;
    }

    public static Filter getWaldenFilter() {
        return waldenFilter;
    }

    public static Filter getToasterFilter() {
        return toasterFilter;
    }

    public static Filter getKelvinFilter() {
        return kelvinFilter;
    }

    public static Filter getWillowtFilter() {
        return willowtFilter;
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) throws SQLException, java.sql.SQLException {
        TableUtils.createTable(connectionSource, Filter.class);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //
    }

}
