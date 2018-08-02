package com.limitlesser.bestpractice.view.bubble

import android.content.Context
import android.graphics.*
import org.jetbrains.anko.dip
import kotlin.properties.Delegates

enum class Arrow {
    LEFT, RIGHT
}

interface IBubble {
    var arrowPosition: Arrow
    var arrowWidth: Int
    var arrowHeight: Int
    var arrowMarginTop: Int
    var cornerRadius: Int
    var borderColor: Int
    var borderWidth: Int
}

class Bubble(context: Context, var onInvalidate: (Bubble.() -> Unit)? = null) : IBubble {

    override var arrowPosition by observable(Arrow.LEFT)
    override var arrowWidth: Int by observable(context.dip(6))
    override var arrowHeight: Int by observable(context.dip(12))
    override var arrowMarginTop: Int by observable(context.dip(15))
    override var cornerRadius: Int by observable(context.dip(10))
    override var borderColor: Int by observable(0xffcfcfcf.toInt())
    override var borderWidth: Int by observable(context.dip(1))

    val path = Path()
    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = borderWidth.toFloat()
    }

    fun <T> observable(init: T) = Delegates.observable(init) { _, _, _ ->
        strokePaint.apply {
            color = borderColor
            strokeWidth = borderWidth.toFloat()
        }
        onInvalidate?.invoke(this)
    }

    fun drawStroke(canvas: Canvas) {
        canvas.drawPath(path, strokePaint)
    }

    fun reset(w: Int, h: Int) {
        with(path) {
            reset()
            moveTo(arrowWidth.toFloat(), cornerRadius.toFloat())
            arcTo(RectF(arrowWidth.toFloat(), 0f, (arrowWidth + cornerRadius).toFloat(), cornerRadius.toFloat()),
                    180f, 90f)
            lineTo((w - cornerRadius).toFloat(), 0f)
            arcTo(RectF((w - cornerRadius).toFloat(), 0f, w.toFloat(), cornerRadius.toFloat()), 270f, 90f)
            lineTo(w.toFloat(), (h - cornerRadius).toFloat())
            arcTo(RectF((w - cornerRadius).toFloat(), (h - cornerRadius).toFloat(), w.toFloat(), h.toFloat()), 0f, 90f)
            lineTo((arrowWidth + cornerRadius).toFloat(), h.toFloat())
            arcTo(RectF(arrowWidth.toFloat(), (h - cornerRadius).toFloat(), (arrowWidth + cornerRadius).toFloat(), h.toFloat()),
                    90f, 90f)
            lineTo(arrowWidth.toFloat(), (arrowMarginTop + arrowHeight).toFloat())
            lineTo(0f, (arrowMarginTop + arrowHeight / 2).toFloat())
            lineTo(arrowWidth.toFloat(), arrowMarginTop.toFloat())
            lineTo(arrowWidth.toFloat(), cornerRadius.toFloat())

            if (arrowPosition == Arrow.RIGHT) {
                val matrix = Matrix()
                matrix.postScale(-1f, 1f)
                matrix.postTranslate(w.toFloat(), 0f)
                path.transform(matrix)
            }
        }
    }
}