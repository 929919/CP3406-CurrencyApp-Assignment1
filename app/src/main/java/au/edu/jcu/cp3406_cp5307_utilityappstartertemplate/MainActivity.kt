package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme.CP3406_CP5603UtilityAppStarterTemplateTheme
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.viewmodel.CurrencyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CP3406_CP5603UtilityAppStarterTemplateTheme {
                UtilityApp()
            }
        }
    }
}

@Composable
fun UtilityApp() {
    val viewModel: CurrencyViewModel = viewModel()
    var selectedTab by remember { mutableStateOf("Utility") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                    label = { Text("Converter") },
                    selected = selectedTab == "Utility",
                    onClick = { selectedTab = "Utility" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Utility" -> UtilityScreen(viewModel)
                "Settings" -> SettingsScreen(viewModel)
            }
        }
    }
}

@Composable
fun UtilityScreen(viewModel: CurrencyViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val amount = uiState.amount.toDoubleOrNull() ?: 1.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Currency Converter", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = { viewModel.onAmountChange(it) },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Base: ${uiState.baseCurrency}", style = MaterialTheme.typography.bodyLarge)

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            Button(onClick = { viewModel.fetchRates() }) {
                Text("Try Again")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.favoriteCurrencies) { currency ->
                    val rate = uiState.rates[currency]
                    val converted = rate?.let { "%.2f".format(amount * it) } ?: "--"
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(currency, style = MaterialTheme.typography.titleMedium)
                            Text(converted, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: CurrencyViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allCurrencies = listOf("USD", "BRL", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "INR")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Text("Base Currency", style = MaterialTheme.typography.titleMedium)

        allCurrencies.forEach { currency ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = uiState.baseCurrency == currency,
                    onClick = { viewModel.onBaseCurrencyChange(currency) }
                )
                Text(currency)
            }
        }

        HorizontalDivider()
        Text("Favorite Currencies", style = MaterialTheme.typography.titleMedium)

        allCurrencies.forEach { currency ->
            val isChecked = currency in uiState.favoriteCurrencies
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        val updated = if (checked)
                            uiState.favoriteCurrencies + currency
                        else
                            uiState.favoriteCurrencies - currency
                        viewModel.onFavoriteCurrenciesChange(updated)
                    }
                )
                Text(currency)
            }
        }
    }
}