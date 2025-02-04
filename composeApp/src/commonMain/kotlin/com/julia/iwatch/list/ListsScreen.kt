package com.julia.iwatch.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.iwatch.common.ui.dialog.ConfirmationDialog
import com.julia.iwatch.common.ui.dialog.ErrorDialog
import com.julia.iwatch.common.ui.dialog.TextInputDialog
import com.julia.iwatch.common.ui.error.LoadingError
import com.julia.iwatch.common.ui.topbar.TopBar
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.action_error_message
import iwatch.composeapp.generated.resources.action_error_title
import iwatch.composeapp.generated.resources.add_24px
import iwatch.composeapp.generated.resources.create_list_title
import iwatch.composeapp.generated.resources.create_new_list_label
import iwatch.composeapp.generated.resources.delete_24px
import iwatch.composeapp.generated.resources.delete_label
import iwatch.composeapp.generated.resources.delete_list_message
import iwatch.composeapp.generated.resources.delete_list_title
import iwatch.composeapp.generated.resources.edit_24px
import iwatch.composeapp.generated.resources.item_count_format
import iwatch.composeapp.generated.resources.logout_24px
import iwatch.composeapp.generated.resources.logout_label
import iwatch.composeapp.generated.resources.more_options_label
import iwatch.composeapp.generated.resources.more_vert_24px
import iwatch.composeapp.generated.resources.my_lists_title
import iwatch.composeapp.generated.resources.no_lists_message
import iwatch.composeapp.generated.resources.rename_label
import iwatch.composeapp.generated.resources.rename_list_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ListsScreen(
    onItemListClick: (ItemList) -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ListsViewModel = viewModel { ListsViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.getLists()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.my_lists_title),
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(painterResource(Res.drawable.logout_24px), stringResource(Res.string.logout_label))
                    }
                }
            )
        },
        floatingActionButton = {
            NewItemListButton(onClick = { viewModel.showCreateDialog() })
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            when {
                viewModel.uiState.loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingValues)
                )

                viewModel.uiState.showError -> LoadingError(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues)
                        .padding(paddingValues)
                        .padding(24.dp),
                    onRetryClick = { viewModel.getLists() }
                )

                viewModel.uiState.itemLists.isNullOrEmpty() -> Text(
                    modifier = Modifier.padding(24.dp).align(Alignment.Center),
                    text = stringResource(Res.string.no_lists_message),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                else -> ListsScreenContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues),
                    entries = viewModel.uiState.itemLists,
                    onItemListClick = onItemListClick,
                    onRenameListClick = { viewModel.showRenameDialog(it) },
                    onDeleteListClick = { viewModel.showDeleteDialog(it) },
                    contentPadding = paddingValues
                )
            }
        }
    }

    viewModel.uiState.listToRename?.let { list ->
        RenameListDialog(
            name = list.name,
            onDismissRequest = { viewModel.dismissRenameDialog() },
            onConfirm = { newName -> viewModel.rename(list, newName) }
        )
    }

    viewModel.uiState.listToDelete?.let { list ->
        DeleteListDialog(
            name = list.name,
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            onConfirm = { viewModel.delete(list) }
        )
    }

    if (viewModel.uiState.creating) {
        CreateListDialog(
            onDismissRequest = { viewModel.dismissCreateDialog() },
            onConfirm = { viewModel.create(it) }
        )
    }

    if (viewModel.uiState.actionError) {
        ErrorDialog(
            title = stringResource(Res.string.action_error_title),
            message = stringResource(Res.string.action_error_message),
            onDismissRequest = { viewModel.dismissActionError() }
        )
    }
}

@Composable
fun RenameListDialog(
    name: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.rename_list_title),
        initialValue = name,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun CreateListDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.create_list_title),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
private fun DeleteListDialog(
    name: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = stringResource(Res.string.delete_list_title),
        message = stringResource(Res.string.delete_list_message, name),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun ListsScreenContent(
    entries: List<ItemList>?,
    onItemListClick: (ItemList) -> Unit,
    onRenameListClick: (ItemList) -> Unit,
    onDeleteListClick: (ItemList) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        entries?.let {
            items(
                items = entries,
                key = { list -> list.id }
            ) { list ->
                ItemList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateItem(),
                    list = list,
                    onClick = { onItemListClick(list) },
                    onRenameClick = { onRenameListClick(list) },
                    onDeleteClick = { onDeleteListClick(list) }
                )
            }
        }

        item {
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun ItemList(
    list: ItemList,
    onClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        BoxWithConstraints {
            val compact = maxWidth < 480.dp

            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = list.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(Res.string.item_count_format, list.itemCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                ListOptions(
                    compact = compact,
                    onRenameClick = onRenameClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
private fun ListOptions(
    compact: Boolean,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        var expanded by remember { mutableStateOf(false) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (compact) {
                IconButton(onClick = { expanded = true }) {
                    Icon(vectorResource(Res.drawable.more_vert_24px), stringResource(Res.string.more_options_label))
                }
            } else {
                IconButton(onClick = onRenameClick) {
                    Icon(vectorResource(Res.drawable.edit_24px), stringResource(Res.string.rename_label))
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(vectorResource(Res.drawable.delete_24px), stringResource(Res.string.delete_label))
                }
            }
        }

        if (compact) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.rename_label)) },
                    leadingIcon = { Icon(vectorResource(Res.drawable.edit_24px), null) },
                    onClick = {
                        onRenameClick()
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.delete_label)) },
                    leadingIcon = { Icon(vectorResource(Res.drawable.delete_24px), null) },
                    onClick = {
                        onDeleteClick()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun NewItemListButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(stringResource(Res.string.create_new_list_label)) },
        icon = { Icon(vectorResource(Res.drawable.add_24px), null) },
        onClick = onClick
    )
}