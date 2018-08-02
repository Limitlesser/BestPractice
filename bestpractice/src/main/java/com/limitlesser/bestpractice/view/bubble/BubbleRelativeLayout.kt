package com.finogeeks.finochat.modules.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.limitlesser.bestpractice.view.bubble.Bubble
import com.limitlesser.bestpractice.view.bubble.IBubble


class BubbleRelativeLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
        private val bubble: Bubble = Bubble(context)
) : RelativeLayout(context, attrs, defStyleAttr), IBubble by bubble {

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