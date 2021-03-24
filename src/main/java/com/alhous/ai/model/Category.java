package com.alhous.ai.model;

import java.util.List;

import org.opencv.core.Mat;

public class Category {
    private String name;
    private List<Mat> images;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Mat> getImages() {
        return images;
    }
    public void setImages(List<Mat> images) {
        this.images = images;
    }    
    
}
