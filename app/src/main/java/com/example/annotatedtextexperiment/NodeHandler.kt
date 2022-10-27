package com.example.annotatedtextexperiment

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.util.*

class TextHandler : HTMLToAnnotatedConverter.NodeHandler {
    override fun handleNode(node: Node, builder: AnnotatedString.Builder, fontSizePx: Int) {
        if (node is TextNode) {
            builder.append(node.text())
        }
    }
}

class BreakHandler : HTMLToAnnotatedConverter.NodeHandler {
    override fun handleNode(node: Node, builder: AnnotatedString.Builder, fontSizePx: Int) {
        if (node is Element && node.tagName() == "br") {
            builder.append("\n")
        }
    }
}

class URLLinkHandler : HTMLToAnnotatedConverter.NodeHandler {
    override fun handleNode(node: Node, builder: AnnotatedString.Builder, fontSizePx: Int) {
        if (node is Element && node.tagName() == "a") {
            val url = node.attr("href")
            builder.addStyle(
                SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ), 0, url.length
            )
        }
    }
}

class MentionHandler(
    private val textColor: Color,
    private val backgroundColor: Color,
) : HTMLToAnnotatedConverter.NodeHandler {
    override fun handleNode(node: Node, builder: AnnotatedString.Builder, fontSizePx: Int) {
        if (node is Element && node.tagName() == "span" && node.attr("class")
                .lowercase(Locale.getDefault()) == "mention"
        ) {
            builder.addStyle(
                SpanStyle(
                    color = textColor,
                    background = backgroundColor,
                ), 0, node.text().length
            )
        }
    }
}

class EmojiHandler(
//    private val converter: EmojiConverter,
) : HTMLToAnnotatedConverter.NodeHandler {

    companion object {
        const val EMOJI_REPLACEMENT: Char = '\uFFFC' // OBJECT REPLACEMENT CHARACTER
    }

    override fun handleNode(node: Node, builder: AnnotatedString.Builder, fontSizePx: Int) {
        if (node is Element && node.tagName() == "img" &&
            node.attr("class").lowercase(Locale.getDefault()) == "emoji"
        ) {
            val code = node.attr("title") ?: ""
            val emojiSize = fontSizePx * 1.25
            builder.appendInlineContent(code)
        }
    }

}