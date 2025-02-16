package com.dnd.safety.presentation.navigation.utils

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute

/**
 * [SavedStateHandle]을 사용하여 ViewModel에서 매개변수를 전달할 수 있도록 합니다.
 *
 * ```
 * @HiltViewModel
 * class MainViewModel @Inject constructor(
 *    savedStateHandle: SavedStateHandle
 * ) : ViewModel() {
 *
 *   private val idData: IdData = savedStateHandle.toRouteType<Route.Home, IdData>()
 *
 *   }
 * ```
 */
inline fun <reified T : Any, reified Type : Any> SavedStateHandle.toRouteType(
): T = toRoute<T>(
    typeMap = createTypeMap(Type::class)
)