package com.github.stephanjames.namedelements

import com.github.stephanjames.swing.ListBasedTableModel

private enum class Column {
    CATEGORY,
    NAME,
    COUNT;

    companion object {
        val titles = entries.map { Bundle.message("columnNames.${it.name.lowercase()}") }
    }
}

class NamedElementsTableModel(data: List<NamedElements>) : ListBasedTableModel<NamedElements>(
    data,
    Column.titles
    , { namedElements, col ->
        when (Column.entries[col]) {
            Column.CATEGORY -> namedElements.category.debugName
            Column.NAME -> namedElements.name
            Column.COUNT -> namedElements.elements.size
        }
    }
)
