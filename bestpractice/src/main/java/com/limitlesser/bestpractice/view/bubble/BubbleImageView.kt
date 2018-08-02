package com.limitlesser.bestpractice.view.bubble

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * 气泡形状的imageView 可以设置圆角边框等参数
 */
class BubbleImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
        private val bubble: Bubble = Bubble(context)
) : AppCompatImageView(context, attrs, defStyleAttr), IBubble by bubble {

    init {
        bubble.onInvalidate = { reset(width, height);invalidate() }
    }

    override fun draw(canvas: Canvas) {
        val count = canvas.save()
        canvas.clipPath(bubble.path)
        super.draw(canvas)
        bubble.drawStroke(canvas)
        canvas.restoreToCount(count)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bubble.reset(w, h)
    }
}
