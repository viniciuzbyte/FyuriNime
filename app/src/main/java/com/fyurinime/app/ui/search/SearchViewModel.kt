package com.fyurinime.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fyurinime.app.data.repository.AnimeRepository
import com.fyurinime.app.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

data class SearchUiState(
    val query: String = "",
    val results: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _state.value = _state.value.copy(query = query)
        searchJob?.cancel()

        if (query.isBlank()) {
            _state.value = _state.value.copy(results = emptyList(), isLoading = false, errorMessage = null)
            return
        }

        searchJob = viewModelScope.launch {
            delay(700) // debounce, hemat kuota request Jikan
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val results = animeRepository.search(query.trim())
                _state.value = _state.value.copy(results = results, isLoading = false)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal mencari: ${e.javaClass.simpleName} ${e.message}"
                )
            }
        }
    }
}
