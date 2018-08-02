package com.limitlesser.bestpractice.view.recyclerview

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 给recyclerView添加空白分割线
 */
class SpaceItemDecoration(var space: Int, var isAddAboveFirst: Boolean = false,
                          var isAddBelowLast: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
        if (space <= 0) return
        val orientation = (parent.layoutManager as? LinearLayoutManager)?.orientation ?: return
        val (begin, end) = if (orientation == LinearLayoutManager.VERTICAL) {
            outRect::top to outRect::bottom
        } else {
            outRect::left to outRect::right
        }
        val position = parent.getChildAdapterPosition(view)
        if (isAddAboveFirst && position < 1 || position >= 1)
            begin.set(space)

        if (isAddBelowLast && position == parent.adapter.itemCount - 1)
            end.set(space)
    }
}