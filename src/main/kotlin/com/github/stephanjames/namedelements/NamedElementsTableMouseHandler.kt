package com.github.stephanjames.namedelements

import com.intellij.ui.table.JBTable
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class NamedElementsTableMouseHandler(private val table: JBTable) : MouseAdapter() {

    override fun mouseClicked(mouseEvent: MouseEvent) {
        val row = table.rowAtPoint(mouseEvent.point)
        val col = table.columnAtPoint(mouseEvent.point)

        if (isValidClick(row, col, mouseEvent)) {
            handleTableClick(row, mouseEvent)
        }
    }

    private fun isValidClick(row: Int, col: Int, mouseEvent: MouseEvent) =
        row >= 0 && col >= 0 && mouseEvent.button == MouseEvent.BUTTON1

    private fun handleTableClick(row: Int, mouseEvent: MouseEvent) {
        val namedElements = selectedNamedElements(row)

        if (namedElements.elements.size == 1) {
            navigateTo(namedElements.elements.first().element)
        } else {
            createNamedElementsPopup(namedElements.elements)
                .showInScreenCoordinates(mouseEvent.component, mouseEvent.locationOnScreen)
        }
    }

    private fun selectedNamedElements(row: Int): NamedElements {
        val modelRow = table.convertRowIndexToModel(row)
        val model = table.model as NamedElementsTableModel
        return model.getRowAt(modelRow)
    }


}
