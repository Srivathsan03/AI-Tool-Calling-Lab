package com.sri.geminiplayground.tool.news

class NewsRepository {

    suspend fun getTopNews(): List<NewsStoryResp> {
        val ids = NewsApiRetrofitProvider.api.getTopStories()
        return ids
            .take(5)
            .map { id ->
                NewsApiRetrofitProvider.api.getStory(id = id)
            }
    }
}