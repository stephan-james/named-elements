package com.github.stephanjames.swing

import org.junit.Assert.assertEquals
import org.junit.Test

class SingleColumnTableModelTest {

    @Test
    fun testConstructorWithDefaultColumnName() {
        val data = listOf("item1", "item2")
        val model = SingleColumnTableModel(data = data) { it }

        assertEquals(1, model.columnCount)
        assertEquals("", model.getColumnName(0))
        assertEquals(2, model.rowCount)
    }

    @Test
    fun testConstructorWithCustomColumnName() {
        val data = listOf("item1", "item2")
        val model = SingleColumnTableModel("CustomColumn", data) { it }

        assertEquals(1, model.columnCount)
        assertEquals("CustomColumn", model.getColumnName(0))
        assertEquals(2, model.rowCount)
    }

    @Test
    fun testGetValueAtCallsValueProvider() {
        val data = listOf(10, 20, 30)
        val model = SingleColumnTableModel("Numbers", data) { it * 2 }

        assertEquals(20, model.getValueAt(0, 0))
        assertEquals(40, model.getValueAt(1, 0))
        assertEquals(60, model.getValueAt(2, 0))
    }

    @Test
    fun testGetValueAtWithStringData() {
        val data = listOf("hello", "world")
        val model = SingleColumnTableModel("Strings", data) { it.uppercase() }

        assertEquals("HELLO", model.getValueAt(0, 0))
        assertEquals("WORLD", model.getValueAt(1, 0))
    }

    @Test
    fun testGetValueAtWithNullReturn() {
        val data = listOf("item1", "item2")
        val model = SingleColumnTableModel("Test", data) { null }

        assertEquals(null, model.getValueAt(0, 0))
        assertEquals(null, model.getValueAt(1, 0))
    }

    @Test
    fun testGetRowAt() {
        val data = listOf("first", "second", "third")
        val model = SingleColumnTableModel("Items", data) { it }

        assertEquals("first", model.getRowAt(0))
        assertEquals("second", model.getRowAt(1))
        assertEquals("third", model.getRowAt(2))
    }

    @Test
    fun testGetColumnClassWithIntegerValues() {
        val data = listOf(1, 2, 3)
        val model = SingleColumnTableModel("Numbers", data) { it }

        assertEquals(Integer::class.java, model.getColumnClass(0))
    }

    @Test
    fun testGetColumnClassWithStringValues() {
        val data = listOf("test")
        val model = SingleColumnTableModel("Text", data) { it }

        assertEquals(String::class.java, model.getColumnClass(0))
    }

    @Test
    fun testWithEmptyData() {
        val data = emptyList<String>()
        val model = SingleColumnTableModel("Empty", data) { it }

        assertEquals(0, model.rowCount)
        assertEquals(1, model.columnCount)
        assertEquals("Empty", model.getColumnName(0))
    }

    @Test
    fun testValueProviderWithComplexObjects() {
        data class Person(val name: String, val age: Int)
        val data = listOf(Person("Alice", 25), Person("Bob", 30))
        val model = SingleColumnTableModel("People", data) { "${it.name} (${it.age})" }

        assertEquals("Alice (25)", model.getValueAt(0, 0))
        assertEquals("Bob (30)", model.getValueAt(1, 0))
    }

}
