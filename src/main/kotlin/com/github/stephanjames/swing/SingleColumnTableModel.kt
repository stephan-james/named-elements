package com.github.stephanjames.swing

open class SingleColumnTableModel<T>(
    columnName: String = "",
    data: List<T>,
    private val valueProvider: (T) -> Any?
) : ListBasedTableModel<T>(
    data,
    listOf(columnName),
    { item, _ -> valueProvider(item) }
)
