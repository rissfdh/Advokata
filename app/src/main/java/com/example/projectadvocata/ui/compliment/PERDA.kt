package com.example.projectadvocata.ui.compliment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectadvocata.R

class PERDA : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perda)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "PERDA"

        val webView = findViewById<WebView>(R.id.webViewPERDA)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
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
