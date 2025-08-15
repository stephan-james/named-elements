package com.github.stephanjames.namedelements

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.ui.table.JBTable
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JComponent

class NamedElementsTableMouseHandlerTest : BasePlatformTestCase() {

    @Mock
    private lateinit var mockTable: JBTable

    @Mock
    private lateinit var mockTableModel: NamedElementsTableModel

    @Mock
    private lateinit var mockMouseEvent: MouseEvent

    @Mock
    private lateinit var mockComponent: JComponent

    @Mock
    private lateinit var mockPsiElement: PsiNamedElement

    @Mock
    private lateinit var mockElementType: IElementType

    @Mock
    private lateinit var mockProject: Project

    @Mock
    private lateinit var mockFile: PsiFile

    private lateinit var handler: NamedElementsTableMouseHandler

    override fun setUp() {
        super.setUp()
        MockitoAnnotations.openMocks(this)
        handler = NamedElementsTableMouseHandler(mockTable)

        whenever(mockTable.model).thenReturn(mockTableModel)
        whenever(mockMouseEvent.component).thenReturn(mockComponent)
        whenever(mockPsiElement.project).thenReturn(mockProject)
        whenever(mockPsiElement.containingFile).thenReturn(mockFile)
    }

    fun testMouseClickedWithValidRowAndColumn() {
        val testPoint = Point(10, 10)
        whenever(mockMouseEvent.point).thenReturn(testPoint)
        whenever(mockTable.rowAtPoint(testPoint)).thenReturn(0)
        whenever(mockTable.columnAtPoint(testPoint)).thenReturn(0)
        whenever(mockMouseEvent.button).thenReturn(MouseEvent.BUTTON1)
        whenever(mockTable.convertRowIndexToModel(0)).thenReturn(0)

        val location = Location("/test/path", 1, 1)
        val namedElement = NamedElement(location, mockPsiElement)
        val namedElements = NamedElements(mockElementType, "testName", mutableSetOf(namedElement))

        whenever(mockTableModel.getRowAt(0)).thenReturn(namedElements)

        handler.mouseClicked(mockMouseEvent)

        verify(mockTable).rowAtPoint(testPoint)
        verify(mockTable).columnAtPoint(testPoint)
    }

    fun testMouseClickedWithInvalidRow() {
        val testPoint = Point(10, 10)
        whenever(mockMouseEvent.point).thenReturn(testPoint)
        whenever(mockTable.rowAtPoint(testPoint)).thenReturn(-1)
        whenever(mockTable.columnAtPoint(testPoint)).thenReturn(0)
        whenever(mockMouseEvent.button).thenReturn(MouseEvent.BUTTON1)

        handler.mouseClicked(mockMouseEvent)

        verify(mockTable, never()).convertRowIndexToModel(anyInt())
    }

    fun testMouseClickedWithInvalidColumn() {
        val testPoint = Point(10, 10)
        whenever(mockMouseEvent.point).thenReturn(testPoint)
        whenever(mockTable.rowAtPoint(testPoint)).thenReturn(0)
        whenever(mockTable.columnAtPoint(testPoint)).thenReturn(-1)
        whenever(mockMouseEvent.button).thenReturn(MouseEvent.BUTTON1)

        handler.mouseClicked(mockMouseEvent)

        verify(mockTable, never()).convertRowIndexToModel(anyInt())
    }

    fun testMouseClickedWithWrongButton() {
        val testPoint = Point(10, 10)
        whenever(mockMouseEvent.point).thenReturn(testPoint)
        whenever(mockTable.rowAtPoint(testPoint)).thenReturn(0)
        whenever(mockTable.columnAtPoint(testPoint)).thenReturn(0)
        whenever(mockMouseEvent.button).thenReturn(MouseEvent.BUTTON3)

        handler.mouseClicked(mockMouseEvent)

        verify(mockTable, never()).convertRowIndexToModel(anyInt())
    }

    fun testMouseClickedWithSingleElement() {
        val testPoint = Point(10, 10)
        whenever(mockMouseEvent.point).thenReturn(testPoint)
        whenever(mockTable.rowAtPoint(testPoint)).thenReturn(0)
        whenever(mockTable.columnAtPoint(testPoint)).thenReturn(0)
        whenever(mockMouseEvent.button).thenReturn(MouseEvent.BUTTON1)
        whenever(mockTable.convertRowIndexToModel(0)).thenReturn(0)

        val location = Location("/test/path", 1, 1)
        val namedElement = NamedElement(location, mockPsiElement)
        val namedElements = NamedElements(mockElementType, "testName", mutableSetOf(namedElement))

        whenever(mockTableModel.getRowAt(0)).thenReturn(namedElements)

        handler.mouseClicked(mockMouseEvent)

        verify(mockTableModel).getRowAt(0)
        assertEquals(1, namedElements.elements.size)
    }

    fun testMouseClickedWithMultipleElements() {
        val testPoint = Point(10, 10)
        whenever(mockMouseEvent.point).thenReturn(testPoint)
        whenever(mockTable.rowAtPoint(testPoint)).thenReturn(0)
        whenever(mockTable.columnAtPoint(testPoint)).thenReturn(0)
        whenever(mockMouseEvent.button).thenReturn(MouseEvent.BUTTON1)
        whenever(mockTable.convertRowIndexToModel(0)).thenReturn(0)
        whenever(mockMouseEvent.locationOnScreen).thenReturn(testPoint)

        val location1 = Location("/test/path1", 1, 1)
        val location2 = Location("/test/path2", 2, 2)
        val namedElement1 = NamedElement(location1, mockPsiElement)
        val namedElement2 = NamedElement(location2, mockPsiElement)
        val namedElements = NamedElements(mockElementType, "testName", mutableSetOf(namedElement1, namedElement2))

        whenever(mockTableModel.getRowAt(0)).thenReturn(namedElements)

        handler.mouseClicked(mockMouseEvent)

        verify(mockTableModel).getRowAt(0)
        assertEquals(2, namedElements.elements.size)
    }

    override fun getTestDataPath(): String = "src/test/testData"

}
