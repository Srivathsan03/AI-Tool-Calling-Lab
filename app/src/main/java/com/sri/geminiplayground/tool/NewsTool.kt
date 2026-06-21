package com.sri.geminiplayground.tool

class NewsTool(
    private val newsRepository: NewsRepository
) : Tool {

    override val name = "news"

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