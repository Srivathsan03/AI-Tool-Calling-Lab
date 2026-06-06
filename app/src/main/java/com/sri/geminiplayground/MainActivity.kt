package com.sri.geminiplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sri.geminiplayground.ui.theme.GeminiPlaygroundTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MainViewModel =
            ViewModelProvider(this, MainViewModel.Factory)[MainViewModel::class.java]
        setContent {
            GeminiPlaygroundTheme {
                Playground(
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Playground(viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Gemini Playground")
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.onConfigClicked()
                    }) {
                        Text(text = "Config")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val promptTextFieldState = rememberTextFieldState()
            TextField(
                modifier = Modifier.fillMaxWidth(),
                state = promptTextFieldState,
                enabled = !viewModel.isPending.value,
                label = { Text(text = "Prompt") }
            )
            Button(
                onClick = {
                    viewModel.onButtonClick(
                        prompt = promptTextFieldState.text.toString()
                    )
                },
                enabled = !viewModel.isPending.value
            ) {
                Text(
                    text = if (viewModel.isPending.value) "Thinking..." else "Submit"
                )
            }
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                markdown =
                    if (viewModel.isPending.value) ""
                    else viewModel.response.value
            )
        }
        ConfigPopup(viewModel = viewModel)
    }
}

@Composable
fun ConfigPopup(viewModel: MainViewModel) {
    if (viewModel.configPopupVisible.value) {
        val savedSystemPrompt by viewModel.repository.systemPrompt.collectAsStateWithLifecycle("")
        val savedTemperature by viewModel.repository.temperature.collectAsStateWithLifecycle(0.1f)
        val savedTopK by viewModel.repository.topK.collectAsStateWithLifecycle(50f)
        val savedTopP by viewModel.repository.topP.collectAsStateWithLifecycle(0.9f)

        val systemPromptTextFieldState = rememberTextFieldState()
        val temperatureTextFieldState = rememberTextFieldState()
        val topKTextFieldState = rememberTextFieldState()
        val topPTextFieldState = rememberTextFieldState()

        LaunchedEffect(savedSystemPrompt, savedTemperature, savedTopK, savedTopP) {
            systemPromptTextFieldState.edit { replace(0, length, savedSystemPrompt) }
            temperatureTextFieldState.edit { replace(0, length, savedTemperature.toString()) }
            topKTextFieldState.edit { replace(0, length, savedTopK.toInt().toString()) }
            topPTextFieldState.edit { replace(0, length, savedTopP.toString()) }
        }

        Dialog(
            onDismissRequest = {
                viewModel.configPopupVisible.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = systemPromptTextFieldState,
                    label = { Text(text = "System Prompt") },
                    lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = temperatureTextFieldState,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text(text = "Temperature") },
                    placeholder = { Text(text = "e.g. 0.5") },
                    inputTransformation = InputTransformation {
                        if (asCharSequence().any { (!it.isDigit() && it != '.') }) {
                            revertAllChanges()
                        }
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = topKTextFieldState,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Top K") },
                    placeholder = { Text(text = "e.g. 5") },
                    inputTransformation = InputTransformation {
                        if (asCharSequence().any { !it.isDigit() }) {
                            revertAllChanges()
                        }
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = topPTextFieldState,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text(text = "Top P") },
                    placeholder = { Text(text = "e.g. 0.9") },
                    inputTransformation = InputTransformation {
                        if (asCharSequence().any { (!it.isDigit() && it != '.') }) {
                            revertAllChanges()
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.configPopupVisible.value = false
                            viewModel.saveConfig(
                                systemPrompt = systemPromptTextFieldState.text.toString(),
                                temperature = temperatureTextFieldState.text.toString()
                                    .toFloatOrNull() ?: 0.1f,
                                topK = topKTextFieldState.text.toString().toFloatOrNull() ?: 5f,
                                topP = topPTextFieldState.text.toString().toFloatOrNull() ?: 0.9f
                            )
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Playground() {
    GeminiPlaygroundTheme {
        Playground(
            viewModel = MainViewModel(
                MainRepository(LocalContext.current)
            )
        )
    }
}