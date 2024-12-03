package com.example.projectadvocata.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.R

class UUD : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uud)

        val webView = findViewById<WebView>(R.id.webViewUUD)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:alert('Web UUD 1945 Berhasil di muat')")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@UUD, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }
//        webView.loadUrl("https://jdih.kemenkeu.go.id/fulltext/1945/UUDTAHUN~1945UUD.HTM")
        webView.loadUrl("https://www.hukumonline.com/pusatdata/detail/lt4ca2eb6dd2834/undang-undang-dasar-1945/")
    }

}