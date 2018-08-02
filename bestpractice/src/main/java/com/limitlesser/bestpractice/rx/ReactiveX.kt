package com.limitlesser.bestpractice.rx

import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.CancellationException


fun <T> Observable<T>.asyncIO(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.asyncIO(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.asyncIO(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.asyncIO(): Completable {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.onLoading(loading: (Boolean) -> Unit): Observable<T> {
    return doOnSubscribe { loading(true) }.doFinally { loading(false) }
}

fun <T> Single<T>.onLoading(loading: (Boolean) -> Unit): Single<T> {
    return doOnSubscribe { loading(true) }.doFinally { loading(false) }
}

fun <T> Flowable<T>.onLoading(loading: (Boolean) -> Unit): Flowable<T> {
    return doOnSubscribe { loading(true) }.doFinally { loading(false) }
}

fun Completable.onLoading(loading: (Boolean) -> Unit): Completable {
    return doOnSubscribe { loading(true) }.doFinally { loading(false) }
}

/**
 * RxLifeCycle 通过发送CancellationException来停止Single 这里做一下安全处理
 */
fun <T> Single<T>.bindToLifecycleSafely(provider: LifecycleProvider<*>): Single<T> {
    return bindToLifecycle(provider).onErrorResumeNext {
        if (it is CancellationException) Single.never()
        else Single.error(it)
    }
}
