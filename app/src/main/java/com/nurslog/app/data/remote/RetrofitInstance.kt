package com.nurslog.app.data.remote

import com.nurslog.app.data.remote.icd10.Icd10Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val ICD10_BASE_URL = "https://clinicaltables.nlm.nih.gov/"

    val icd10Api: Icd10Api by lazy {
        Retrofit.Builder()
            .baseUrl(ICD10_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Icd10Api::class.java)
    }
}