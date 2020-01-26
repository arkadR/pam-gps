package com.pam.gps.utils

class Observable<T> (initialValue: T) {

  private val subscribers = mutableListOf<(T) -> Unit>()

  private var _value: T = initialValue


  var value: T
    get() = _value
    set(value) {
      subscribers.forEach { s -> s(value) }
      _value = value
    }

  fun subscribe(callback: (T) -> Unit) {
    subscribers.add(callback);
  }
}