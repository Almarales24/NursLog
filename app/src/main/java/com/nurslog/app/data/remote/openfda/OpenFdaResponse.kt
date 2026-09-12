package com.nurslog.app.data.remote.openfda

data class OpenFdaResponse(
    val results: List<OpenFdaResult>?
)

data class OpenFdaResult(
    val warnings: List<String>?,
    val dosage_and_administration: List<String>?
)