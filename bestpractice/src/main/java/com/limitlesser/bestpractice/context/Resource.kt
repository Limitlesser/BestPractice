@file:Suppress("NOTHING_TO_INLINE")

package com.limitlesser.bestpractice.context

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat


inline fun <T> Context.attr(@AttrRes attr: Int, block: (TypedArray) -> T): T {
    val a = obtainStyledAttributes(intArrayOf(attr))
    val t = block.invoke(a)
    a.recycle()
    return t
}

fun Context.attrColor(@AttrRes attr: Int): Int = attr(attr) { it.getColor(0, Color.WHITE) }

fun Context.attrDimension(@AttrRes attr: Int): Float = attr(attr) { it.getDimension(0, 0.0f) }

fun Context.attrDimensionPixelSize(@AttrRes attr: Int): Int = attr(attr) { it.getDimensionPixelSize(0, 0) }

fun Context.attrInt(@AttrRes attr: Int): Int = attr(attr) { it.getInt(0, 0) }

fun Context.attrId(@AttrRes attr: Int): Int = attr(attr) { it.getResourceId(0, 0) }

fun Context.attrBoolean(@AttrRes attr: Int): Boolean = attr(attr) { it.getBoolean(0, false) }

fun Context.attrDrawable(@AttrRes attr: Int): Drawable = attr(attr) { it.getDrawable(0) }

fun Context.attrString(@AttrRes attr: Int): String = attr(attr) { it.getString(0) }

inline fun Context.string(@StringRes resId: Int) = getString(resId)!!

inline fun Context.string(@StringRes resId: Int, vararg formatArgs: Any) = getString(resId, formatArgs)!!

inline fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

inline fun Context.colorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)!!

inline fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)!!
