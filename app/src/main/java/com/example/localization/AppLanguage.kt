package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    MARATHI("mr", "Marathi", "मराठी"),
    ENGLISH("en", "English", "English");

    fun toggle(): AppLanguage {
        return if (this == MARATHI) ENGLISH else MARATHI
    }
}
