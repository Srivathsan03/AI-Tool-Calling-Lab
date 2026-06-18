package com.sri.geminiplayground.tool

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentTimeTool : Tool {

    override val name = "time"

    override fun execute(input: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return sdf.format(Date())
    }
}