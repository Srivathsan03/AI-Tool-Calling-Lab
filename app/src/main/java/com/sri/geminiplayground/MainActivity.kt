package com.sri.geminiplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.sri.geminiplayground.ui.theme.GeminiPlaygroundTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContent {
            GeminiPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Playground(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Playground(modifier: Modifier = Modifier, viewModel: MainViewModel? = null) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val promptTextFieldState = rememberTextFieldState()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            state = promptTextFieldState,
            enabled = !(viewModel?.isPending?.value ?: false),
            label = { Text(text = "Prompt") }
        )
        val temperatureTextFieldState = rememberTextFieldState()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            state = temperatureTextFieldState,
            enabled = !(viewModel?.isPending?.value ?: false),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(text = "Temperature") },
            placeholder = { Text(text = "e.g. 0.5") },
            inputTransformation = InputTransformation {
                if (asCharSequence().any { !it.isDigit() && it != '.' }) {
                    revertAllChanges()
                }
            }
        )
        val topKTextFieldState = rememberTextFieldState()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            state = topKTextFieldState,
            enabled = !(viewModel?.isPending?.value ?: false),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Top K") },
            placeholder = { Text(text = "e.g. 5") },
            inputTransformation = InputTransformation {
                if (asCharSequence().any { !it.isDigit() }) {
                    revertAllChanges()
                }
            }
        )
        val topPTextFieldState = rememberTextFieldState()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            state = topPTextFieldState,
            enabled = !(viewModel?.isPending?.value ?: false),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(text = "Top P") },
            placeholder = { Text(text = "e.g. 0.9") },
            inputTransformation = InputTransformation {
                if (asCharSequence().any { !it.isDigit() && it != '.' }) {
                    revertAllChanges()
                }
            }
        )
        Button(
            onClick = {
                viewModel?.onButtonClick(
                    prompt = promptTextFieldState.text.toString(),
                    temperature = temperatureTextFieldState.text.toString().toFloatOrNull() ?: 0f,
                    topK = topKTextFieldState.text.toString().toFloatOrNull() ?: 5f,
                    topP = topPTextFieldState.text.toString().toFloatOrNull() ?: 0.9f
                )
            },
            enabled = !(viewModel?.isPending?.value ?: false)
        ) {
            Text(
                text = if (viewModel?.isPending?.value ?: false) "Thinking..." else "Submit"
            )
        }
        MarkdownText(
            markdown =
                if (viewModel?.isPending?.value ?: false) ""
                else viewModel?.response?.value ?: ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Playground() {
    GeminiPlaygroundTheme {
        Playground()
    }
}