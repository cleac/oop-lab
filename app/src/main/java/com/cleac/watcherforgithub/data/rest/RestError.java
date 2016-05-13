package com.cleac.watcherforgithub.data.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cleac on 4/15/15.
 */
public class RestError {
    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String errorDetails;
}