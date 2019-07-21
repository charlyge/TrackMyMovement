package com.charlyge.android.trackmymovement.VolleyRequest;

public class Elements {
    private Distance distance;
    private TDuration duration;
    private String status;

    public Elements(Distance distance, TDuration duration, String status) {
        this.distance = distance;
        this.duration = duration;
        this.status = status;
    }

    public Distance getDistance() {
        return distance;
    }

    public TDuration getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }
}
