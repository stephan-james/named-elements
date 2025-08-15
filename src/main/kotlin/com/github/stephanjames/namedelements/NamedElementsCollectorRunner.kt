package com.github.stephanjames.namedelements

import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory

@Service
class NamedElementsCollectorRunner {

    fun execute(directory: PsiDirectory): AnalysisResult {
        val collector = NamedElementsCollector()
        val manager = directory.manager

        ReadAction.run<Throwable> {
            VfsUtilCore.iterateChildrenRecursively(directory.virtualFile, null) { file ->
                if (shouldProcessFile(file)) {
                    manager.findFile(file)?.accept(collector)
                }
                true
            }
        }

        return collector.analysisResult
    }

    private fun shouldProcessFile(file: VirtualFile): Boolean {
        if (file.isDirectory) return false
        if (file.fileType.isBinary) return false
        return true
    }


}
