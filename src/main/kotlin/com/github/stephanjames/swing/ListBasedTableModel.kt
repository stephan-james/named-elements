package com.github.stephanjames.swing

import javax.swing.table.AbstractTableModel

open class ListBasedTableModel<T>(
    private var data: List<T>,
    private var columnNames: List<String>,
    private val valueProvider: (T, Int) -> Any?
) : AbstractTableModel() {

    init {
        require(columnNames.isNotEmpty()) { "Column names cannot be empty" }
    }

    override fun getRowCount() = data.size

    override fun getColumnCount() = columnNames.size

    override fun getColumnName(col: Int) = columnNames[col]

    override fun getColumnClass(columnIndex: Int): Class<*> {
        if (rowCount == 0) {
            return Any::class.java
        }
        val value = getValueAt(0, columnIndex)
        return value?.javaClass ?: Any::class.java
    }

    override fun getValueAt(row: Int, col: Int) = valueProvider(getRowAt(row), col)

    fun getRowAt(row: Int) = data[row]

}
