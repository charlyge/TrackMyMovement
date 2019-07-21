package com.charlyge.android.trackmymovement.VolleyRequest;

import java.util.List;

public class ApiResponseClass {
    private List<Rows> rows;

    public ApiResponseClass(List<Rows> rows) {
        this.rows = rows;
    }

    public List<Rows> getRows() {
        return rows;
    }
}
