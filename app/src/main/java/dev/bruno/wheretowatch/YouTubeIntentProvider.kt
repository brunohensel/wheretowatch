package dev.bruno.wheretowatch

import android.content.Intent
import android.net.Uri

object YouTubeIntentProvider {
    private const val YOUTUBE_PACKAGE = "com.google.android.youtube"
    private const val YOUTUBE_URL = "https://www.youtube.com/watch?v=%s"

    fun get(key: String) = Intent(Intent.ACTION_VIEW).apply {
        setPackage(YOUTUBE_PACKAGE)
        setData(Uri.parse(YOUTUBE_URL.format(key)))
    }
}
