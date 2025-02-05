package com.dnd.safety.presentation.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnd.safety.R

val pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_reqular, FontWeight.SemiBold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_reqular, FontWeight.Normal)
)

private val pretendardStyle = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    )
)

val Typography = SafetyTypography(
    title1 = pretendardStyle.copy(
        fontSize = 24.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold
    ),
    title2 = pretendardStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Bold,
    ),
    title3 = pretendardStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    title4 = pretendardStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Medium,
    ),
    title5 = pretendardStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
    ),
    body1 = pretendardStyle.copy(
        fontSize = 18.sp,
        lineHeight = 27.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    body2 = pretendardStyle.copy(
        fontSize = 18.sp,
        lineHeight = 27.sp,
        fontWeight = FontWeight.Medium,
    ),
    body3 = pretendardStyle.copy(
        fontSize = 18.sp,
        lineHeight = 27.sp,
    ),
    paragraph1 = pretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
    ),
    paragraph2 = pretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    label1 = pretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Medium,
    ),
    label2 = pretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Normal,
    ),
    smallText = pretendardStyle.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.SemiBold,
    ),
)

@Immutable
data class SafetyTypography(
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val title4: TextStyle,
    val title5: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val paragraph1: TextStyle,
    val paragraph2: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val smallText: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    SafetyTypography(
        title1 = pretendardStyle,
        title2 = pretendardStyle,
        title3 = pretendardStyle,
        title4 = pretendardStyle,
        title5 = pretendardStyle,
        body1 = pretendardStyle,
        body2 = pretendardStyle,
        body3 = pretendardStyle,
        paragraph1 = pretendardStyle,
        paragraph2 = pretendardStyle,
        label1 = pretendardStyle,
        label2 = pretendardStyle,
        smallText = pretendardStyle,
    )
}

@Immutable
data class SafetyShapes(
    val r10: RoundedCornerShape,
    val r15: RoundedCornerShape,
    val r30: RoundedCornerShape,
    val r100: RoundedCornerShape,
)

val LocalShapes = staticCompositionLocalOf {
    SafetyShapes(
        r10 = RoundedCornerShape(10.dp),
        r15 = RoundedCornerShape(15.dp),
        r30 = RoundedCornerShape(30.dp),
        r100 = RoundedCornerShape(100.dp),
    )
}