package com.karroltontech.simplewallpaper.domain;

import java.util.List;



/**
 * Created by mranjan on 17-04-2016.
 */
public class Product {
    //@XmlElement(name="time",type = Time.class)
    //@XmlElementWrapper(name="times")
    private List<Time> times;

    public List<Time> getTimes() {

        return times;
    }

    public void setTimes(List<Time> time) {
        this.times = times;
    }
}
