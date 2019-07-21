package com.charlyge.android.trackmymovement.NetworkModels;

import java.util.List;

public class Rows {
    private List<Elements> elements;

    public Rows(List<Elements> elements) {
        this.elements = elements;
    }

    public List<Elements> getElements() {
        return elements;
    }
}
