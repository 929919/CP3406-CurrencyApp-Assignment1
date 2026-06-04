package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.repository

import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.model.ExchangeRateResponse
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.remote.ExchangeRateApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyRepository {

    private val api: ExchangeRateApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }

    suspend fun getRates(baseCurrency: String): ExchangeRateResponse {
        return api.getRates(
            apiKey = "4ce2829e5b5a19f1df5a4a6c",
            baseCurrency = baseCurrency
        )
    }

}