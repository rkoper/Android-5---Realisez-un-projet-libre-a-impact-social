package com.so.dingbring

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {

    private val bus = PublishSubject.create<Any>()

    fun send(event: Any) {
        bus.onNext(event)
    }

    fun <T> observeEvent(type: Class<T>): Observable<T> =
        bus.ofType(type)
}

class NeedToUpdateEvent{

}