package com.julia.iwatch.item

/**
 * Items screen state.
 */
data class ItemsUiState(
    val items: List<Item>? = null,
    val itemToDelete: Item? = null,
    val showError: Boolean = false,
    val loading: Boolean = false,
    val actionError: Boolean = false
)