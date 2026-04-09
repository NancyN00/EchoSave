# EchoSave: ElevenLabs + Jetpack Compose

EchoSave is a Jetpack Compose Android app that lets users generate lifelike audio from text, upload audio to convert to text, and automatically save and manage audio clips in the cloud.

🚀 Features

* Text-to-Speech (TTS): Connects to ElevenLabs API to generate lifelike audio.
* Model Selection: Supports high-quality (Multilingual v2) and low-latency (Flash 2.5) models.
* Voice Selection: Toggle between curated AI voices.
* Cloud Persistence: Uses Firebase Firestore to automatically save generated audio metadata (text, voice, audio URL, timestamp).
* Audio Library: A secondary screen to list, play back, and manage saved clips directly from Firestore.
* Auto-Save & Toast: Every generated or uploaded audio is saved automatically with a “Saved ✓” toast notification.
* Dependency Injection: Uses Hilt for clean injection of repositories, Retrofit API service, and Firebase instances.
* Navigation: Uses Voyager for Jetpack Compose screens and bottom tab navigation.

🛠️ Tech Stack

* UI: Jetpack Compose
* Language: Kotlin
* Networking: Retrofit
* Cloud Storage: Firebase Storage for audio files
* Database: Firebase Firestore for metadata
* AI Audio: ElevenLabs API
* DI: Hilt for dependency injection (ViewModels, Repositories, Firestore, Storage, Retrofit)
* Navigation: Voyager for Compose navigation and bottom tabs

📱 Screenshots

<p align="center">
  <img width="300" alt="audiotts_screen" src="https://github.com/user-attachments/assets/7827af55-0dec-4da5-94f3-ab1650f1149e" />
 <img width="300" alt="saved_screen" src="https://github.com/user-attachments/assets/d2ca9056-dde0-46e3-a763-f2a9819e6b89" />
</p>
