# Currency Converter App
### CP3406 - Mobile Technologies - Utility App | Assignment 1
**James Cook University** /
**CP3406 - Mobile Computing**

---

## Overview
Currency Converter is a utility Android app that provides real-time currency exchange rates. Users can instantly convert values between 30+ world currencies, customize their favorite currencies, and switch between light and dark themes.

---

## Features

### 🔄 Real-Time Currency Conversion
- Fetches live exchange rates from [ExchangeRate-API](https://www.exchangerate-api.com/)
- Supports 30+ world currencies
- Instant conversion as the user types

### 🔍 Currency Search
- Search bar on the Converter screen to quickly filter displayed currencies
- Search bar on the Settings screen to find and configure currencies

### ⭐ Favorite Currencies
- Users can select which currencies appear on the main converter screen
- Managed via the Settings screen with checkboxes

### 🌍 Base Currency Selection
- Users can change the base currency from the Settings screen
- App automatically fetches new rates when base currency changes

### 🌙 Dark Mode
- Toggle between light and dark themes from the Settings screen
- Fully supports Material Design 3 dynamic theming

### 🎨 Color-Coded Currency Cards
- Each currency has a unique color for quick visual identification

---

## Architecture

This app follows modern Android architecture best practices:

- **Jetpack Compose** — declarative UI with Material Design 3
- **ViewModel** — manages UI state and survives configuration changes
- **Repository Pattern** — `CurrencyRepository` abstracts data access
- **StateFlow** — reactive state management between ViewModel and UI
- **Retrofit** — HTTP client for consuming the ExchangeRate API
- **Coroutines** — asynchronous network calls without blocking the UI

### Project Structure

| File | Location | Purpose |
|---|---|---|
| `ExchangeRateResponse.kt` | `data/model/` | Data model for API response |
| `ExchangeRateApi.kt` | `data/remote/` | Retrofit interface for API calls |
| `CurrencyRepository.kt` | `data/repository/` | Abstracts data access layer |
| `CurrencyViewModel.kt` | `ui/viewmodel/` | Manages UI state with StateFlow |
| `MainActivity.kt` | root | UtilityScreen and SettingsScreen UI |
| `Color.kt / Theme.kt` | `ui/theme/` | Material Design 3 theming |