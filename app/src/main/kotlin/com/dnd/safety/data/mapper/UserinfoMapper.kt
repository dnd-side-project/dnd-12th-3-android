package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.LoginResponse
import com.dnd.safety.domain.model.UserInfo

fun LoginResponse.toUserInfo() = UserInfo(
    token = accessToken,
    name = name,
)