package com.limitlesser.bestpractice.view.recyclerview

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


class EndlessRecyclerViewScrollListener(private val mLayoutManager: RecyclerView.LayoutManager,
                                        private var onLoadMore: (Int, Int, RecyclerView) -> Unit,
                                        private val minLoadingStartPosition: Int = 10,
                                        private var visibleThreshold: Int = 4) : RecyclerView.OnScrollListener() {

    // The current offset index of data you have loaded
    var currentPage = 0
        private set
    // The total number of items in the dataset after the last load
    var previousTotalItemCount = 0
        private set
    // True if we are still waiting for the last set of data to load.
    var loading = true
        private set

    var hasMoreData = true

    var isEnabled = true

    // Sets the starting page index
    private val startingPageIndex = 0

    init {
        when (mLayoutManager) {
            is GridLayoutManager -> visibleThreshold *= mLayoutManager.spanCount
            is StaggeredGridLayoutManager -> visibleThreshold *= mLayoutManager.spanCount
        }
    }


    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {

        if (!isEnabled) return

        val totalItemCount = mLayoutManager.itemCount

        val lastVisibleItemPosition = when (mLayoutManager) {
            is StaggeredGridLayoutManager -> mLayoutManager.findLastVisibleItemPositions(null).max()
                    ?: 0
            is GridLayoutManager -> mLayoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> mLayoutManager.findLastVisibleItemPosition()
            else -> 0
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (hasMoreData && !loading && lastVisibleItemPosition > minLoadingStartPosition
                && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
        hasMoreData = true
    }

}