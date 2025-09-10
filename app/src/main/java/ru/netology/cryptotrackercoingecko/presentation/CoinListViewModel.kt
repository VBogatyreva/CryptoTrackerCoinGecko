package ru.netology.cryptotrackercoingecko.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.cryptotrackercoingecko.data.repository.CoinRepositoryImpl
import ru.netology.cryptotrackercoingecko.domain.CoinInfo

class CoinListViewModel : ViewModel() {

    private val repository = CoinRepositoryImpl

    private val _coinList = MutableStateFlow<List<CoinInfo>>(emptyList())
    val coinList: StateFlow<List<CoinInfo>> = _coinList.asStateFlow()

    private val _searchResults = MutableStateFlow<List<CoinInfo>>(emptyList())
    val searchResults: StateFlow<List<CoinInfo>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _showSearchResults = MutableStateFlow(false)
    val showSearchResults: StateFlow<Boolean> = _showSearchResults.asStateFlow()

    private val _currentFilter = MutableStateFlow<String>("all")
    val currentFilter: StateFlow<String> = _currentFilter.asStateFlow()

    private val _filteredCoinList = MutableStateFlow<List<CoinInfo>>(emptyList())
    val filteredCoinList: StateFlow<List<CoinInfo>> = _filteredCoinList.asStateFlow()


    init {
        viewModelScope.launch {
            repository.getCoinList().collectLatest { coins ->
                _coinList.value = coins
                applyFilter(_currentFilter.value)
            }
        }
    }

    fun applyFilter(filter: String) {
        _currentFilter.value = filter
        applyFilterToCurrentData()
    }

    private fun applyFilterToCurrentData() {
        val currentData = if (_currentFilter.value != "all") {
            _coinList.value.sortedByDescending { it.price?.toDoubleOrNull() ?: 0.0 }
        } else {
            _coinList.value
        }
        _filteredCoinList.value = when (_currentFilter.value) {
            "top3" -> currentData.take(3)
            "top5" -> currentData.take(5)
            "top10" -> currentData.take(10)
            "top50" -> currentData.take(50)
            "top100" -> currentData.take(100)
            else -> currentData
        }
    }

    fun loadCoinList() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.refreshCoinList()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchCoins(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.searchCoins(name).collect{ coins ->
                    _searchResults.value = coins
                    _showSearchResults.value = name.isNotEmpty()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
        _showSearchResults.value = false
    }
}