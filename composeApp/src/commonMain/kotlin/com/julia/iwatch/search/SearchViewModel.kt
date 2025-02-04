package com.julia.iwatch.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.iwatch.item.CreateItemRequest
import com.julia.iwatch.item.ItemsRepository
import com.julia.iwatch.tmdb.TMDB_IMAGE_BASE_URL
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

/**
 * View model for the search screen.
 */
class SearchViewModel(
    private val repository: SearchRepository = SearchRepository(),
    private val itemsRepository: ItemsRepository = ItemsRepository()
): ViewModel() {

    private lateinit var listId: String

    /**
     * The current state of the search screen.
     */
    var uiState by mutableStateOf(SearchUiState())
        private set

    fun initialize(listId: String) {
        this.listId = listId
    }

    /**
     * Sets the typed query.
     */
    fun setQuery(query: String) {
        uiState = uiState.copy(query = query)
    }

    /**
     * Attempts to load a suggestions of items by the typed query.
     */
    fun fetchSuggestions() {
        viewModelScope.launch {
            try {
                val suggestions = repository.searchMovies(uiState.query).map { it.title }
                uiState = uiState.copy(suggestions = suggestions)
            } catch (error: Throwable) {
                // Do nothing
            }
        }
    }

    /**
     * Attempts to find the searched item.
     */
    fun search() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(
                    results = null,
                    loading = true,
                    suggestions = emptyList()
                )

                val results = repository.searchMovies(uiState.query)

                uiState = uiState.copy(results = results)
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    /**
     * Attempts to add the item to list.
     */
    fun addToList(result: SearchResult) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(adding = true)

                val releaseYear = try {
                    result.releaseDate?.let { LocalDate.parse(it).year.toString() }
                } catch (error: Throwable) {
                    null
                }

                itemsRepository.create(
                    request = CreateItemRequest(
                        name = result.title,
                        year = releaseYear,
                        posterUrl = result.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
                        tmdbId = result.id
                    ),
                    listId = listId,
                )

                uiState = uiState.copy(addedToList = true)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(adding = false)
            }
        }
    }

    /**
     * Dismisses the error dialog.
     */
    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}