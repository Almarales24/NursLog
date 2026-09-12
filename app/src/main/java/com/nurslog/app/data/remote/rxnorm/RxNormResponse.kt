package com.nurslog.app.data.remote.rxnorm

data class RxNormDrugsResponse(
    val drugGroup: RxNormDrugGroup?
)

data class RxNormDrugGroup(
    val conceptGroup: List<RxNormConceptGroup>?
)

data class RxNormConceptGroup(
    val conceptProperties: List<RxNormConceptProperty>?
)

data class RxNormConceptProperty(
    val rxcui: String,
    val name: String
)