package com.example.annotatedtextexperiment

import android.content.Context

class EmojiConverter(
    private val context: Context
) {


    // Private

    private fun resolveEmojiCode(code: String?): String? {
        return code?.let { aliasMap[it] ?: it }
    }

    private val aliasMap: HashMap<String, String> = hashMapOf(
        Pair("+1", "thumbsup"),
        Pair("bowing_man", "bow"),
        Pair("no_good_woman", "no_good"),
        Pair("raising_hand_woman", "raising_hand"),
        Pair("fist_oncoming", "punch"),
        Pair("-1", "thumbsdown"),
        Pair("hankey", "poop"),
        Pair("running_man", "runner")
    )
}