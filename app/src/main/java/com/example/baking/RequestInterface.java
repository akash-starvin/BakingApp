package com.example.baking;

import com.example.baking.properties.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET(Constants.JSON)
    Call<List<Recipe>> getJSON();
}
