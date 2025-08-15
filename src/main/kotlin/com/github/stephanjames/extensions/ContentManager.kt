package com.github.stephanjames.extensions

import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentManager

fun ContentManager.addContents(contents: List<Content>) {
    contents.forEach { content ->
        if (content.isValid) {
            removeContent(content, false)
        }
        addContent(content)
    }
}

fun ContentManager.replaceContents(contents: List<Content>) {
    removeAllContents(true)
    addContents(contents)
}
