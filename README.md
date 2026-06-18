# Gemini Playground

Gemini Playground is an Android application built for experimenting with Large Language Model (LLM) concepts using Google's Gemini API.

The project serves as a learning laboratory for exploring prompt engineering, generation parameters, structured output, tool calling, and other modern AI application patterns.

## Features

- Interactive AI Chat
- Prompt Engineering Experiments
- System Instruction Testing
- Context Management Experiments
- Temperature Control
- TopP Control
- TopK Control
- Markdown Rendering
- Robust Error Handling
- Jetpack Compose UI

## Tech Stack

### Language
- Kotlin

### UI
- Jetpack Compose
- Material 3

### Architecture
- MVVM
- Kotlin Coroutines

### AI
- Google GenAI SDK (Gemini)

### Serialization
- Kotlinx Serialization

### Markdown Rendering
- compose-markdown

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

## AI Concepts Explored

### Prompt Engineering
Experimenting with prompt design techniques to influence model behavior and response quality.

### System Instructions
Using system-level instructions to define model behavior and response boundaries.

### Context Management
Passing conversation history to maintain continuity across interactions.

### Generation Parameters
Exploring the effects of:

- Temperature
- TopP
- TopK

on response creativity and determinism.

### Structured Output
Generating JSON responses and parsing them into Kotlin data models.

### Tool Calling
Experimenting with external tool execution and integrating tool results into AI workflows.

### Error Handling
Handling API failures, quota limits, and retry mechanisms.

## Roadmap

### Completed

- [x] Basic Gemini Integration
- [x] Prompt Engineering
- [x] System Instructions
- [x] Temperature Configuration
- [x] TopP Configuration
- [x] TopK Configuration
- [x] Markdown Rendering
- [x] Structured Output Experiments

### In Progress

- [ ] Calculator Tool
- [ ] Function Calling
- [ ] Tool Calling
- [ ] Multi-Turn Context Experiments

### Future Experiments

- [ ] Multimodal Inputs
- [ ] Streaming Responses
- [ ] Local LLM Integration
- [ ] MCP Integration
- [ ] RAG Experiments

## Project Structure

- `MainActivity` - Application entry point.
- `MainViewModel` - Manages UI state and AI interactions.
- `GeminiRepository` - Handles Gemini API communication.
- `Tool` - Defines the contract for application tools.
- `ToolExecutor` - Executes registered tools.
- `CalculatorTool` - Example tool used for tool-calling experiments.

## License

This project is licensed under the Apache License 2.0.
