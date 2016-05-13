package com.cleac.watcherforgithub.data.rest.service;

import com.cleac.watcherforgithub.data.rest.model.UserData;
import com.cleac.watcherforgithub.data.rest.model.branch.Branch;
import com.cleac.watcherforgithub.data.rest.model.committs.CommitsDatum;
import com.cleac.watcherforgithub.data.rest.model.repo.RepoInfo;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by cleac on 03.04.15.
 */
public interface GithubApiService {

    @GET("/users/{username}")
    public UserData getUserData(@Path("username") String userName);

    @GET("/repos/{username}/{reponame}")
    public RepoInfo getRepoData(@Path("username") String userName,
                                @Path("reponame") String repoName);

    @GET("/repos/{username}/{reponame}/commits")
    public ArrayList<CommitsDatum> getCommitsData(@Path("username") String userName,
                                                  @Path("reponame") String repoName);

    @GET("/repos/{username}/{reponame}/branches")
    public ArrayList<Branch> getBranchesData(@Path("username") String userName,
                                             @Path("reponame") String repoName);


    @GET("/users/{username}")
    public void getUserData(@Path("username") String userName,
                                Callback<UserData> callback);

    @GET("/repos/{username}/{reponame}")
    public void getRepoData(@Path("username") String userName,
                                @Path("reponame") String repoName,
                                Callback<RepoInfo> callback);

    @GET("/repos/{username}/{reponame}/commits")
    public void getCommitsData(@Path("username") String userName,
                                                  @Path("reponame") String repoName,
                                                  Callback<ArrayList<CommitsDatum>> callback);

    @GET("/repos/{username}/{reponame}/branches")
    public void getBranchesData(@Path("username") String userName,
                                             @Path("reponame") String repoName,
                                             Callback<ArrayList<Branch>> callback);
}
