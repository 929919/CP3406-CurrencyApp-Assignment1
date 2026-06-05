package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme.CP3406_CP5603UtilityAppStarterTemplateTheme
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.viewmodel.CurrencyViewModel

val currencyColors = mapOf(
    "USD" to Color(0xFF1565C0),
    "BRL" to Color(0xFF2E7D32),
    "EUR" to Color(0xFF6A1B9A),
    "GBP" to Color(0xFFC62828),
    "JPY" to Color(0xFFAD1457),
    "AUD" to Color(0xFF00838F),
    "CAD" to Color(0xFFE65100),
    "CHF" to Color(0xFF4527A0),
    "CNY" to Color(0xFFB71C1C),
    "INR" to Color(0xFF558B2F),
    "MXN" to Color(0xFF00695C),
    "SGD" to Color(0xFF0277BD),
    "HKD" to Color(0xFF283593),
    "NOK" to Color(0xFF4E342E),
    "SEK" to Color(0xFF37474F),
    "DKK" to Color(0xFF1B5E20),
    "NZD" to Color(0xFF006064),
    "ZAR" to Color(0xFF880E4F),
    "RUB" to Color(0xFF4A148C),
    "TRY" to Color(0xFFBF360C),
    "ARS" to Color(0xFF0D47A1),
    "CLP" to Color(0xFF1A237E),
    "COP" to Color(0xFFE65100),
    "PKR" to Color(0xFF1B5E20),
    "IDR" to Color(0xFF7B1FA2),
    "MYR" to Color(0xFF004D40),
    "PHP" to Color(0xFF1565C0),
    "THB" to Color(0xFF4A148C),
    "AED" to Color(0xFF01579B),
    "SAR" to Color(0xFF33691E)
)

val allAvailableCurrencies = currencyColors.keys.toList()

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
        Text("Currency Converter", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = { viewModel.onAmountChange(it) },
            label = { Text("Amount (${uiState.baseCurrency})") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            Button(onClick = { viewModel.fetchRates() }) {
                Text("Try Again")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(uiState.favoriteCurrencies) { currency ->
                    val rate = uiState.rates[currency]
                    val converted = rate?.let { "%.2f".format(amount * it) } ?: "--"
                    val cardColor = currencyColors[currency] ?: Color(0xFF455A64)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currency,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = converted,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Base Currency", style = MaterialTheme.typography.titleMedium)
        }

        items(allAvailableCurrencies) { currency ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = uiState.baseCurrency == currency,
                    onClick = { viewModel.onBaseCurrencyChange(currency) }
                )
                val dotColor = currencyColors[currency] ?: Color.Gray
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(dotColor, shape = MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(currency)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Favorite Currencies", style = MaterialTheme.typography.titleMedium)
            Text(
                "Select which currencies to show on the converter",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(allAvailableCurrencies) { currency ->
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
                val dotColor = currencyColors[currency] ?: Color.Gray
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(dotColor, shape = MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(currency)
            }
        }
    }
}