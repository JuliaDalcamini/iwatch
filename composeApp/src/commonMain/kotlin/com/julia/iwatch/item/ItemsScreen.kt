package com.julia.iwatch.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.julia.iwatch.common.ui.dialog.ConfirmationDialog
import com.julia.iwatch.common.ui.dialog.ErrorDialog
import com.julia.iwatch.common.ui.error.LoadingError
import com.julia.iwatch.common.ui.topbar.TopBar
import com.julia.iwatch.list.ItemList
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.action_error_message
import iwatch.composeapp.generated.resources.action_error_title
import iwatch.composeapp.generated.resources.add_24px
import iwatch.composeapp.generated.resources.add_movie_label
import iwatch.composeapp.generated.resources.delete_24px
import iwatch.composeapp.generated.resources.delete_item_message
import iwatch.composeapp.generated.resources.delete_item_title
import iwatch.composeapp.generated.resources.delete_label
import iwatch.composeapp.generated.resources.list_items_title
import iwatch.composeapp.generated.resources.no_items_in_list_message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ItemsScreen(
    list: ItemList,
    onAddClick: () -> Unit,
    onBackRequest: () -> Unit,
    viewModel: ItemsViewModel = viewModel { ItemsViewModel() }
) {
    LaunchedEffect(list) {
        viewModel.getItems(listId = list.id)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.list_items_title),
                subtitle = list.name,
                onBackClick = onBackRequest
            )
        },
        floatingActionButton = {
            NewItemButton(onClick = onAddClick)
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
                    onRetryClick = { viewModel.getItems(listId = list.id) }
                )

                viewModel.uiState.items.isNullOrEmpty() -> Text(
                    modifier = Modifier.padding(24.dp).align(Alignment.Center),
                    text = stringResource(Res.string.no_items_in_list_message),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                else -> ItemsScreenContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues),
                    entries = viewModel.uiState.items,
                    onToggleItemWatched = { item, watched -> viewModel.setWatched(item, watched) },
                    onDeleteItemClick = { viewModel.showDeleteDialog(it) },
                    contentPadding = paddingValues
                )
            }
        }
    }

    viewModel.uiState.itemToDelete?.let { item ->
        DeleteItemDialog(
            name = item.name,
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            onConfirm = { viewModel.delete(item) }
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
private fun DeleteItemDialog(
    name: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = stringResource(Res.string.delete_item_title),
        message = stringResource(Res.string.delete_item_message, name),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun ItemsScreenContent(
    entries: List<Item>?,
    onToggleItemWatched: (Item, Boolean) -> Unit,
    onDeleteItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        entries?.sortedBy { it.watched }?.let { sortedEntries ->
            items(
                items = sortedEntries,
                key = { item -> item.id }
            ) { item ->
                ItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateItem(),
                    item = item,
                    onToggleWatched = { onToggleItemWatched(item, it) },
                    onDeleteClick = { onDeleteItemClick(item) }
                )
            }
        }

        item {
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    onToggleWatched: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val opacity = if (item.watched) 0.8f else 1f

    ElevatedCard(
        modifier = modifier.graphicsLayer(alpha = opacity)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .graphicsLayer(alpha = opacity),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = item.watched,
                onCheckedChange = onToggleWatched
            )

            AsyncImage(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .graphicsLayer(alpha = opacity),
                model = item.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(Modifier.weight(1f)) {
                if (item.watched) {
                    Text(
                        text = "Assistido",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                            .graphicsLayer(alpha = opacity)
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(alpha = opacity),
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                item.year?.let { year ->
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .graphicsLayer(alpha = opacity),
                        text = year,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.graphicsLayer(alpha = opacity)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete_24px),
                    contentDescription = stringResource(Res.string.delete_label),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Composable
fun NewItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(stringResource(Res.string.add_movie_label)) },
        icon = { Icon(vectorResource(Res.drawable.add_24px), null) },
        onClick = onClick
    )
}