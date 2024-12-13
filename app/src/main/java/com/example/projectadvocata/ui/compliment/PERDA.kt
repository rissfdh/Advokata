package com.example.projectadvocata.ui.compliment

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.R

class PERDA : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perda)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val webView = findViewById<WebView>(R.id.webViewPERDA)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: android.webkit.JsResult
            ): Boolean {
                Toast.makeText(this@PERDA, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }
        webView.loadUrl("https://search.hukumonline.com/search/regulations?p=0&q=&l=10&o=desc&s=date&language=&hierarchy=surat+bupati%2Fwalikota%2Csurat+edaran+bupati%2Fwalikota%2Cpengumuman+bupati%2Fwalikota%2Cinstruksi+bupati%2Fwalikota%2Cperaturan+bupati%2Fwalikota%2Ckeputusan+bupati%2Fwalikota%2Csurat+gubernur%2Csurat+edaran+gubernur%2Cinstruksi+gubernur%2Cperaturan+gubernur%2Ckeputusan+gubernur%2Cperaturan+daerah+tingkat+ii%2Cperaturan+daerah+tingkat+i&legal_status=&consolidated=&year=")
    }
}