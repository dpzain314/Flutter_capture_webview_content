package com.example.recap_weview

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.OutputStream

class RecapActivity : AppCompatActivity() {

    private lateinit var recapURL: String

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recap)

        recapURL = intent.getStringExtra("recapURL") ?: "";

        val download = findViewById<ImageView>(R.id.download)
        val webView = findViewById<WebView>(R.id.wv_recap)

        download.setOnClickListener {
            finish()
        }

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JSHandler(this@RecapActivity, webView), "Android")

//        webView.loadUrl(recapURL)
        webView.loadData(recapURL, "text/html", "UTF-8")
    }


}

class JSHandler(val context: Context, val webview: WebView) {
    @JavascriptInterface
    fun captureScreen(message: String) {
        val webViewBitmap = captureWebView(webview)
        saveBitmapToGallery(context, webViewBitmap)
    }

    private fun captureWebView(webView: WebView): Bitmap {
        webView.isScrollbarFadingEnabled = true
        webView.measure(
            View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val bitmap = Bitmap.createBitmap(webView.measuredWidth, webView.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        canvas.drawBitmap(bitmap, 0f, bitmap.height.toFloat(), paint)
        webView.draw(canvas)
        return bitmap;
    }

    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
        val epochTime = System.currentTimeMillis() / 1000

        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, "kiotVietRecap$epochTime.png")

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "KiotvietRecap$epochTime")
            put(MediaStore.Images.Media.DESCRIPTION, "KiotvietRecap$epochTime")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        var imageUri: Any? = null
        val resolver = context.contentResolver

        try {
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream: OutputStream? = imageUri?.let { resolver.openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream?.flush()
            outputStream?.close()

            Toast.makeText(context, "Ảnh đã được lưu vào thư viện ảnh", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show()
        } finally {
            // Make sure to revoke the access to the image URI after saving
            if (imageUri is android.net.Uri) {
                resolver.notifyChange(imageUri, null)
            }
        }
    }
}