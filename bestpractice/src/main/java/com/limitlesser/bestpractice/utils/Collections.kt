package com.limitlesser.bestpractice.utils

import android.util.SparseArray


@Suppress("NOTHING_TO_INLINE")
inline operator fun <E> SparseArray<E>.set(k: Int, v: E?) = put(k, v)
