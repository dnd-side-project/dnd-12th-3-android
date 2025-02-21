package com.dnd.safety.domain.usecase

import com.dnd.safety.domain.repository.LoginRepository
import com.dnd.safety.domain.repository.UserInfoRepository
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import javax.inject.Inject

class SendTokenUsecase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userInfoRepository: UserInfoRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        loginRepository
            .sendToken(
                name = name,
                email = email
            )
            .suspendOnSuccess {
                userInfoRepository.setUserInfo(
                    token = data.token,
                    name = data.name
                )
                onSuccess()
            }
            .suspendOnFailure {
                onError("로그인에 실패했습니다")
            }
    }
}