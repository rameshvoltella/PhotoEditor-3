package com.example.olga.photoeditor.models;

import com.example.olga.photoeditor.R;

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

    public static PropertyData getBRIGHTNESS() {
        return BRIGHTNESS;
    }

    public static PropertyData getCONTRAST() {
        return CONTRAST;
    }

    public static PropertyData getSATURATE() {
        return SATURATE;
    }

    public static PropertyData getSHARPEN() {
        return SHARPEN;
    }

    public static PropertyData getAUTOFIX() {
        return AUTOFIX;
    }

    public static PropertyData getBLACK() {
        return BLACK;
    }

    public static PropertyData getWHITE() {
        return WHITE;
    }

    public static PropertyData getFILLIGHT() {
        return FILLIGHT;
    }

    public static PropertyData getGRAIN() {
        return GRAIN;
    }

    public static PropertyData getTEMPERATURE() {
        return TEMPERATURE;
    }

    public static PropertyData getFISHEYE() {
        return FISHEYE;
    }

    public static PropertyData getVIGNETTE() {
        return VIGNETTE;
    }


}
