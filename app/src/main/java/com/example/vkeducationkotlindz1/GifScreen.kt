package com.example.vkeducationkotlindz1

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun GifScreen(vm: GiphyViewModel = viewModel()) {
    val uiState by vm.uiState
    val context = LocalContext.current

    LaunchedEffect(Unit) { vm.loadInitial() }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        uiState.error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.error_message))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { vm.loadInitial() }) {
                    Text(stringResource(R.string.retry_message))
                }
            }
        }
        else -> GifGrid(
            uiState = uiState,
            onClick = { index ->
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_gif_number, index + 1),
                    Toast.LENGTH_SHORT
                ).show()
            },
            onLoadMore = { vm.loadMore() }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifGrid(
    uiState: GifUiState,
    onClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    columns: Int = 3
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .distinctUntilChanged()
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                if (lastVisible >= totalItems - 6) onLoadMore()
            }
    }

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(6.dp)
    ) {
        itemsIndexed(uiState.items) { index, item ->
            AsyncImage(
                model = item.images.downsized.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable { onClick(index) }
            )
        }

        if (uiState.isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }
    }
}
