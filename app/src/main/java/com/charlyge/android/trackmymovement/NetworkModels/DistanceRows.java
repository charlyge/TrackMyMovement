package com.charlyge.android.trackmymovement.NetworkModels;

import java.util.List;

public class DistanceRows {
   private List<Rows> rows;

    public DistanceRows(List<Rows> rows) {
        this.rows = rows;
    }

    public List<Rows> getRows() {
        return rows;
    }
}
