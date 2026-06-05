# Gemini Playground

An Android application demonstrating the integration of the Google GenAI (Gemini) API with Jetpack Compose. This project showcases how to handle AI content generation.

## Features

- **Interactive AI Chat**: Submit text prompts to the Gemini 2.5 Flash model and receive real-time responses.
- **Modern Android Stack**:
    - Built with **Jetpack Compose** and **Material 3**.
    - Lifecycle-aware coroutines using `rememberCoroutineScope`.
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

- `MainActivity.kt`: Contains the main UI logic using Jetpack Compose and the integration with the GenAI Client.
- `build.gradle.kts` (app): Configures the build process and injects the API key from `local.properties` into `BuildConfig`.
- `libs.versions.toml`: Manages project dependencies and versions.

## Key Implementation Details

### Handling Quota Limits (429)
The application includes a retry loop to handle the free-tier rate limits (e.g., 5 requests per minute):
```kotlin
while (attempt < maxAttempts) {
    try {
        // ... API Call ...
    } catch (e: ClientException) {
        if (e.code() == 429 && attempt < maxAttempts - 1) {
            delay(currentDelay)
            currentDelay *= 2
            attempt++
            continue
        }
        // ... Handle other errors ...
    }
}
```

### Main Thread Safety
Since the `google-genai` library uses blocking OkHttp calls, we ensure thread safety using `withContext(Dispatchers.IO)`:
```kotlin
suspend fun geminiResponse(prompt: String): String = withContext(Dispatchers.IO) {
    // Blocking network call happens here safely
    client.models.generateContent(...)
}
```

## Dependencies

- `com.google.genai:google-genai:1.57.0`
- `androidx.compose.material3:material3`
- `androidx.activity:activity-compose`
- `com.squareup.okhttp3:okhttp`

## License

This project is licensed under the Apache License 2.0.
