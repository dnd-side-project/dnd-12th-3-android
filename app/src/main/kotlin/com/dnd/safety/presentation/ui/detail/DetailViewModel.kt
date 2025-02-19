package com.dnd.safety.presentation.ui.detail

import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.repository.CommentRepository
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.toRouteType
import com.dnd.safety.presentation.ui.detail.effect.DetailModalEffect
import com.dnd.safety.presentation.ui.detail.effect.DetailUiEffect
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val incidentId = savedStateHandle.toRouteType<Route.IncidentDetail, Incident>().incident.id

    private var cursor = mutableLongStateOf(0L)

    private val _commentState = MutableStateFlow<List<Comment>>(emptyList())
    val commentState: TriggerStateFlow<List<Comment>> = _commentState
        .onStart { getComments() }
        .triggerStateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = emptyList()
        )

    private val _detailModalState = MutableStateFlow<DetailModalEffect>(DetailModalEffect.Hidden)
    val detailModalState: StateFlow<DetailModalEffect> get() = _detailModalState

    private val _detailUiEffect = MutableSharedFlow<DetailUiEffect>()
    val detailUiEffect: SharedFlow<DetailUiEffect> get() = _detailUiEffect

    private fun getComments() {
        viewModelScope.launch {
            commentRepository.getComments(
                incidentId = incidentId,
                cursor = cursor.longValue
            ).onSuccess {
                cursor.longValue = data.nextCursor

                _commentState.update {
                    data.comments
                }
            }.onFailure {

            }
        }
    }

    private fun resetComment() {
        cursor.longValue = 0L
        commentState.restart()
    }

    fun writeComment(comment: String) {
        viewModelScope.launch {
            commentRepository.writeComment(
                incidentId = incidentId,
                comment = comment
            ).onSuccess {
                resetComment()
            }.onFailure {
                showSnackBar("댓글 작성에 실패했습니다")
            }
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(
                incidentId = incidentId,
                commentId = commentId
            ).onSuccess {
                resetComment()
                showSnackBar("댓글이 삭제되었습니다")
            }.onFailure {
                showSnackBar("댓글 삭제에 실패했습니다")
            }
        }
    }

    fun editComment(commentId: Long, comment: String) {
        viewModelScope.launch {
            commentRepository.editComment(
                incidentId = incidentId,
                commentId = commentId,
                comment = comment
            ).onSuccess {
                resetComment()
                showSnackBar("댓글이 수정되었습니다")
            }.onFailure {
                showSnackBar("댓글 수정에 실패했습니다")
            }
        }
    }

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            _detailUiEffect.emit(DetailUiEffect.ShowSnackBar(message))
        }
    }

    fun showCommentActionMenu(comment: Comment) {
        viewModelScope.launch {
            _detailModalState.update { DetailModalEffect.ShowCommentActionMenu(comment) }
        }
    }

    fun dismiss() {
        _detailModalState.update { DetailModalEffect.Hidden }
    }
}