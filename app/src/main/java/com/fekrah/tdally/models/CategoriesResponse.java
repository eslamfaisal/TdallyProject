package com.fekrah.tdally.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("data")
    @Expose
    private List<Data> data;

    public CategoriesResponse() {
    }

    public boolean isStatus() {
        return status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public class Data {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("image")
        @Expose
        private String image;

        @SerializedName("subCategory")
        @Expose
        private List<SubCategory> subCategory;

        public Data() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<SubCategory> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(List<SubCategory> subCategory) {
            this.subCategory = subCategory;
        }
    }

    public class SubCategory{

        @SerializedName("ubcCategoryId")
        @Expose
        private String ubcCategoryId;

        @SerializedName("nameSubCategory")
        @Expose
        private String nameSubCategory;

        @SerializedName("photo")
        @Expose
        private String photo;

        public SubCategory() {
        }

        public String getUbcCategoryId() {
            return ubcCategoryId;
        }

        public void setUbcCategoryId(String ubcCategoryId) {
            this.ubcCategoryId = ubcCategoryId;
        }

        public String getNameSubCategory() {
            return nameSubCategory;
        }

        public void setNameSubCategory(String nameSubCategory) {
            this.nameSubCategory = nameSubCategory;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
