package com.julia.iwatch.list

data class ListsUiState(
    val itemLists: List<ItemList>? = null,
    val listToRename: ItemList? = null,
    val listToDelete: ItemList? = null,
    val showError: Boolean = false,
    val loading: Boolean = false,
    val actionError: Boolean = false,
    val creating: Boolean = false
)