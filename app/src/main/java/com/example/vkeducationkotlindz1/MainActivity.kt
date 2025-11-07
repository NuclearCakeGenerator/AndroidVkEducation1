package com.example.vkeducationkotlindz1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vkeducationkotlindz1.ui.theme.VkEducationKotlinDz1Theme
import kotlinx.serialization.builtins.serializer
import java.util.Collections.list

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    val count = 100
    val columnsCount = 3
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnsCount),
        modifier = Modifier.fillMaxSize(),
    ) {

        items(
            count = count,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(size = 80.dp)
                    .padding(5.dp)
                    .background(color = if (it % 2 == 0) {Color.Red} else {Color.Blue}),

                ) {
                Text(
                    text = "$it",
                    fontSize = 20.sp,
                    color = Color.Green,
                )
            }
        }
    }
}

data class MyCell(
    val text: String,
    val id: Int
)

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}