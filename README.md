# Gemini Playground

Gemini Playground is an Android application built for experimenting with Large Language Model (LLM) concepts using Google's Gemini API.

The project serves as a hands-on learning laboratory for exploring modern AI application development, including prompt engineering, structured output, tool calling, context management, and agent architecture patterns.

## Features

- Interactive AI Chat
- Prompt Engineering Experiments
- System Instruction Testing
- Context Management Experiments
- Temperature Control
- TopP Control
- TopK Control
- Calculator Tool
- Weather Tool
- Currency Tool
- Tool Registry & Tool Executor Architecture
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
- Repository Pattern
- Kotlin Coroutines

### AI
- Google GenAI SDK (Gemini)

### Networking
- Retrofit
- Gson Converter

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

## Tool Architecture

```text
User Prompt
    ↓
ChatRunner
    ↓
ToolExecutor
    ↓
ToolRegistry
    ↓
Selected Tool
    ↓
External API / Local Logic
    ↓
Response
```

### Implemented tools:

- Calculator Tool
- Weather Tool
- Currency Tool

## Roadmap

### Completed

- [x] Basic Gemini Integration
- [x] Prompt Engineering
- [x] System Instructions
- [x] Temperature Configuration
- [x] TopP Configuration
- [x] TopK Configuration
- [x] Calculator Tool
- [x] Weather Tool
- [x] Currency Tool
- [x] Tool Registry & Tool Executor Architecture
- [x] Tool Selection Logic
- [x] Markdown Rendering

### In Progress

- [ ] Function Calling
- [ ] Multi-Turn Context Experiments

### Future Experiments

- [ ] Multimodal Inputs
- [ ] Local LLM Integration
- [ ] MCP Integration
- [ ] RAG Experiments
- [ ] Agentic Workflows

## Project Structure

- `MainActivity` - Application entry point.
- `MainViewModel` - Manages UI state and AI interactions.
- `GeminiRepository` - Handles Gemini API communication.
- `Tool` - Defines the contract for application tools.
- `ToolRegistry` - Stores available tools
- `ToolExecutor` - Executes registered tools.
- `CalculatorTool` - Example tool used for tool-calling experiments.
- `WeatherTool` - Weather data retrieval
- `CurrencyTool` - Currency conversion

## License

This project is licensed under the Apache License 2.0.
