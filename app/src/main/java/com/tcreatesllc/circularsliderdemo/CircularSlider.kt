package com.tcreatesllc.circularsliderdemo


import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircularSlider(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 20f,
    cap: StrokeCap = StrokeCap.Round,
    touchStroke: Float = 50f,
    onChange: ((Float) -> Unit)? = null
) {
    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var angle by remember { mutableStateOf(0f) }
    var nearTheThumbIndicator by remember { mutableStateOf(false) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var appliedAngle by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = angle) {

        if (angle < 0.0f && angle > -90f) {
            angle = 0.0f
        } else if (angle < 0.0f && angle < -90f) {
            angle = 180.0f
        } else if (angle >= 180f) {
            angle = 180f
        }

        appliedAngle = angle

        onChange?.invoke(angle / 180f)
    }


    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFFD9C7DF),
            0.4f to Color(0xFF99C4EF),
            3.0f to Color(0xFFD9C7DF)
        )
    )

    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                width = it.size.width
                height = it.size.height
                center = Offset(width / 2f, height / 2f)
                radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f
            }
            .pointerInteropFilter {
                val x = it.x
                val y = it.y
                val offset = Offset(x, y)
                when (it.action) {

                    MotionEvent.ACTION_DOWN -> {
                        val d = distance(offset, center)
                        val a = angle(center, offset)
                        if (d >= radius - touchStroke / 2f && d <= radius + touchStroke / 2f) {
                            nearTheThumbIndicator = true
                            angle = a
                        } else {
                            nearTheThumbIndicator = false
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (nearTheThumbIndicator) {
                            angle = angle(center, offset)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        nearTheThumbIndicator = false
                    }

                    else -> return@pointerInteropFilter false
                }
                return@pointerInteropFilter true
            }
    ) {

        drawArc(
            brush = gradient,
            startAngle = -180f,
            sweepAngle = 180f,
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )

        drawCircle(
            color = Color.White,
            radius = 30f,
            center = center + Offset(
                radius * cos((-180 + appliedAngle) * PI / 180f).toFloat(),
                radius * sin((-180 + appliedAngle) * PI / 180f).toFloat()
            )
        )

    }
}

fun angle(center: Offset, offset: Offset): Float {
    val rad = atan2(center.y - offset.y, center.x - offset.x)
    val deg = Math.toDegrees(rad.toDouble())
    return deg.toFloat()
}

fun distance(first: Offset, second: Offset): Float {
    return sqrt((first.x - second.x).square() + (first.y - second.y).square())
}

fun Float.square(): Float {
    return this * this
}