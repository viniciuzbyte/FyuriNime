package com.fyurinime.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fyurinime.app.data.repository.AnimeRepository
import com.fyurinime.app.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

data class HomeUiState(
    val isLoading: Boolean = true,
    val trending: List<Anime> = emptyList(),
    val seasonNow: List<Anime> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val trending = runCatching { retry { animeRepository.getTrending() } }
            delay(1100) // batas Jikan: 3 req/detik
            val season = runCatching { retry { animeRepository.getSeasonNow() } }

            val err = trending.exceptionOrNull() ?: season.exceptionOrNull()
            _uiState.value = HomeUiState(
                isLoading = false,
                trending = trending.getOrDefault(emptyList()),
                seasonNow = season.getOrDefault(emptyList()),
                errorMessage = err?.let { "Gagal memuat: ${it.javaClass.simpleName} ${it.message}" }
            )
        }
    }

    private suspend fun <T> retry(times: Int = 3, block: suspend () -> T): T {
        var last: Exception? = null
        repeat(times) { i ->
            try {
                return block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                last = e
                delay(1200L * (i + 1))
            }
        }
        throw last!!
    }
}
