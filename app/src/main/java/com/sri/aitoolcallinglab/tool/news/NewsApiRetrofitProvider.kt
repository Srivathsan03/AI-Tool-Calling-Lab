package com.sri.aitoolcallinglab.tool.news

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsApiRetrofitProvider {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://hacker-news.firebaseio.com/v0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: NewsApi = retrofit.create(NewsApi::class.java)
}