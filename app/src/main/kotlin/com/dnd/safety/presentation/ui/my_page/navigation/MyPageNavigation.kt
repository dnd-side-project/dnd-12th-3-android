package com.dnd.safety.presentation.ui.my_page.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.ui.my_page.MyPageRoute

fun NavController.navigateToMyPage(navOptions: NavOptions) {
    navigate(MainTabRoute.MyPage, navOptions)
}

fun NavGraphBuilder.myPageNavGraph(
    onGoBack: () -> Unit,
) {
    composable<MainTabRoute.MyPage> {
        MyPageRoute(
            onGoBack = onGoBack,
        )
    }
}