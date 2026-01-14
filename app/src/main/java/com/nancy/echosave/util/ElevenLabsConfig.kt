package com.nancy.echosave.util

import com.nancy.echosave.BuildConfig


object ElevenLabsConfig {
    const val BASE_URL = "https://api.elevenlabs.io/"
    const val MODEL_MULTILINGUAL = "eleven_multilingual_v2"
    const val MODEL_FLASH = "eleven_flash_v2_5"

    val API_KEY: String
        get() = BuildConfig.ELEVEN_LABS_API_KEY
}
