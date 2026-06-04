package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.model

data class ExchangeRateResponse(
    val result: String,
    val base_code: String,
    val conversion_rates: Map<String, Double>
)