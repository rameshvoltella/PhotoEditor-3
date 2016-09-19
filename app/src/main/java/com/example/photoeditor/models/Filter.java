package com.example.photoeditor.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 08.07.16
 * Time: 14:01
 *
 * @author Olga
 */


@SuppressWarnings("unused")
public enum Filter {
    NONE("Без фильтра"),
    CROSSPROCESS("Пленка"),
    DOCUMENTARY("Документальный"),
    GRAYSCALE("Оттенки серого"),
    LOMOISH("Ломография"),
    NEGATIVE("Негатив"),
    POSTERIZE("Постеризация"),
    SEPIA("Сепия"),
    FLIPVERT("Отражение по вертикали"),
    FLIPHOR("Отражение по горизонтали");

    public static List<Filter> getFilterList() {
        return new ArrayList<>(Arrays.asList(NONE, CROSSPROCESS, DOCUMENTARY, GRAYSCALE, LOMOISH, NEGATIVE, POSTERIZE, SEPIA));
    }

    private String mFilterName;

    Filter(String filterName) {
        mFilterName = filterName;
    }

    public String getFilterName() {
        return mFilterName;
    }

    public void setFilterName(String filterName) {
        mFilterName = filterName;
    }
}
