package com.example.vkeducationkotlindz1

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screenStatus = rememberSaveable { mutableStateOf<Int?>(null) }
            val initialBoxesCount: Int = integerResource(R.integer.initial_cells_count)
            var boxesCount = rememberSaveable { mutableIntStateOf(value = initialBoxesCount) }

            when (screenStatus.value) {
                null -> {
                    MainScreen(screenStatus, boxesCount)
                }

                else -> {
                    ExtraScreen(screenStatus)
                }

            }


        }
    }
}

@Composable
fun ExtraScreen(
    screenStatus: MutableState<Int?>,
) {

    Box(
        modifier = Modifier.fillMaxSize()

    )
    {
        Text(
            text = "Text ${screenStatus.value}", fontSize = integerResource(R.integer.extra_screen_text_size).sp,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = { screenStatus.value = null },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(dimensionResource(R.dimen.close_button_padding)),

            ) {
            Text(
                text = stringResource(R.string.close_button_text)
            )
        }
    }
}

@Composable
fun MainScreen(
    screenStatus: MutableState<Int?>,
    boxesCount: MutableState<Int>,
) {

    val listSidePadding: Dp = dimensionResource(R.dimen.list_border_padding)
    val listInternalPadding: Dp = dimensionResource(R.dimen.inter_boxes_padding)
    val actionButtonPadding: Dp = dimensionResource(R.dimen.fab_padding)
    val actionButtonSize: Dp = dimensionResource(R.dimen.fab_size)
    val screenPosition = LocalConfiguration.current.orientation
    val columnsCount = if (screenPosition == Configuration.ORIENTATION_LANDSCAPE)
        integerResource(R.integer.horizontal_orientation_columns_count)
    else
        integerResource(R.integer.vertical_orientation_columns_count)
    val context = LocalConfiguration.current


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
            items(count = boxesCount.value) {
                Box(

                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(percent = integerResource(R.integer.box_round_percentage)))
                        .background(
                            color = if ((it + 1) % 2 == 1)
                                colorResource(R.color.odd_color)
                            else
                                colorResource(R.color.even_color)
                        )
                        .clickable(
                            onClick = {
                                screenStatus.value = it + 1
                            },
                        )
                ) {
                    Text(
                        text = "${it + 1}",
                        fontSize = integerResource(R.integer.inbox_text_size).sp,
                        color = colorResource(R.color.inbox_text_color),
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { boxesCount.value++ },
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


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MainScreenLightPreview() {
    MainScreen(
        mutableStateOf<Int?>(null),
        mutableIntStateOf(integerResource(R.integer.initial_cells_count))
    )
}

//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun MainScreenDarkPreview() {
//    MainScreen()
//}
