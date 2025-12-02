package com.example.vkeducationkotlindz1 // must match manifest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
//import com.example.giphyapp.GifObject


data class GiphyResponse(val data: List<GifObject>)
data class GifObject(val images: GifImages)
data class GifImages(val downsized: GifData)
data class GifData(val url: String)

interface GiphyApi {
    @GET("v1/gifs/trending")
    suspend fun loadTrending(
        @Query("api_key") key: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GiphyResponse
}

private fun createApi(): GiphyApi = Retrofit.Builder().baseUrl("https://api.giphy.com/")
    .addConverterFactory(GsonConverterFactory.create()).build().create(GiphyApi::class.java)

class GiphyViewModel : ViewModel() {

    private val api = createApi()

    var items by mutableStateOf<List<GifObject>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isLoadingMore by mutableStateOf(false)
    var error by mutableStateOf(false)
    private var offset = 0

    private val apiKey = "tTf1DVyeWzeb4YbVSY4tqL29kpYHhjbM"

    fun loadInitial() {
        if (items.isNotEmpty()) return // survive rotation

        isLoading = true
        error = false

        kotlinx.coroutines.GlobalScope.launch {
            try {
                val response = api.loadTrending(apiKey)
                items = response.data
                offset = items.size
            } catch (e: Exception) {
                error = true
            }
            isLoading = false
        }
    }

    fun loadMore() {
        if (isLoadingMore) return

        isLoadingMore = true

        kotlinx.coroutines.GlobalScope.launch {
            try {
                val response = api.loadTrending(apiKey, offset = offset)
                items = items + response.data
                offset = items.size
            } catch (_: Exception) {
            }
            isLoadingMore = false
        }
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppScreen() } }
    }
}


@Composable
fun AppScreen(vm: GiphyViewModel = viewModel()) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { vm.loadInitial() }

    when {
        vm.isLoading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        vm.error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error loading data")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { vm.loadInitial() }) {
                        Text("Retry")
                    }
                }
            }
        }

        else -> {
            GifGrid(
                items = vm.items, onClick = { index ->
                    Toast.makeText(context, "GIF #${index + 1}", Toast.LENGTH_SHORT).show()
                }, onLoadMore = { vm.loadMore() }, isLoadingMore = vm.isLoadingMore
            )
        }
    }
}

@Composable
fun GifGrid(
    items: List<GifObject>,
    onClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    columns: Int = 3 // default 2 columns
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(items) { index, item ->

            AsyncImage(
                model = item.images.downsized.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // makes cells square
                    .clickable { onClick(index) }
            )

            // trigger pagination 5 items before the end
            if (index == items.lastIndex - 5) {
                onLoadMore()
            }
        }

        // bottom loading indicator spanning all columns
        if (isLoadingMore) {
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