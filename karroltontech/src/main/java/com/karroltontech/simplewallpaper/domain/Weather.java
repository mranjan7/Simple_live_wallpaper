package com.karroltontech.simplewallpaper.domain;

/**
 * Created by mranjan on 27-03-2016.
 */
public class Weather {
    private Currently currently;

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }
}
