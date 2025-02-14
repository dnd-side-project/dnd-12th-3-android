package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import com.dnd.safety.presentation.designsystem.theme.Gray10

fun Modifier.circleBackground(
    color: Color = Gray10,
): Modifier {
    return this
        .background(color, CircleShape)
        .circleLayout()
}

private fun Modifier.circleLayout() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val currentHeight = placeable.height
        val currentWidth = placeable.width
        val newDiameter = maxOf(currentHeight, currentWidth)

        layout(newDiameter, newDiameter) {
            placeable.placeRelative((newDiameter-currentWidth)/2, (newDiameter-currentHeight)/2)
        }
    }