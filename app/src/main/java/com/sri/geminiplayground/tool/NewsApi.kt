package com.sri.geminiplayground.tool

import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {

    @GET("topstories.json")
    suspend fun getTopStories(): List<Long>

    @GET("item/{id}.json")
    suspend fun getStory(@Path("id") id:Long): NewsStoryResp
}