package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CurrencyUiState(
    val isLoading: Boolean = false,
    val baseCurrency: String = "USD",
    val amount: String = "1",
    val rates: Map<String, Double> = emptyMap(),
    val favoriteCurrencies: List<String> = listOf("BRL", "EUR", "GBP", "JPY", "AUD"),
    val errorMessage: String? = null
)

class CurrencyViewModel : ViewModel() {

    private val repository = CurrencyRepository()

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState

    init {
        fetchRates()
    }

    fun fetchRates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getRates(_uiState.value.baseCurrency)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    rates = response.conversion_rates
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao buscar taxas. Verifique sua conexão."
                )
            }
        }
    }

    fun onAmountChange(newAmount: String) {
        _uiState.value = _uiState.value.copy(amount = newAmount)
    }

    fun onBaseCurrencyChange(newBase: String) {
        _uiState.value = _uiState.value.copy(baseCurrency = newBase)
        fetchRates()
    }

    fun onFavoriteCurrenciesChange(newFavorites: List<String>) {
        _uiState.value = _uiState.value.copy(favoriteCurrencies = newFavorites)
    }
}