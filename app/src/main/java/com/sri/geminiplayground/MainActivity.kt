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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.sri.geminiplayground.ui.theme.GeminiPlaygroundTheme

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
            enabled = !(viewModel?.isPending?.value ?: false)
        )
        Button(
            onClick = {
                viewModel?.onButtonClick(promptTextFieldState.text.toString())
            },
            enabled = !(viewModel?.isPending?.value ?: false)
        ) {
            Text(
                text = if (viewModel?.isPending?.value ?: false) "Thinking..." else "Submit"
            )
        }
        Text(
            text =
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