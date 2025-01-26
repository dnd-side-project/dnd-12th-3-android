package com.dnd.safety.presentation.navigation.utils

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T : Any> customNavType(): NavType<T> {
    return object : NavType<T>(true) {
        override fun get(bundle: Bundle, key: String): T? {
            val json = bundle.getString(key)
            return json?.let {
                Json.decodeFromString(serializer(), URLDecoder.decode(it, "UTF-8"))
            }
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(serializer(), URLDecoder.decode(value, "UTF-8"))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            val json = Json.encodeToString(serializer(), value)
            bundle.putString(key, URLEncoder.encode(json, "UTF-8"))
        }

        override fun serializeAsValue(value: T): String {
            return URLEncoder.encode(Json.encodeToString(serializer(), value), "UTF-8")
        }
    }
}

inline fun <reified T : Any> createTypeMap(vararg classes: KClass<T>): Map<KType, NavType<*>> {
    return classes.associate { typeOf<T>() to customNavType<T>() }
}