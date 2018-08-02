package com.limitlesser.bestpractice.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.*
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import org.jetbrains.anko.dip


fun dividerItemDecoration(context: Context, height: Int = context.dip(0.5f), color: Int = Color.parseColor("#d8d8da"),
                          inset: Int = context.dip(10)): DividerItemDecoration {
    val decoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
    decoration.setDrawable(dividerDrawable(height, color, inset))
    return decoration
}

fun dividerDrawable(height: Int, color: Int, inset: Int): Drawable {
    return InsetDrawable(GradientDrawable().apply {
        setSize(-1, height)
        setColor(color)
    }, inset, 0, 0, 0)
}

val LayerDrawable.drawables: List<Drawable>
    get() = List(numberOfLayers, this::getDrawable)


/** imageView bitmap 如果是bitmapDrawable */
val ImageView.bitmap: Bitmap?
    get() {
        val drawable = drawable ?: return null
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is TransitionDrawable -> {
                val bitmapDrawable = drawable.drawables.asReversed().firstOrNull { it is BitmapDrawable }
                (bitmapDrawable as? BitmapDrawable)?.bitmap
            }
            else -> null
        }
    }

var View.isVisible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

val ViewGroup.children
    get() = List(childCount, this::getChildAt)

var TabLayout.isEnableSwitch: Boolean
    get() {
        val strip = children[0] as LinearLayout
        return strip.isEnabled && strip.children.all { it.isClickable == true }
    }
    set(value) {
        val strip = children[0] as LinearLayout
        strip.isEnabled = value
        strip.children.forEach { it.isClickable = value }
    }

val Menu.items
    get() = List(size(), this::getItem)

/** use setOnTouchListener to disable viewpager scroll */
var ViewPager.isPagingEnabled: Boolean
    @Deprecated("no getter")
    get() = throw IllegalAccessException()
    set(value) {
        setOnTouchListener(if (value) null else View.OnTouchListener { _, _ -> true })
    }

var ImageView.imageTintListCompat: ColorStateList?
    get() = ImageViewCompat.getImageTintList(this)
    set(value) {
        ImageViewCompat.setImageTintList(this, value)
    }

var View.padding: Int
    @Deprecated("no getter")
    get() = throw IllegalAccessException()
    set(value) {
        setPadding(value, value, value, value)
    }

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode");
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)

        menuView.children.forEach {
            (it as BottomNavigationItemView).setShiftingMode(false);
            it.setChecked(it.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
    }
}