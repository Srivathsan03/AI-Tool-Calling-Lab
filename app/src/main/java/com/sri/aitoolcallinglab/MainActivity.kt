package com.sri.aitoolcallinglab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.sri.aitoolcallinglab.ui.theme.AiToolCallingTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContent {
            AiToolCallingTheme {
                ScreenContent(
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
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
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_ScreenContent() {
    AiToolCallingTheme {
        ScreenContent(
            viewModel = MainViewModel()
        )
    }
}