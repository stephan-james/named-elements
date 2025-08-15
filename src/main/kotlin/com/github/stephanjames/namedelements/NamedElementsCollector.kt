package com.github.stephanjames.namedelements

import com.github.stephanjames.extensions.addIfNoneMatch
import com.intellij.lang.Language
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType

class NamedElementsCollector : PsiRecursiveElementWalkingVisitor() {

    private val _analysisResult = linkedMapOf<Language, MutableMap<Pair<IElementType, String>, NamedElements>>()

    val analysisResult: AnalysisResult
        get() = _analysisResult.mapValues { (_, v) -> v }

    private val unknownElementTypes = mutableMapOf<Language, IElementType>()

    override fun visitElement(element: PsiElement) {
        if (element is PsiNamedElement) {
            addNamedElement(element)
        }
        super.visitElement(element)
    }

    private fun addNamedElement(element: PsiNamedElement) {
        val name = element.name ?: return

        addNamedElement(element, name, Language.ANY)
        addNamedElement(element, name, element.language)
    }

    private fun addNamedElement(element: PsiNamedElement, name: String, language: Language) {
        val elementType = element.elementType ?: unknownElementTypes.getOrPut(language) {
            IElementType("Unknown", language)
        }

        namedElementFrom(element)?.let { namedElement ->
            _analysisResult
                .getOrPut(language) { linkedMapOf() }
                .getOrPut(elementType to name) { NamedElements(elementType, name, mutableSetOf()) }
                .elements.addIfNoneMatch(namedElement) { it.location == namedElement.location }
        }
    }

    private fun namedElementFrom(element: PsiNamedElement): NamedElement? {
        val virtualFile = element.containingFile?.virtualFile ?: return null
        val path = virtualFile.path.removePrefix(element.project.basePath ?: "")

        val document = FileDocumentManager.getInstance().getDocument(virtualFile)
        val offset = element.textOffset

        val lineNumber = document?.getLineNumber(offset) ?: -1

        val columnNumber = if (lineNumber >= 0) {
            document?.getLineStartOffset(lineNumber)?.let { offset - it } ?: -1
        } else {
            -1
        }

        val location = Location(path, lineNumber, columnNumber)

        return NamedElement(location, element)
    }

}
