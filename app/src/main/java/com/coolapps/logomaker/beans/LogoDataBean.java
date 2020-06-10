package com.coolapps.logomaker.beans;

/**
 * Created by apple on 3/3/18.
 */

public class LogoDataBean {

    private int image;
    private String name;
    private int viewType;

    public LogoDataBean(int viewType){
        this.viewType = viewType;
    }


//    public LogoDataBean(int image){
//        this.image = image;
//    }

    public LogoDataBean(int image, String name){
        this.image = image;
        this.name = name;
        this.viewType = viewType;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getViewType() {
        return viewType;
    }

}
