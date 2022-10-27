package com.example.annotatedtextexperiment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import org.jsoup.Jsoup
import org.jsoup.nodes.Node

class HTMLToAnnotatedConverter(
    private val nodeHandlers: List<NodeHandler>
) {

    companion object {
        fun createDefault(
            mentionTextColor: Color,
            mentionBackgroundColor: Color,
        ): HTMLToAnnotatedConverter {
            return HTMLToAnnotatedConverter(
                listOf(
                    TextHandler(),
                    BreakHandler(),
                    URLLinkHandler(),
                    MentionHandler(mentionTextColor, mentionBackgroundColor),
                    EmojiHandler(),
                )
            )
        }
    }

    interface NodeHandler {
        fun handleNode(node: Node, builder: AnnotatedString.Builder, fontSizePx: Int)
    }

    fun convert(html: String, fontSizePx: Int): AnnotatedString {
        return Jsoup.parse(html).body().let { body ->
            convertRecursively(body, fontSizePx)
        }
    }

    // TODO: - 絵文字のみの場合は後で対応する
//    fun modifyEmojiSizeIfEmojiOnly(annotatedString: AnnotatedString, size: Int): Boolean {
//        if (checkEmojiOnly(annotatedString)) {
//            val imageAnnotated = annotatedString.getStringAnnotations(0, annotatedString.length, )
//        }
//    }

    // Private

    private fun convertRecursively(node: Node, fontSizePx: Int): AnnotatedString {
        val builder = AnnotatedString.Builder()
        node.childNodes().forEach { childNode ->
            val annotatedString = convertRecursively(childNode, fontSizePx)
            builder.append(annotatedString)
        }
        nodeHandlers.forEach { nodeHandler ->
            nodeHandler.handleNode(node, builder, fontSizePx)
        }
        return builder.toAnnotatedString()
    }

    private fun checkEmojiOnly(annotatedString: AnnotatedString): Boolean {
        if (annotatedString.isBlank()) {
            return false
        }

        for (element in annotatedString) {
            if (Character.isWhitespace(element)) continue
            if (element != EmojiHandler.EMOJI_REPLACEMENT) {
                return false
            }
        }
        return true
    }
}