package com.github.stephanjames.swing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ListBasedTableModelTest {

    @Test
    fun testConstructorWithEmptyColumnNames() {
        assertThrows(IllegalArgumentException::class.java) {
            ListBasedTableModel(
                emptyList<String>(),
                emptyList(),
                { _, _ -> null }
            )
        }
    }

    @Test
    fun testGetRowCount() {
        val data = listOf("item1", "item2", "item3")
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> null }

        assertEquals(3, model.rowCount)
    }

    @Test
    fun testGetColumnCount() {
        val data = listOf("item1")
        val columnNames = listOf("Col1", "Col2", "Col3")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> null }

        assertEquals(3, model.columnCount)
    }

    @Test
    fun testGetColumnName() {
        val data = listOf("item1")
        val columnNames = listOf("FirstColumn", "SecondColumn")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> null }

        assertEquals("FirstColumn", model.getColumnName(0))
        assertEquals("SecondColumn", model.getColumnName(1))
    }

    @Test
    fun testGetValueAtCallsValueProvider() {
        val data = listOf("item1", "item2")
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames, { item, column ->
            "value($item,$column)"
        })

        val result = model.getValueAt(1, 0)

        assertEquals("value(item2,0)", result)
    }

    @Test
    fun testGetColumnClassWithEmptyData() {
        val data = emptyList<String>()
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> null }

        assertEquals(Any::class.java, model.getColumnClass(0))
    }

    @Test
    fun testGetColumnClassWithStringValue() {
        val data = listOf("item1")
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> "stringValue" }

        assertEquals(String::class.java, model.getColumnClass(0))
    }

    @Test
    fun testGetColumnClassWithIntegerValue() {
        val data = listOf("item1")
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> 42 }

        assertEquals(Integer::class.java, model.getColumnClass(0))
    }

    @Test
    fun testGetColumnClassWithNullValue() {
        val data = listOf("item1")
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> null }

        assertEquals(Any::class.java, model.getColumnClass(0))
    }

    @Test
    fun testGetColumnClassWithBooleanValue() {
        val data = listOf("item1")
        val columnNames = listOf("Column1")

        val model = ListBasedTableModel(data, columnNames) { _, _ -> true }

        assertEquals(java.lang.Boolean::class.java, model.getColumnClass(0))
    }

}
