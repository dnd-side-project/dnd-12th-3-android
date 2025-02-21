/**
 * https://stackoverflow.com/questions/74041429/how-to-set-half-expanded-height-for-bottomsheet-using-bottomsheetscaffold-in-com
 * */

package com.dnd.safety.presentation.ui.home.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.Gray30
import com.dnd.safety.presentation.designsystem.theme.Gray80
import kotlinx.coroutines.launch

enum class ExpandedType {
    HALF, FULL, COLLAPSED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheetScaffold(
    modifier: Modifier = Modifier,
    onExpandTypeChanged: (ExpandedType) -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val marginTop = 75
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val bottomSheetSt = rememberStandardBottomSheetState(
        skipHiddenState = true,
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetSt)
    var peekHeight: Int by remember { mutableIntStateOf(0) }

    val cornerValue by animateDpAsState(
        targetValue = if (bottomSheetSt.currentValue == SheetValue.Expanded) 0.dp else 16.dp,
        animationSpec = tween(durationMillis = 300)
    )


    LaunchedEffect(bottomSheetSt.currentValue) {
        if (bottomSheetSt.currentValue == SheetValue.Expanded) {
            // 바텀시트가 완전히 펼쳐졌을 때 처리할 로직
        }
    }

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(topStart = cornerValue, topEnd = cornerValue),
        scaffoldState = scaffoldState,
        sheetContent = {
            val scope = rememberCoroutineScope()
            BottomSheetGestureWrapper(
                onExpandTypeChanged = {
                    onExpandTypeChanged(it)
                    scope.launch {
                        peekHeight = when (it) {
                            ExpandedType.COLLAPSED -> 84
                            ExpandedType.FULL -> screenHeight - marginTop
                            ExpandedType.HALF -> screenHeight / 2 - marginTop
                        }
                        bottomSheetSt.partialExpand()
                    }
                },
                modifier = modifier
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    DragHandle(color = Gray30)
                    sheetContent()
                }
            }
        },
        sheetPeekHeight = peekHeight.dp,
        modifier = modifier.fillMaxSize(),
        sheetShadowElevation = 2.dp,
        sheetContainerColor = Gray80,
        sheetDragHandle = null,
        content = content
    )
}


@Composable
private fun BottomSheetGestureWrapper(
    modifier: Modifier = Modifier,
    onExpandTypeChanged: (ExpandedType) -> Unit,
    content: @Composable () -> Unit
) {
    var expandedType by remember {
        mutableStateOf(ExpandedType.HALF)
    }

    var isUpdated = false

    LaunchedEffect(key1 = expandedType) {
        onExpandTypeChanged(expandedType)
    }

    Box(
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        if (!isUpdated) {
                            expandedType = when {
                                dragAmount < 0 && expandedType == ExpandedType.COLLAPSED -> {
                                    ExpandedType.HALF
                                }

                                dragAmount < 0 && expandedType == ExpandedType.HALF -> {
                                    ExpandedType.FULL
                                }

                                dragAmount > 0 && expandedType == ExpandedType.FULL -> {
                                    ExpandedType.HALF
                                }

                                dragAmount > 0 && expandedType == ExpandedType.HALF -> {
                                    ExpandedType.COLLAPSED
                                }

                                else -> {
                                    expandedType
                                }
                            }
                            isUpdated = true
                        }
                    },
                    onDragEnd = {
                        isUpdated = false
                    }
                )
            }
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        content()
    }
}