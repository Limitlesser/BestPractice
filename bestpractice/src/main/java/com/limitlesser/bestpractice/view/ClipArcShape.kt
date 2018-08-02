package com.limitlesser.bestpractice.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape


class ClipArcShape(var angle: Float, var cornerRadius: Float = 0f) : RectShape() {

    private val path = Path()
    private val rect = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.save()
        canvas.clipPath(path)
        canvas.rotate(-90f, rect.centerX(), rect.centerY())
        canvas.drawArc(rect, angle, 360 - angle, true, paint)
        canvas.restore()
    }

    override fun onResize(width: Float, height: Float) {
        super.onResize(width, height)
        path.reset()
        path.addRoundRect(rect(), cornerRadius, cornerRadius, Path.Direction.CW)
        rect.set(rect())
        val r = Math.sqrt(Math.pow((rect().width() / 2).toDouble(), 2.0) + Math.pow((rect().height() / 2).toDouble(), 2.0)) * 2
        rect.inset(-(r - rect().width() / 2).toFloat(), -(r - rect().width() / 2).toFloat())
    }
}

class ClipArcDrawable(color: Int = 0x4c000000, cornerRadius: Int = 4) : ShapeDrawable() {

    private val clipArcShape: ClipArcShape = ClipArcShape(0f, cornerRadius.toFloat())

    init {
        shape = clipArcShape
        paint.color = color
    }

    var color
        get() = paint.color
        set(value) {
            paint.color = value
        }

    var cornerRadius
        get() = clipArcShape.cornerRadius
        set(value) {
            clipArcShape.cornerRadius = value
            clipArcShape.resize(intrinsicWidth.toFloat(), intrinsicHeight.toFloat())
        }

    override fun onLevelChange(level: Int): Boolean {
        clipArcShape.angle = (360 * level / 10000).toFloat()
        invalidateSelf()
        return true
    }
}