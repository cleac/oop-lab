package com.cleac.watcherforgithub.data.rest;

import android.test.AndroidTestCase;

import com.cleac.watcherforgithub.data.rest.model.UserData;
import com.cleac.watcherforgithub.data.rest.model.branch.Branch;
import com.cleac.watcherforgithub.data.rest.model.committs.CommitsDatum;
import com.cleac.watcherforgithub.data.rest.model.repo.RepoInfo;

import java.util.ArrayList;

/**
 * Created by cleac on 03.04.15.
 */
public class TestGetInfo extends AndroidTestCase {

    public final static String LOG_TAG = TestGetInfo.class.getSimpleName();
    public static final String test_name = "cleac";
    public static final String test_repo = "studyjams";
    public static final String test_branch_master = "master";
    private RestClient restAdapter;

    public void testGetUserInfo() throws Throwable {
        //Gets info of user cleac
        UserData testData = RestClient.instance().ApiService().getUserData(test_name);
        assertEquals(test_name,testData.getLogin());
    }

    public void testGetRepoInfo() throws  Throwable {
        RepoInfo testData = RestClient.instance().ApiService().getRepoData(test_name, test_repo);
        assertEquals(test_repo,testData.getName());
    }

    public void testGetCommitsData() throws Throwable {
        ArrayList<CommitsDatum> testData = RestClient.instance().ApiService().getCommitsData(test_name,test_repo);
        assertEquals(test_name, testData.get(0).getAuthor().getLogin());
    }

    public void testGetBranchesData() throws Throwable {
        ArrayList<Branch> testData = RestClient.instance().ApiService().getBranchesData(test_name, test_repo);
        assertEquals(test_branch_master, testData.get(0).getName());
    }
}
