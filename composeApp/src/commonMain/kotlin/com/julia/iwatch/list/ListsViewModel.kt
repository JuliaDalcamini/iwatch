package com.julia.iwatch.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View model for the main screen.
 */
class ListsViewModel(
    private val repository: ListsRepository = ListsRepository()
): ViewModel() {

    /**
     * The current state of the main screen.
     */
    var uiState by mutableStateOf(ListsUiState())
        private set

    /**
     * Attempts to load all lists of the user.
     */
    fun getLists() {
        viewModelScope.launch {
            try {
                uiState = ListsUiState(loading = true)

                val itemLists = repository.getAll()

                uiState = uiState.copy(itemLists = itemLists)
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    /**
     * Attempts to rename the list.
     */
    fun rename(list: ItemList, newName: String) {
        viewModelScope.launch {
            try {
                repository.update(listId = list.id, newName = newName)
                getLists()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Attempts to delete the list.
     */
    fun delete(list: ItemList) {
        viewModelScope.launch {
            try {
                repository.delete(listId = list.id)
                getLists()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Attempts to create a new list.
     */
    fun create(name: String) {
        viewModelScope.launch {
            try {
                repository.create(name)
                getLists()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Shows the rename dialog.
     */
    fun showRenameDialog(list: ItemList) {
        uiState = uiState.copy(listToRename = list)
    }

    /**
     * Dismisses the rename dialog.
     */
    fun dismissRenameDialog() {
        uiState = uiState.copy(listToRename = null)
    }

    /**
     * Shows the delete dialog.
     */
    fun showDeleteDialog(list: ItemList) {
        uiState = uiState.copy(listToDelete = list)
    }

    /**
     * Dismisses the delete dialog.
     */
    fun dismissDeleteDialog() {
        uiState = uiState.copy(listToDelete = null)
    }

    /**
     * Shows the create dialog.
     */
    fun showCreateDialog() {
        uiState = uiState.copy(creating = true)
    }

    /**
     * Dismisses the create dialog.
     */
    fun dismissCreateDialog() {
        uiState = uiState.copy(creating = false)
    }

    /**
     * Dismisses action error.
     */
    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}