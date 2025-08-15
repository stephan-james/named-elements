package com.github.stephanjames.namedelements

import com.intellij.lang.Language
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType

data class Location(
    val path: String,
    val lineNumber: Int,
    val columnNumber: Int
)

data class NamedElement(
    val location: Location,
    val element: PsiNamedElement
)

data class NamedElements(
    val category: IElementType,
    val name: String,
    var elements: MutableCollection<NamedElement>
)

typealias AnalysisResult = Map<Language, Map<Pair<IElementType, String>, NamedElements>>
