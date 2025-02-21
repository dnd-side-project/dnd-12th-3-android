package com.dnd.safety.presentation.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FadeAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        content = content,
        modifier = modifier
    )
}

@Composable
fun BottomToTopAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300)
        ),
        modifier = modifier,
        content = content,
    )
}

@Composable
fun TopToBottomAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight }, // 화면 상단에서 시작
            animationSpec = tween(100)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight }, // 화면 상단으로 사라짐
            animationSpec = tween()
        ),
        modifier = modifier,
        content = content,
    )
}
