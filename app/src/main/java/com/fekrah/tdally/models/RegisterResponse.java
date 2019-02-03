package com.fekrah.tdally.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("data")
    @Expose
    private Data data;

    public RegisterResponse() {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("userToken")
        @Expose
        private String userToken;

        public Data() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
