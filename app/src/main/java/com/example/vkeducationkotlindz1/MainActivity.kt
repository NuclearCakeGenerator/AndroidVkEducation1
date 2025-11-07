package com.example.vkeducationkotlindz1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
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
    var count by rememberSaveable { mutableIntStateOf(5) }
    val screenPosition = LocalConfiguration.current.orientation
    val columnsCount = if (screenPosition == Configuration.ORIENTATION_LANDSCAPE) {
        4
    } else {
        3
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = columnsCount),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
        ) {

            items(
                count = count,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
//                        .padding(5.dp)
                        .background(
                            color = if (it % 2 == 0) {
                                Color.Red
                            } else {
                                Color.Blue
                            }
                        ),

                    ) {
                    Text(
                        text = "$it",
                        fontSize = 30.sp,
                        color = Color.Green,
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { count++ },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(30.dp)
        ) {
            Text(
                text = "+",
                fontSize = 50.sp,
            )
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