package com.github.stephanjames.namedelements

import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NamedElementsAnalysisToolWindowFactoryTest : BasePlatformTestCase() {

    private lateinit var factory: NamedElementsAnalysisToolWindowFactory

    override fun setUp() {
        super.setUp()
        factory = NamedElementsAnalysisToolWindowFactory()
    }

    fun testCreateToolWindowContent() {
        val toolWindow = project.getService(ToolWindowManager::class.java)
            .registerToolWindow("Test", true, ToolWindowAnchor.BOTTOM)

        factory.createToolWindowContent(project, toolWindow)

        val contentManager = toolWindow.contentManager
        assertEquals(1, contentManager.contentCount)

        val content = contentManager.getContent(0)
        assertNotNull(content)
        assertEquals(Bundle.message("default.title"), content?.displayName)
        assertFalse(content?.isPinned ?: true)
        assertNotNull(content?.component)
        assertTrue(content?.component is javax.swing.JPanel)
    }

    fun testMultipleToolWindowCreation() {
        val toolWindow1 = project.getService(ToolWindowManager::class.java)
            .registerToolWindow("Test1", true, ToolWindowAnchor.BOTTOM)
        val toolWindow2 = project.getService(ToolWindowManager::class.java)
            .registerToolWindow("Test2", true, ToolWindowAnchor.LEFT)

        factory.createToolWindowContent(project, toolWindow1)
        factory.createToolWindowContent(project, toolWindow2)

        assertEquals(1, toolWindow1.contentManager.contentCount)
        assertEquals(1, toolWindow2.contentManager.contentCount)
    }

    fun testContentManagerIntegration() {
        val toolWindow = project.getService(ToolWindowManager::class.java)
            .registerToolWindow("Test", true, ToolWindowAnchor.BOTTOM)

        val contentManager = toolWindow.contentManager
        val initialContentCount = contentManager.contentCount

        factory.createToolWindowContent(project, toolWindow)

        assertEquals(initialContentCount + 1, contentManager.contentCount)
    }
}
