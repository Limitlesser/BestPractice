package com.limitlesser.bestpractice.model

data class ProgressResult<out T>(val progress: Int = 0, val data: T? = null) {

    val isComplete = data != null

    inline fun showProgress(block: (Boolean, Int) -> Unit) {
        if (isComplete) block(false, progress)
        else block(true, progress)
    }

    inline fun handleResult(block: (T) -> Unit) {
        if (isComplete) block(data!!)
    }

}