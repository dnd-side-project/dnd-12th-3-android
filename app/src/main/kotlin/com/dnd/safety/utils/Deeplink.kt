package com.dnd.safety.utils

object Deeplink {

    private const val DEEPLINK_ADDRESS = "watchout://watchout.com/"

    fun deeplink(url: String) = "$DEEPLINK_ADDRESS$url"

    fun deeplinkWithArgument(url: String, vararg arguments: String): String {
        val argumentPath = arguments.joinToString("/") { "{$it}" }
        return "$DEEPLINK_ADDRESS$url/$argumentPath"
    }
}