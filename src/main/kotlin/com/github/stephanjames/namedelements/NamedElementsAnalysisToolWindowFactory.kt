package com.github.stephanjames.namedelements

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class NamedElementsAnalysisToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.contentManager.addContent(defaultContent())
    }

    private fun defaultContent(): Content = ContentFactory.getInstance().createContent(
        createNamedElementsPanel(emptyList()),
        Bundle.message("default.title"),
        false
    )

}
