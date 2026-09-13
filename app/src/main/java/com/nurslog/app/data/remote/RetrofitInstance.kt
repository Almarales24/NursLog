package com.nurslog.app.data.remote

import com.nurslog.app.data.remote.icd10.Icd10Api
import com.nurslog.app.data.remote.openfda.OpenFdaApi
import com.nurslog.app.data.remote.rxnorm.RxNormApi
import com.nurslog.app.data.remote.translate.TranslateApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton que proporciona instancias de APIs externas mediante Retrofit
object RetrofitInstance {

    // URL base de la API de Clasificación ICD-10 (NIH)
    private const val ICD10_BASE_URL = "https://clinicaltables.nlm.nih.gov/"
    // URL base de la API de drogas y medicamentos (FDA)
    private const val OPENFDA_BASE_URL = "https://api.fda.gov/"
    // URL base de la API de traducción (MyMemory)
    private const val TRANSLATE_BASE_URL = "https://api.mymemory.translated.net/"
    // URL base de la API de medicamentos normalizados (NIH)
    private const val RXNORM_BASE_URL = "https://rxnav.nlm.nih.gov/"

    // Proporciona acceso a la API de ICD-10 (se crea una sola vez por lazy)
    val icd10Api: Icd10Api by lazy {
        Retrofit.Builder()
            .baseUrl(ICD10_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Icd10Api::class.java)
    }

    // Proporciona acceso a la API de OpenFDA (se crea una sola vez por lazy)
    val openFdaApi: OpenFdaApi by lazy {
        Retrofit.Builder()
            .baseUrl(OPENFDA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFdaApi::class.java)
    }

    // Proporciona acceso a la API de Traducción (se crea una sola vez por lazy)
    val translateApi: TranslateApi by lazy {
        Retrofit.Builder()
            .baseUrl(TRANSLATE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateApi::class.java)
    }

    // Proporciona acceso a la API de RxNorm (se crea una sola vez por lazy)
    val rxNormApi: RxNormApi by lazy {
        Retrofit.Builder()
            .baseUrl(RXNORM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RxNormApi::class.java)
    }
}