package com.example.olga.photoeditor.models;

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

    FLIPVERT("Отражение по вертикали", "FLIPVERT"),
    FLIPHOR("Отражение по горизогтали", "FLIPHOR"),
    NONE("Без фильтра", "NONE"),
    CROSSPROCESS("Пленка", "CROSSPROCESS"),
    DOCUMENTARY("Документальный", "DOCUMENTARY"),
    GRAYSCALE("Оттенки серого", "GRAYSCALE"),
    LOMOISH("Ломография", "LOMOISH"),
    NEGATIVE("Негатив", "NEGATIVE"),
    POSTERIZE("Постеризация", "POSTERIZE"),
    SEPIA("Сепия", "SEPIA");

    public static List<Filter> getFilterList() {
        return new ArrayList<>(Arrays.asList(NONE, CROSSPROCESS, DOCUMENTARY, GRAYSCALE, LOMOISH, NEGATIVE, POSTERIZE, SEPIA));
    }

    private String mFilterName;
    private String mFilterLabel;

    Filter(String filterName, String filterLabel) {
        mFilterName = filterName;
        mFilterLabel = filterLabel;
    }

    public String getFilterName() {
        return mFilterName;
    }

    public void setFilterName(String filterName) {
        mFilterName = filterName;
    }

    public String getFilterLabel() {
        return mFilterLabel;
    }

    public void setFilterLabel(String filterLabel) {
        mFilterLabel = filterLabel;
    }
}
