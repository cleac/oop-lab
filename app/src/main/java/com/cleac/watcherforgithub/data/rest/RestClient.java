package com.cleac.watcherforgithub.data.rest;

import com.cleac.watcherforgithub.data.rest.service.GithubApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by cleac on 03.04.15.
 */
public class RestClient {

    private static RestClient mInstance;
    public static RestClient instance() {
        if(mInstance == null) {
            mInstance = new RestClient();
        }
        return mInstance;
    }

    public static final String base_url = "https://api.github.com";
    private final GithubApiService apiService;

    private RestClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(GithubApiService.class);
    }

    public GithubApiService ApiService() {
        return apiService;
    }
}
