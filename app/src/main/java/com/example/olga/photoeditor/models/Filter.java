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
public class Filter {

    private String mFilterName;
    private String mFilterLabel;

    public Filter(String filterName, String filterLabel) {
        mFilterName = filterName;
        mFilterLabel = filterLabel;
    }

    private static final Filter CURRENT = new  Filter("Без фильтра", "NONE");
    private static final Filter CROSSPROCESS = new  Filter("Пленка", "CROSSPROCESS");
    private static final Filter DOCUMENTARY = new  Filter("Документальный", "DOCUMENTARY");
    private static final Filter GRAYSCALE = new  Filter("Оттенки серого", "GRAYSCALE");
    private static final Filter LOMOISH = new  Filter("Ломография", "LOMOISH");
    private static final Filter NEGATIVE = new  Filter("Негатив", "NEGATIVE");
    private static final Filter POSTERIZE = new  Filter("Постеризыция", "POSTERIZE");
    private static final Filter SEPIA = new  Filter("Сепия", "SEPIA");

    public static List<Filter> getFilterList () {
        return new ArrayList<>(Arrays.asList(CURRENT, CROSSPROCESS, DOCUMENTARY, GRAYSCALE, LOMOISH, NEGATIVE, POSTERIZE, SEPIA));
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
