package com.github.stephanjames.namedelements

import com.github.stephanjames.swing.SingleColumnTableModel
import com.intellij.ide.util.PsiNavigationSupport
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.table.TableModel

fun navigateTo(element: PsiElement, requestFocus: Boolean = true) {
    val project = element.project
    val virtualFile = element.containingFile?.virtualFile ?: return
    val offset = element.textOffset

    ApplicationManager.getApplication().invokeLater({
        if (project.isDisposed) return@invokeLater
        PsiNavigationSupport.getInstance()
            .createNavigatable(project, virtualFile, offset)
            .navigate(requestFocus)
    }, ModalityState.defaultModalityState())
}

fun createNamedElementsPanel(namedElements: List<NamedElements>): JPanel {
    val model = NamedElementsTableModel(namedElements)
    val table = createNamedElementsTable(model)
    return JPanel(BorderLayout()).apply {
        add(JBScrollPane(table), BorderLayout.CENTER)
    }
}

private fun createNamedElementsTable(model: TableModel): JBTable =
    JBTable(model).apply {
        autoCreateRowSorter = true
        addMouseListener(NamedElementsTableMouseHandler(this))
    }

fun createNamedElementsPopup(namedElements: Collection<NamedElement>): JBPopup {
    val model = createNamedElementsModel(namedElements)
    val table = createNamedElementsTable(model)
    return createNamedElementsPopup(table)
}

private fun createNamedElementsModel(namedElements: Collection<NamedElement>) =
    SingleColumnTableModel(
        data = namedElements.sortedBy { it.element.containingFile.name },
        valueProvider = { displayText(it.location) }
    )

private fun displayText(location: Location): String =
    location.path +
            if (location.lineNumber > 0)
                "(${location.lineNumber.plus(1)}:${location.columnNumber.plus(1)})"
            else
                ""

private fun createNamedElementsPopup(table: JBTable): JBPopup =
    JBPopupFactory.getInstance()
        .createComponentPopupBuilder(JBScrollPane(table), null)
        .setResizable(true)
        .setMovable(true)
        .setMinSize(Dimension(900, table.rowCount * 12))
        .createPopup()

private fun createNamedElementsTable(model: SingleColumnTableModel<NamedElement>) =
    JBTable(model).apply {
        setShowGrid(true)
        setShowColumns(false)
        selectionModel.addListSelectionListener {
            if (it.valueIsAdjusting || selectedRow < 0) {
                return@addListSelectionListener
            }
            val namedElement = model.getRowAt(selectedRow)
            navigateTo(namedElement.element)
        }
    }
