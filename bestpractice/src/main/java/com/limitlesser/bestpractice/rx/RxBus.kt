package com.limitlesser.bestpractice.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object RxBus {

    private val bus = PublishSubject.create<Any>()

    fun post(event: Any) {
        bus.onNext(event)
    }

    fun post(key: String, value: Any) {
        bus.onNext(KeyEvent(key, value))
    }

    fun asObservable(): Observable<Any> = bus

    inline fun <reified T> observe(): Observable<T> {
        return asObservable().filter { it is T }.cast(T::class.java)
    }

    fun <T> observe(key: String): Observable<T> {
        return observe<KeyEvent<T>>().filter { it.key == key }.map { it.value }
    }
}

interface Event {

    fun post() = RxBus.post(this)
}

open class KeyEvent<T>(val key: String, val value: T) : Event
