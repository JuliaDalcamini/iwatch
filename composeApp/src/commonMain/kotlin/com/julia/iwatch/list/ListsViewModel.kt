package com.julia.iwatch.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ListsViewModel(
    private val repository: ListsRepository = ListsRepository()
): ViewModel() {

    var uiState by mutableStateOf(ListsUiState())
        private set

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

    fun showRenameDialog(list: ItemList) {
        uiState = uiState.copy(listToRename = list)
    }

    fun dismissRenameDialog() {
        uiState = uiState.copy(listToRename = null)
    }

    fun showDeleteDialog(list: ItemList) {
        uiState = uiState.copy(listToDelete = list)
    }

    fun dismissDeleteDialog() {
        uiState = uiState.copy(listToDelete = null)
    }

    fun showCreateDialog() {
        uiState = uiState.copy(creating = true)
    }

    fun dismissCreateDialog() {
        uiState = uiState.copy(creating = false)
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}