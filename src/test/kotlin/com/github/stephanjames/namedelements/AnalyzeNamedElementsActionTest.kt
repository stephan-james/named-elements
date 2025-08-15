package com.github.stephanjames.namedelements

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiDirectory
import com.intellij.testFramework.TestActionEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AnalyzeNamedElementsActionTest : BasePlatformTestCase() {

    private lateinit var action: AnalyzeNamedElementsAction

    override fun setUp() {
        super.setUp()
        action = AnalyzeNamedElementsAction()
    }

    fun testActionPerformedWithValidDirectory() {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        toolWindowManager.registerToolWindow(
            "Named Elements Analysis",
            true,
            ToolWindowAnchor.BOTTOM
        )

        val testFile = myFixture.addFileToProject("TestClass.kt", """
            class TestClass {
                fun testMethod() {}
                val testProperty = "test"
            }
        """.trimIndent())

        val directory = testFile.containingDirectory
        val event = createActionEvent(directory)

        action.actionPerformed(event)

        val toolWindow = getToolWindow()
        assertNotNull(toolWindow)

        toolWindowManager.unregisterToolWindow("Named Elements Analysis")
    }

    fun testActionPerformedWithoutProject() {
        val event = TestActionEvent.createTestEvent { dataId ->
            when (dataId) {
                CommonDataKeys.PROJECT.name -> null
                else -> null
            }
        }

        action.actionPerformed(event)
    }

    fun testActionPerformedWithoutDirectory() {
        val event = TestActionEvent.createTestEvent { dataId ->
            when (dataId) {
                CommonDataKeys.PROJECT.name -> project
                CommonDataKeys.PSI_ELEMENT.name -> null
                else -> null
            }
        }

        action.actionPerformed(event)
    }

    fun testCreateContentTabsWithEmptyAnalysisResult() {
        val action = AnalyzeNamedElementsAction()
        val analysisResult: AnalysisResult = emptyMap()

        val method = action.javaClass.getDeclaredMethod("createContentTabs", AnalysisResult::class.java)
        method.isAccessible = true
        val result = method.invoke(action, analysisResult) as List<*>

        assertTrue(result.isEmpty())
    }

    fun testExecuteAnalysisReturnsResult() {
        val testFile = myFixture.addFileToProject("Sample.kt", """
            class SampleClass {
                fun sampleMethod() {}
            }
        """.trimIndent())

        val directory = testFile.containingDirectory

        val method = action.javaClass.getDeclaredMethod("executeAnalysis", PsiDirectory::class.java)
        method.isAccessible = true
        val result = method.invoke(action, directory) as AnalysisResult

        assertNotNull(result)
    }

    private fun createActionEvent(directory: PsiDirectory): AnActionEvent {
        return TestActionEvent.createTestEvent { dataId ->
            when (dataId) {
                CommonDataKeys.PROJECT.name -> project
                CommonDataKeys.PSI_ELEMENT.name -> directory
                else -> null
            }
        }
    }

    private fun getToolWindow(): ToolWindow? {
        return ToolWindowManager.getInstance(project).getToolWindow("Named Elements Analysis")
    }
}
