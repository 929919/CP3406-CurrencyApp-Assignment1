package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.remote

import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    @GET("v6/{apiKey}/latest/{baseCurrency}")
    suspend fun getRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCurrency") baseCurrency: String
    ): ExchangeRateResponse

}