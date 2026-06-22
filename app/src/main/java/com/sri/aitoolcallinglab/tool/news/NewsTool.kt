package com.sri.aitoolcallinglab.tool.news

import com.sri.aitoolcallinglab.tool.Tool

class NewsTool(
    private val newsRepository: NewsRepository
) : Tool {

    override val name = "news"
    override val description = "Retrieves top hacker news stories"
    override val guidance = "Use news for current news and headlines."
    override val examples = """
        Latest news headlines
        TOOL: news
        INPUT: (not needed)
    """.trimIndent()

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