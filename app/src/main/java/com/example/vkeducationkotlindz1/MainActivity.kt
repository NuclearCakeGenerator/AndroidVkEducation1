package com.example.vkeducationkotlindz1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times

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
    val initialBoxesCount: Int = integerResource(R.integer.initial_cells_count)
    var boxesCount by rememberSaveable { mutableIntStateOf(value = initialBoxesCount) }
    val listSidePadding: Dp = dimensionResource(R.dimen.list_border_padding)
    val listInternalPadding: Dp = dimensionResource(R.dimen.inter_boxes_padding)
    val actionButtonPadding: Dp = dimensionResource(R.dimen.fab_padding)
    val actionButtonSize: Dp = dimensionResource(R.dimen.fab_size)
    val screenPosition = LocalConfiguration.current.orientation
    val columnsCount = if (screenPosition == Configuration.ORIENTATION_LANDSCAPE)
        integerResource(R.integer.horizontal_orientation_columns_count)
    else
        integerResource(R.integer.vertical_orientation_columns_count)



    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = columnsCount),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = listSidePadding,
                start = listSidePadding,
                end = listSidePadding,
                bottom = 2 * actionButtonPadding + actionButtonSize,
            ),
            verticalArrangement = Arrangement.spacedBy(space = listInternalPadding),
            horizontalArrangement = Arrangement.spacedBy(space = listInternalPadding),
        ) {

            items(
                count = boxesCount,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(percent = integerResource(R.integer.box_round_percentage)))
                        .background(
                            color = if (it % 2 == 0)
                                colorResource(R.color.odd_color)
                            else
                                colorResource(R.color.even_color)

                        )
                        ,

                    ) {
                    Text(
                        text = "$it",
                        fontSize = integerResource(R.integer.inbox_text_size).sp,
                        color = colorResource(R.color.inbox_text_color),
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { boxesCount++ },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(all = actionButtonPadding)
                .size(size = actionButtonSize)
        ) {
            Text(
                text = "+",
                fontSize = integerResource(R.integer.fab_text_size).sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}