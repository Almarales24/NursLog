package com.nurslog.app.data.remote.translate

data class TranslateResponse(
    val responseData: TranslateResponseData?
)

data class TranslateResponseData(
    val translatedText: String?
)