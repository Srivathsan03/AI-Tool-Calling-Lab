# Gemini Playground

An Android application demonstrating the integration of the Google GenAI (Gemini) API with Jetpack Compose. This project showcases how to handle AI content generation.

## Features

- **Interactive AI Chat**: Submit text prompts to the Gemini 2.5 Flash model and receive real-time responses.
- **ViewModel Architecture**:
    - Centralized UI state management using `MainViewModel`.
    - Business logic separation from the UI layer.
    - Lifecycle-aware operations using `viewModelScope`.
- **Robust Error Handling**:
    - **Automatic Retries**: Implements exponential backoff for `429 Quota Exceeded` errors.
    - **Safe Networking**: Prevents `NetworkOnMainThreadException` by using `Dispatchers.IO` for blocking API calls inside the ViewModel.
    - **Client Validation**: Gracefully handles and displays client-side and server-side errors.
- **Modern Android Stack**:
    - Built with **Jetpack Compose** and **Material 3**.
    - Responsive UI with scrolling support and loading states.
    - Secure API key management via `local.properties`.

## Prerequisites

- Android Studio Ladybug (2024.2.1) or newer.
- A Gemini API Key from the [Google AI Studio](https://aistudio.google.com/).

## Setup

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    ```
2.  **Add your API Key**:
    Open the `local.properties` file in the root directory and add your key:
    ```properties
    GEMINI_API_KEY=your_actual_api_key_here
    ```
3.  **Sync and Run**:
    Sync the project with Gradle files and run the `app` module on an emulator or physical device.

## Project Structure

- `MainActivity.kt`: Entry point that sets up the UI and provides the `MainViewModel`.
- `MainViewModel.kt`: Manages the AI response state, loading status, and the API interaction logic.
- `build.gradle.kts` (app): Configures the build process and injects the API key from `local.properties` into `BuildConfig`.
- `libs.versions.toml`: Manages project dependencies and versions.

## Key Implementation Details

### State Management
The UI state is exposed via `MutableState` within the `MainViewModel`:
```kotlin
class MainViewModel : ViewModel() {
    val response: MutableState<String> = mutableStateOf("")
    var isPending: MutableState<Boolean> = mutableStateOf(false)
    
    fun onButtonClick(prompt: String) {
        isPending.value = true
        viewModelScope.launch {
            response.value = geminiResponse(prompt)
            isPending.value = false
        }
    }
}
```

### Main Thread Safety
Since the `google-genai` library uses blocking OkHttp calls, we ensure thread safety using `withContext(Dispatchers.IO)` inside the ViewModel:
```kotlin
suspend fun geminiResponse(prompt: String): String = withContext(Dispatchers.IO) {
    // Blocking network call happens here safely
    client.models.generateContent(...)
}
```

## Dependencies

- `com.google.genai:google-genai:1.57.0`
- `androidx.compose.material3:material3`
- `androidx.lifecycle:lifecycle-viewmodel-ktx`
- `com.squareup.okhttp3:okhttp`

## License

This project is licensed under the Apache License 2.0.
