package com.example.annotatedtextexperiment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.annotatedtextexperiment.ui.theme.AnnotatedTextExperimentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnnotatedTextExperimentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val html = "<a href='https://hoge.jp' class='link'>https://hoge.jp</a><br /><span class='mention'>@山田太郎</span> <img class=\"emoji\" alt=\"+1\" title=\"+1\" src=\"https://hoge.jp/image/thumbsup.png\" /> <img class=\"emoji\" alt=\"smile\" title=\"smile\" src=\"https://hoge.jp/image/smile.png\" /> "
    val htmlRenderer = HTMLToAnnotatedConverter.createDefault(
        Color.Blue,
        Color.LightGray,
    )
    val attributedString = htmlRenderer.convert(html, 24)

    val uriHandler = LocalUriHandler.current

    Column {

        ClickableText(
            text = attributedString,
            onClick = { pos ->
                // クリックされた箇所からAnnotationを取得
                val annotation =
                    attributedString.getStringAnnotations(start = pos, end = pos).firstOrNull()
                annotation?.let { range ->
                    // クリックされた箇所のURLを開く
                    // pushStringAnnotationで設定した情報が取得できる
                    uriHandler.openUri(range.item) // UriHandlerを使ってブラウザを開く
                }
            },
        )

        Text(
            text = attributedString,
            inlineContent = mapOf(
                "+1" to InlineTextContent(
                    Placeholder(
                        width = 24.sp,
                        height = 24.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                    ),
                    children = {
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "thumbsup")
                    }
                ),
                "smile" to InlineTextContent(
                    Placeholder(
                        width = 24.sp,
                        height = 24.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                    ),
                    children = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "smile")
                    }
                ),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnnotatedTextExperimentTheme {
        Greeting()
    }
}