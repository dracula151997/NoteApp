package com.project.semicolon.noteapp.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("api_key")
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return
                "UserResponse{" +
                        "api_key = '" + apiKey + '\'' +
                        "}";
    }
}