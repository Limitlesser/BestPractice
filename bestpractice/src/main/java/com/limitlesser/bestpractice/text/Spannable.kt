package com.limitlesser.bestpractice.text

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import android.view.View

inline fun span(init: SpannableStringBuilderExt.() -> Unit): SpannableStringBuilder {
    return SpannableStringBuilderExt().apply(init)
}

fun SpannableStringBuilder.span(text: CharSequence, span: Any, start: Int? = null, end: Int? = null): SpannableStringBuilder {
    val preStart = length
    if (text != this) append(text)
    setSpan(span, preStart + (start ?: 0), preStart + (end ?: text.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

inline fun SpannableStringBuilder.color(color: Int, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
        span(text(), ForegroundColorSpan(color), start, end)

inline fun SpannableStringBuilder.background(color: Int, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
        span(text(), BackgroundColorSpan(color), start, end)

inline fun SpannableStringBuilder.bold(start: Int? = null, end: Int? = null, text: () -> CharSequence) =
        span(text(), StyleSpan(Typeface.BOLD), start, end)

inline fun SpannableStringBuilder.italic(start: Int? = null, end: Int? = null, text: () -> CharSequence) =
        span(text(), StyleSpan(Typeface.ITALIC), start, end)

fun SpannableStringBuilder.click(click: (View) -> Unit, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
        span(text(), object : ClickableSpan() {
            override fun onClick(p0: View) {
                click(p0)
            }
        }, start, end)

fun SpannableStringBuilder.url(url: String, start: Int? = null, end: Int? = null, text: () -> CharSequence) =
        span(text(), URLSpan(url), start, end)


class SpannableStringBuilderExt : SpannableStringBuilder() {

    operator fun SpannableStringBuilder.plus(other: CharSequence): SpannableStringBuilder {
        return if (this == other) this else append(other)
    }

    operator fun CharSequence.unaryPlus(): SpannableStringBuilder {
        return append(this)
    }

}