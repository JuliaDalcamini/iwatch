package com.julia.iwatch.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.julia.iwatch.common.ui.dialog.ErrorDialog
import com.julia.iwatch.common.ui.error.LoadingError
import com.julia.iwatch.common.ui.padding.plus
import com.julia.iwatch.list.ItemList
import com.julia.iwatch.tmdb.TMDB_IMAGE_BASE_URL
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.action_error_message
import iwatch.composeapp.generated.resources.action_error_title
import iwatch.composeapp.generated.resources.arrow_back_24px
import iwatch.composeapp.generated.resources.search_24px
import iwatch.composeapp.generated.resources.search_movies_label
import iwatch.composeapp.generated.resources.search_movies_message
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    list: ItemList,
    onAddedToList: () -> Unit,
    onBackRequest: () -> Unit,
    viewModel: SearchViewModel = viewModel { SearchViewModel() }
) {
    LaunchedEffect(list) {
        viewModel.initialize(listId = list.id)
    }

    LaunchedEffect(viewModel.uiState.addedToList) {
        if (viewModel.uiState.addedToList) {
            onAddedToList()
        }
    }

    Box(Modifier.fillMaxSize()) {
        var expanded by remember { mutableStateOf(false) }
        val contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp)
        val insets = WindowInsets.safeDrawing.asPaddingValues()

        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = viewModel.uiState.query,
                    onQueryChange = {
                        viewModel.setQuery(it)
                        viewModel.fetchSuggestions()
                    },
                    onSearch = {
                        viewModel.setQuery(it)
                        viewModel.search()
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    enabled = !viewModel.uiState.adding,
                    placeholder = { Text(stringResource(Res.string.search_movies_label)) },
                    leadingIcon = {
                        IconButton(onClick = onBackRequest) {
                            Icon(painterResource(Res.drawable.arrow_back_24px), null)
                        }
                    },
                    trailingIcon = {
                        Icon(painterResource(Res.drawable.search_24px), null)
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            LazyColumn {
                items(viewModel.uiState.suggestions) { suggestion ->
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                viewModel.setQuery(suggestion)
                                viewModel.search()
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        headlineContent = { Text(suggestion) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    )
                }
            }
        }

        when {
            viewModel.uiState.loading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(insets)
                    .padding(contentPadding)
            )

            viewModel.uiState.showError -> LoadingError(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(insets)
                    .padding(insets)
                    .padding(contentPadding)
                    .padding(24.dp),
                onRetryClick = { viewModel.search() }
            )

            viewModel.uiState.results.isNullOrEmpty() -> Text(
                modifier = Modifier.padding(24.dp).align(Alignment.Center),
                text = stringResource(Res.string.search_movies_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            else -> SearchScreenResults(
                results = viewModel.uiState.results.orEmpty(),
                onResultClick = { viewModel.addToList(it) },
                enabled = !viewModel.uiState.adding,
                contentPadding = insets + contentPadding
            )
        }
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
fun SearchScreenResults(
    results: List<SearchResult>,
    onResultClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = results,
            key = { item -> item.id }
        ) { movie ->
            MovieCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                movie = movie,
                onClick = { onResultClick(movie) },
                enabled = enabled
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: SearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                model = movie.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val releaseYear = try {
                    movie.releaseDate?.let { LocalDate.parse(it).year.toString() }
                } catch (error: Throwable) {
                    null
                }

                releaseYear?.let { year ->
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = year,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}