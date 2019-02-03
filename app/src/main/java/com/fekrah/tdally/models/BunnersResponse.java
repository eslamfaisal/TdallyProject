package com.fekrah.tdally.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BunnersResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("data")
    @Expose
    private List<Data> data;

    public BunnersResponse() {
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("image")
        @Expose
        private String image;

        public Data() {
        }

        public String getMessage() {
            return image;
        }

        public void setMessage(String message) {
            this.image = message;
        }
    }
}
