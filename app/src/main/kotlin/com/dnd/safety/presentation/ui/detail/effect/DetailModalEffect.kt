package com.dnd.safety.presentation.ui.detail.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.Comment

@Stable
sealed interface DetailModalEffect {

    @Immutable
    data object Hidden : DetailModalEffect

    @Immutable
    data class ShowCommentActionMenu(val comment: Comment) : DetailModalEffect

    @Immutable
    data object ShowIncidentsActionMenu : DetailModalEffect

    @Immutable
    data object ShowDeleteCheckDialog : DetailModalEffect
}