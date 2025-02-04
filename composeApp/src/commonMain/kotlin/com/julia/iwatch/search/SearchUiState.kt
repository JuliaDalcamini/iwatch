package com.julia.iwatch.search

/**
 * Search screen state.
 */
data class SearchUiState(
    val query: String = "",
    val results: List<SearchResult>? = null,
    val suggestions: List<String> = emptyList(),
    val showError: Boolean = false,
    val loading: Boolean = false,
    val adding: Boolean = false,
    val addedToList: Boolean = false,
    val actionError: Boolean = false
)