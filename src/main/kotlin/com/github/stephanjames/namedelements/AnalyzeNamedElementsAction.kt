package com.github.stephanjames.namedelements

import com.github.stephanjames.extensions.replaceContents
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiDirectory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class AnalyzeNamedElementsAction : AnAction() {

    companion object {
        private const val TOOL_WINDOW_ID = "Named Elements Analysis"
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val directory = event.getData(CommonDataKeys.PSI_ELEMENT) as? PsiDirectory ?: return

        ProgressManager.getInstance().run(object : Task.Backgroundable(
            project,
            Bundle.message("process.title"),
            false
        ) {

            private lateinit var analysisResult: AnalysisResult

            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = Bundle.message("process.indicator.text")
                indicator.text2 = Bundle.message("process.indicator.text2")

                analysisResult = executeAnalysis(directory)
            }

            override fun onSuccess() {
                showNamedElementsAnalysis(project, analysisResult)
            }
        })
    }

    private fun showNamedElementsAnalysis(project: Project, analysisResult: AnalysisResult) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID) ?: return
        val contents = createContentTabs(analysisResult)

        toolWindow.contentManager.replaceContents(contents)
        toolWindow.activate(null, true)
    }

    private fun executeAnalysis(directory: PsiDirectory) =
        getNamedElementsCollectorRunner().execute(directory)

    private fun getNamedElementsCollectorRunner() =
        ApplicationManager.getApplication().getService(NamedElementsCollectorRunner::class.java)

    private fun createContentTabs(analysisResult: AnalysisResult) =
        analysisResult
            .toSortedMap(compareBy { it.displayName })
            .filterValues { it.values.isNotEmpty() }
            .map { (language, entries) -> createContentTabForLanguage(language, entries.values.toList()) }

    private fun createContentTabForLanguage(language: Language, namedElements: List<NamedElements>): Content {
        val tabTitle = language.displayName.ifBlank { Bundle.message("default.title") }
        val panel = createNamedElementsPanel(namedElements)
        return ContentFactory.getInstance().createContent(panel, tabTitle, false)
    }

}
