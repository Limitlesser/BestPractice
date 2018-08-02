package com.limitlesser.bestpractice.view.bubble

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.Shape
import org.jetbrains.anko.dip

class BubbleShape @JvmOverloads constructor(context: Context,
                                            var arrowPosition: ArrowDirection = ArrowDirection.LEFT,
                                            var solidColor: Int = Color.WHITE,
                                            var strokeColor: Int = 0xFFCFCFCF.toInt(),
                                            var strokeWidth: Int = context.dip(1)) : Shape() {

    enum class ArrowDirection {
        LEFT, RIGHT
    }

    var arrowWidth: Float = context.dip(6).toFloat()
    var arrowHeight: Float = context.dip(12).toFloat()
    var arrowMarginTop: Float = context.dip(14).toFloat()
    var cornerRadius: Float = context.dip(10).toFloat()
    private val mTop = Path()
    private val mBottom = Path()


    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = solidColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true
        val isLeft = arrowPosition == ArrowDirection.LEFT

        canvas.save()
        if (!isLeft) {
            canvas.scale(-1f, 1f, width / 2, height / 2)
        }

        canvas.drawPath(mTop, paint)
        canvas.drawRect(RectF(arrowWidth, arrowMarginTop + arrowHeight, width, height - cornerRadius), paint)
        canvas.drawPath(mBottom, paint)

        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = strokeWidth.toFloat()
        val offset = strokeWidth / 2f
        val ext = cornerRadius * 0.5f
        //画边框

        canvas.drawArc(RectF(arrowWidth, offset, (arrowWidth + cornerRadius), cornerRadius + offset),
                180f, 90f, false, paint)
        canvas.drawLine(arrowWidth + cornerRadius - ext, offset,
                width - cornerRadius + ext, offset, paint)
        canvas.drawArc(RectF((width - cornerRadius - offset), offset, width - offset, cornerRadius + offset), 270f,
                90f, false, paint)
        canvas.drawLine(width - offset, cornerRadius - ext, width - offset, height - cornerRadius + ext, paint)
        canvas.drawArc(RectF(width - cornerRadius - offset, height - cornerRadius - offset, width - offset, height - offset),
                0f, 90f, false, paint)
        canvas.drawLine(width - cornerRadius + ext, height - offset,
                arrowWidth + cornerRadius - ext, height - offset, paint)
        canvas.drawArc(RectF(arrowWidth, (height - cornerRadius - offset), (arrowWidth + cornerRadius), height - offset),
                90f, 90f, false, paint)
        canvas.drawLine(arrowWidth, height - cornerRadius + ext, arrowWidth, arrowMarginTop + arrowHeight, paint)
        canvas.drawLine(arrowWidth, arrowMarginTop + arrowHeight, 0f, arrowMarginTop + arrowHeight / 2, paint)
        canvas.drawLine(0f, arrowMarginTop + arrowHeight / 2, arrowWidth, arrowMarginTop, paint)
        canvas.drawLine(arrowWidth, arrowMarginTop, arrowWidth, cornerRadius - ext, paint)
        canvas.restore()
    }

    override fun onResize(width: Float, height: Float) {
        mTop.reset()
        mTop.moveTo(arrowWidth, (arrowMarginTop + arrowHeight))
        mTop.lineTo(0f, (arrowMarginTop + arrowHeight / 2))
        mTop.lineTo(arrowWidth, arrowMarginTop)
        mTop.lineTo(arrowWidth, cornerRadius)
        mTop.arcTo(RectF(arrowWidth, 0f, (arrowWidth + cornerRadius), cornerRadius),
                180f, 90f)
        mTop.lineTo((width - cornerRadius), 0f)
        mTop.arcTo(RectF((width - cornerRadius), 0f, width, cornerRadius), 270f, 90f)
        mTop.lineTo(width, (arrowHeight + arrowMarginTop))

        mBottom.reset()
        mBottom.moveTo(width, (height - cornerRadius))
        mBottom.arcTo(RectF((width - cornerRadius), (height - cornerRadius), width, height), 0f, 90f)
        mBottom.lineTo((arrowWidth + cornerRadius), height)
        mBottom.arcTo(RectF(arrowWidth, (height - cornerRadius), (arrowWidth + cornerRadius), height),
                90f, 90f)
        mBottom.lineTo(arrowWidth, height - cornerRadius)

    }

    override fun clone(): BubbleShape {
        val shape = super.clone() as BubbleShape
        shape.arrowHeight = arrowHeight
        shape.arrowMarginTop = arrowMarginTop
        shape.arrowPosition = arrowPosition
        shape.arrowWidth = arrowWidth
        shape.strokeColor = strokeColor
        shape.strokeWidth = strokeWidth
        shape.cornerRadius = cornerRadius
        shape.solidColor = solidColor
        return shape
    }

}

class BubbleDrawable(context: Context, shape: BubbleShape) : ShapeDrawable(shape) {

    init {
        val padding = context.dip(6)
        val paddingLeft = if (shape.arrowPosition == BubbleShape.ArrowDirection.RIGHT) padding else context.dip(14)
        val paddingRight = if (shape.arrowPosition == BubbleShape.ArrowDirection.RIGHT) context.dip(14) else padding
        setPadding(paddingLeft, padding, paddingRight, padding)
    }

}

fun bubbleStateListDrawable(context: Context, bubbleColor: Int, bubbleStrokeColor: Int, bubbleStrokeWidth: Int,
                            isSender: Boolean, showPressEffect: Boolean = true): StateListDrawable {
    val darkColor = Color.argb(Color.alpha(bubbleColor), (Color.red(bubbleColor) * 0.9f).toInt(),
            (Color.green(bubbleColor) * 0.9f).toInt(), (Color.blue(bubbleColor) * 0.9f).toInt())
    val shape = BubbleShape(context,
            if (isSender) BubbleShape.ArrowDirection.RIGHT else BubbleShape.ArrowDirection.LEFT,
            bubbleColor, bubbleStrokeColor, bubbleStrokeWidth)
    val darkShape = shape.clone()
    darkShape.solidColor = darkColor

    val drawable = BubbleDrawable(context, shape)
    val darkDrawable = BubbleDrawable(context, darkShape)
    val stateListDrawable = StateListDrawable()
    if (showPressEffect)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), darkDrawable)
    stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), darkDrawable)
    stateListDrawable.addState(intArrayOf(), drawable)
    return stateListDrawable
}
