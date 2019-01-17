package com.fekrah.tdally.models;

public class Ad {
    private String addImage;
    private String addName;
    private String views;
    private String shares;
    private String distanse;
    private String price;
    private String date;

    public Ad() {
    }

    public Ad(String addImage, String addName, String views, String shares, String distanse, String price, String date) {
        this.addImage = addImage;
        this.addName = addName;
        this.views = views;
        this.shares = shares;
        this.distanse = distanse;
        this.price = price;
        this.date = date;
    }

    public String getAddImage() {
        return addImage;
    }

    public void setAddImage(String addImage) {
        this.addImage = addImage;
    }

    public String getAddName() {
        return addName;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getDistanse() {
        return distanse;
    }

    public void setDistanse(String distanse) {
        this.distanse = distanse;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
