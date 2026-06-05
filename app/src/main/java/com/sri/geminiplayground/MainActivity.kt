package com.sri.geminiplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.GenerateContentConfig
import com.sri.geminiplayground.ui.theme.GeminiPlaygroundTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeminiPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Playground(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Playground(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
    ) {
        val prompt = rememberTextFieldState()
        var response by remember { mutableStateOf("") }
        var isPending by remember { mutableStateOf(false) }

        TextField(
            state = prompt,
            enabled = !isPending
        )
        Button(
            onClick = {
                isPending = true
                scope.launch {
                    response = geminiResponse(prompt.text.toString())
                    isPending = false
                }
            },
            enabled = !isPending
        ) {
            Text(
                text = if (isPending) "Thinking..." else "Submit"
            )
        }
        Text(
            modifier = Modifier.scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Vertical
            ),
            text = if (isPending) "" else response
        )
    }
}

suspend fun geminiResponse(prompt: String): String = withContext(Dispatchers.IO) {
    val client = Client.builder()
        .apiKey(BuildConfig.GEMINI_API_KEY)
        .build()

    var attempt = 0
    val maxAttempts = 3
    var currentDelay = 2000L

    while (attempt < maxAttempts) {
        try {
            val response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                GenerateContentConfig.builder().build()
            )
            return@withContext response.text() ?: "Empty response"
        } catch (e: ClientException) {
            val msg = e.message() ?: ""
            if (e.code() == 429) {
                if (attempt < maxAttempts - 1) {
                    attempt++
                    kotlinx.coroutines.delay(currentDelay)
                    currentDelay *= 2
                    continue
                }
                return@withContext if (msg.contains("retry in")) {
                    "Quota exceeded. Retry in ${msg.substringAfter("retry in").trim()}."
                } else {
                    "Quota exceeded. Please try again later."
                }
            }
            return@withContext "Client error (${e.code()}): $msg"
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error: ${e.message}"
        }
    }
    "Failed after $maxAttempts attempts"
}

@Preview(showBackground = true)
@Composable
fun Preview_Playground() {
    GeminiPlaygroundTheme {
        Playground()
    }
}