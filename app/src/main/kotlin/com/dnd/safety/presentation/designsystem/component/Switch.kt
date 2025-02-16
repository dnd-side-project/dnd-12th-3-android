package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dnd.safety.presentation.designsystem.theme.Gray30
import com.dnd.safety.presentation.designsystem.theme.Main
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Switch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    checkedColor: Color = Main,
    uncheckedColor: Color = Gray30,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Switch(
            checked = checked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                uncheckedThumbColor = White,
                checkedTrackColor = checkedColor,
                uncheckedTrackColor = uncheckedColor,
            ),
            thumbContent = {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = White,
                        radius = 26f,
                        center = center
                    )
                }
            },
            onCheckedChange = onCheckedChange,
            modifier = modifier
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun SwitchPreview(
) {
    SafetyTheme {
        Column {
            Switch(
                checked = true
            ) {

            }
        }
    }
}
