package com.example.vkeducationkotlindz1

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class GifUiState(
    val items: List<GifObject> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: Boolean = false,
    val offset: Int = 0
)

class GiphyViewModel : ViewModel() {

    private val api = createApi()
    private val apiKey = BuildConfig.GIPHY_API_KEY



    private val _uiState = mutableStateOf(GifUiState())
    val uiState: State<GifUiState> = _uiState

    fun loadInitial() {
        if (_uiState.value.items.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = false)
            try {
                val response = api.loadTrending(apiKey)
                _uiState.value = _uiState.value.copy(
                    items = response.data,
                    isLoading = false,
                    offset = response.data.size
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = true)
            }
        }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            try {
                val response = api.loadTrending(apiKey, offset = _uiState.value.offset)
                _uiState.value = _uiState.value.copy(
                    items = _uiState.value.items + response.data,
                    isLoadingMore = false,
                    offset = _uiState.value.offset + response.data.size
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoadingMore = false)
            }
        }
    }
}
