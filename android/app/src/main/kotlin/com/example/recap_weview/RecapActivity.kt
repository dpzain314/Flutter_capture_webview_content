package com.example.recap_weview

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.OutputStream

class RecapActivity : AppCompatActivity() {
    private val url =
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "   <head>\n" +
                "      <style>\n" +
                "        body {\n" +
                "          font-size: 13px;\n" +
                "          font-family: sans-serif;\n" +
                "          font-weight: 500;\n" +
                "        }\n" +
                "\n" +
                "        .table {\n" +
                "          width:100%;\n" +
                "        }\n" +
                "\n" +
                "       \n" +
                "      </style>\n" +
                "   </head>\n" +
                "   <body>\n" +
                "      <span ><span><strong>KiotViet</strong></span></span><br /><span ><span>Chi nhánh: Trần Khát Chân</span></span><br /><span ><span>Địa chỉ: 434 Trần Khát Chân</span></span><br /><span ><span>Điện thoại: 1800 6162</span></span>\n" +
                "      <div style=\"border-bottom:1px dashed black; margin:5px 0\"></div>\n" +
                "      <div><span ><span>Ngày bán: 06/10/2016</span></span></div>\n" +
                "      <div style=\"text-align:center; margin:20px 0\"><span ><span><strong>MẪU IN THỬ</strong></span></span><br /><span ><span><strong>HD000001</strong></span></span></div>\n" +
                "      <div><span ><span><strong>Khách hàng: </strong>Anh Hòa Q1</span></span><br /><span ><span><strong>Người bán: </strong>Nguyễn Văn A</span></span></div>\n" +
                "      <div style=\"border-bottom:1px dashed black; margin:10px 0\"></div>\n" +
                "      <table class=\"table\">\n" +
                "         <tbody>\n" +
                "            <tr style=\"width:100%;\">\n" +
                "               <td style=\"width:35%;\"><strong><span ><span>Đơn giá</span></span></strong></td>\n" +
                "               <td style=\"text-align:center;width:30%;\"><strong><span ><span>SL</span></span></strong></td>\n" +
                "               <td style=\"text-align:right;\"><strong><span ><span>Thành tiền</span></span></strong></td>\n" +
                "            </tr>\n" +
                "            <tr style='width:100%;'>\n" +
                "               <td colspan=\"3\"><span ><span>Váy nữ Alcado (chiếc)</span></span></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td><span ><span>223,812</span></span></td>\n" +
                "               <td style=\"text-align:center;\"><span ><span>3</span></span></td>\n" +
                "               <td style=\"text-align:right;\"><span ><span>671,436</span></span></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td colspan=\"3\" style=\"border-bottom:1px dashed black;width:100%;\"></td>\n" +
                "            </tr>\n" +
                "            <tr style='width:100%;'>\n" +
                "               <td colspan=\"3\"><span ><span>Quần jeans nữ Blue Exchange (chiếc)</span></span></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td><span ><span>210,123</span></span></td>\n" +
                "               <td style=\"text-align:center;\"><span ><span>2</span></span></td>\n" +
                "               <td style=\"text-align:right;\"><span ><span>420,246</span></span></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td colspan=\"3\" style=\"border-bottom:1px dashed black;width:100%;\"></td>\n" +
                "            </tr>\n" +
                "         </tbody>\n" +
                "      </table>\n" +
                "      <p><span>Ghi chú: Đây là mẫu in thử</span></p>\n" +
                "      <table style=\"width:100%;\">\n" +
                "         <tbody>\n" +
                "            <tr>\n" +
                "               <td style=\"text-align:right;\"><span>Tổng tiền hàng:</span></td>\n" +
                "               <td style=\"text-align:right;\"><span>1,192,532</span></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td style=\"text-align:right;\"><span>Chiết khấu:</span></td>\n" +
                "               <td style=\"text-align:right;\"><span>100,000</span></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "               <td style=\"text-align:right;\"><span>Tổng cộng:</span></td>\n" +
                "               <td style=\"text-align:right;\"><strong>1,092,532</strong></td>\n" +
                "            </tr>\n" +
                "         </tbody>\n" +
                "      </table>\n" +
                " <button id=\"callbackButton\" onclick=\"sendCallback()\">Send Callback</button>" +
                "<script>\n" +
                "        function sendCallback() {\n" +
                "            // Check if the native interface is available\n" +
                "            if (window.webkit && window.webkit.messageHandlers) {\n" +
                "                // iOS\n" +
                "                window.webkit.messageHandlers.jsMessageHandler.postMessage(\"Callback from WebView\");\n" +
                "            } else if (window.Android) {\n" +
                "                // Android\n" +
                "                window.Android.captureScreen(\"Callback from WebView\");\n" +
                "            }\n" +
                "        }\n" +
                "    </script>" +
                "   </body>\n" +
                "</html>\n"

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recap)

        val download = findViewById<Button>(R.id.download)
        val webView = findViewById<WebView>(R.id.wv_recap)

        download.setOnClickListener {}

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JSHandler(this@RecapActivity, webView), "Android")

//        webView.loadUrl("https://www.instagram.com")
        webView.loadData(url, "text/html", "UTF-8")
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