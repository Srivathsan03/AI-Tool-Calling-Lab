package com.sri.geminiplayground.tool.news

import com.sri.geminiplayground.tool.Tool

class NewsTool(
    private val newsRepository: NewsRepository
) : Tool {

    override val name = "news"
    override val description = "Retrieves top hacker news stories"

    override suspend fun execute(input: String?): String {
        val topNews = newsRepository.getTopNews()
        return buildString {
            appendLine("Top HackerNews Stories")
            appendLine()
            topNews.forEachIndexed { index, newsStoryResp ->
                appendLine("${index + 1}. ${newsStoryResp.title}")
            }
        }
    }
}