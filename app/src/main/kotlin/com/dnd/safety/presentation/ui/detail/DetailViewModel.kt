package com.dnd.safety.presentation.ui.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dnd.safety.data.location.LocationService
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.repository.CommentRepository
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.domain.repository.LikeRepository
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.utils.toRouteType
import com.dnd.safety.presentation.ui.detail.effect.DetailModalEffect
import com.dnd.safety.presentation.ui.detail.effect.DetailUiEffect
import com.dnd.safety.presentation.ui.detail.state.DetailUiState
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val likeRepository: LikeRepository,
    private val incidentRepository: IncidentRepository,
    locationService: LocationService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val myLocation = locationService.requestLocationUpdates()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val incidentId = savedStateHandle.toRoute<Route.IncidentDetail>().incidentId

    private val _incident = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val incident: StateFlow<DetailUiState> = _incident.onStart {
        getIncident()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000L),
        initialValue = DetailUiState.Loading
    )

    private var cursor = mutableStateOf<Long?>(null)

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

    var commentMode = MutableStateFlow<CommentMode>(
        CommentMode.Text("")
    )
        private set

    private fun getIncident() {
        viewModelScope.launch {
            myLocation.collectLatest {
                if (it != null) {
                    incidentRepository
                        .getIncidentData(
                            incidentId,
                            it
                        )
                        .onSuccess {
                            _incident.update {
                                DetailUiState.IncidentDetail(data)
                            }
                            closeCommentEditMode()
                        }
                        .onFailure {
                            showSnackBar("네트워크 오류가 발생했습니다")
                            navigateBack()
                        }
                    cancel()
                }
            }
        }
    }

    private fun getComments() {
        viewModelScope.launch {
            commentRepository.getComments(
                incidentId = incidentId,
                cursor = cursor.value
            ).onSuccess {
                cursor.value = data.nextCursor

                _commentState.update {
                    data.comments
                }
            }.onFailure {
                showSnackBar("댓글을 불러오는데 실패했습니다")
            }
        }
    }

    private fun resetComment() {
        cursor.value = 0L
        commentState.restart()
        hideKeyboard()
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

        dismiss()
    }

    fun editComment(commentId: Long, comment: String) {
        closeCommentEditMode()
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

    fun deleteIncident() {
        viewModelScope.launch {
            incidentRepository.deleteIncident(incidentId)
                .suspendOnSuccess {
                    _detailUiEffect.emit(DetailUiEffect.NavigateToBack)
                }
                .onFailure {
                    showSnackBar("삭제에 실패했습니다")
                }
        }
    }

    fun likeIncident() {
        viewModelScope.launch {
            likeRepository.toggleLike(incidentId)

            _incident.update {
                when (it) {
                    is DetailUiState.Loading -> it
                    is DetailUiState.IncidentDetail -> it.copy(
                        incident = it.incident.copy(
                            liked = !it.incident.liked,
                            likeCount = it.incident.likeCount + if (it.incident.liked) -1 else 1
                        )
                    )
                }
            }
        }
    }


    fun showSnackBar(message: String) {
        viewModelScope.launch {
            _detailUiEffect.emit(DetailUiEffect.ShowSnackBar(message))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _detailUiEffect.emit(DetailUiEffect.NavigateToBack)
        }
    }

    fun showCommentActionMenu(comment: Comment) {
        viewModelScope.launch {
            _detailModalState.update { DetailModalEffect.ShowCommentActionMenu(comment) }
        }
    }

    fun showIncidentsActionMenu() {
        viewModelScope.launch {
            _detailModalState.update { DetailModalEffect.ShowIncidentsActionMenu }
        }
    }

    fun showCommentEditMode(comment: Comment) {
        dismiss()
        commentMode.value = CommentMode.Edit(comment.comment, comment.commentId)
    }

    fun showIncidentEditScreen() {
        dismiss()

        val incident = (incident.value as? DetailUiState.IncidentDetail)?.incident ?: return

        viewModelScope.launch {
            _detailUiEffect.emit(DetailUiEffect.NavigateToIncidentEdit(incident))
        }
    }

    fun showDeleteCheckDialog() {
        _detailModalState.update { DetailModalEffect.ShowDeleteCheckDialog }
    }

    fun closeCommentEditMode() {
        val incident = (incident.value as? DetailUiState.IncidentDetail)?.incident ?: return

        commentMode.value = CommentMode.Text(incident.userName)
    }

    fun hideKeyboard() {
        viewModelScope.launch {
            _detailUiEffect.emit(DetailUiEffect.HideKeyboard)
        }
    }

    fun dismiss() {
        _detailModalState.update { DetailModalEffect.Hidden }
    }
}