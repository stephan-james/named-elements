package com.github.stephanjames.extensions

inline fun <T> MutableCollection<T>.addIfNoneMatch(
    element: T,
    crossinline predicate: (T) -> Boolean
) = if (none(predicate)) {
    add(element)
    true
} else false
