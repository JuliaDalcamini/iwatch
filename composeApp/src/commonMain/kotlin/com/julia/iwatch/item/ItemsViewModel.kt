package com.julia.iwatch.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View model for the list of items screen.
 */
class ItemsViewModel(
    private val repository: ItemsRepository = ItemsRepository()
): ViewModel() {

    private lateinit var listId: String

    /**
     * The current state of the list of items screen.
     */
    var uiState by mutableStateOf(ItemsUiState())
        private set

    /**
     * Attempts to load a list of items.
     */
    fun getItems(listId: String) {
        this.listId = listId

        viewModelScope.launch {
            try {
                uiState = ItemsUiState(loading = true)

                val items = repository.getAll(listId)

                uiState = uiState.copy(items = items)
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    /**
     * Sets the checkbox of the item has a watched.
     */
    fun setWatched(item: Item, watched: Boolean) {
        viewModelScope.launch {
            try {
                repository.update(id = item.id, listId = listId, watched = watched)
                getItems(listId)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Attempts to delete an item.
     */
    fun delete(item: Item) {
        viewModelScope.launch {
            try {
                repository.delete(id = item.id, listId = listId)
                getItems(listId)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Show the delete dialog.
     */
    fun showDeleteDialog(list: Item) {
        uiState = uiState.copy(itemToDelete = list)
    }

    /**
     * Dismisses the delete dialog.
     */
    fun dismissDeleteDialog() {
        uiState = uiState.copy(itemToDelete = null)
    }

    /**
     * Dismisses the action error.
     */
    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}