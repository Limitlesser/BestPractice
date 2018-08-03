package com.limitlesser.bestpractice.text

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import android.view.View

inline fun span(init: SpannableContext.() -> SpannableStringBuilder): SpannableStringBuilder {
    return SpannableContext().run(init)
}

class SpannableContext {

    fun span(span: Any, start: Int? = null, end: Int? = null, text: CharSequence): SpannableStringBuilder {
        val str = SpannableStringBuilder.valueOf(text)
        str.setSpan(span, start ?: 0, end ?: str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return str
    }

    inline fun color(color: Int, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
            span(ForegroundColorSpan(color), start, end, text())

    inline fun background(color: Int, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
            span(BackgroundColorSpan(color), start, end, text())

    inline fun bold(start: Int? = null, end: Int? = null, text: () -> CharSequence) =
            span(StyleSpan(Typeface.BOLD), start, end, text())

    inline fun italic(start: Int? = null, end: Int? = null, text: () -> CharSequence) =
            span(StyleSpan(Typeface.ITALIC), start, end, text())

    fun click(click: (View) -> Unit, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
            span(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    click(p0)
                }
            }, start, end, text())

    fun url(url: String, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
            span(URLSpan(url), start, end, text())

    operator fun SpannableStringBuilder.plus(other: CharSequence): SpannableStringBuilder {
        append(other)
        return this
    }

    operator fun CharSequence.unaryPlus(): SpannableStringBuilder {
        return SpannableStringBuilder.valueOf(this)
    }

}