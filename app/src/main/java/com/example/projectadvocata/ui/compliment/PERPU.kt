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

class PERPU : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perpu)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "PERPU"
        val webView = findViewById<WebView>(R.id.webViewPERPU)
        webView.settings.javaScriptEnabled = true

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        progressBar.visibility = View.VISIBLE

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@PERPU, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }
        webView.loadUrl("https://search.hukumonline.com/search/regulations?p=0&q=&l=10&o=desc&s=date&language=&hierarchy=keputusan+dewan%2Csurat+edaran+komisi%2Cperaturan+komisi%2Ckeputusan+komisi%2Csurat+direktur%2Csurat+edaran+direktur%2Cpengumuman+direktur%2Cperaturan+direktur%2Ckeputusan+direktur%2Csurat+direksi%2Csurat+edaran+direksi%2Cperaturan+direksi%2Ckeputusan+direksi%2Csurat+lembaga%2Fbadan%2Csurat+edaran+lembaga%2Fbadan%2Cpengumuman+lembaga%2Fbadan%2Cinstruksi+lembaga%2Fbadan%2Cperaturan+lembaga%2Fbadan%2Ckeputusan+lembaga%2Fbadan%2Csurat+kepala%2Fketua+lembaga%2Fbadan%2Csurat+edaran+kepala%2Fketua+lembaga%2Fbadan%2Cinstruksi+kepala%2Fketua+lembaga%2Fbadan%2Cperaturan+kepala%2Fketua+lembaga%2Fbadan%2Ckeputusan+kepala%2Fketua+lembaga%2Fbadan%2Cperaturan+inspektur+jenderal%2Csurat+direktur+jenderal%2Csurat+edaran+direktur+jenderal%2Cinstruksi+direktur+jenderal%2Cperaturan+direktur+jenderal%2Ckeputusan+direktur+jenderal%2Csurat+asisten+menteri%2Csurat+menteri%2Csurat+edaran+menteri%2Cpengumuman+menteri%2Cinmen%2Cpermen%2Ckepmen%2Csurat+menteri+negara%2Fketua+lembaga%2Fbadan%2Csurat+edaran+menteri+negara%2Fketua+lembaga%2Fbadan%2Cpengumuman+menteri+negara%2Fketua+lembaga%2Fbadan%2Cinstruksi+menteri+negara%2Fketua+lembaga%2Fbadan%2Cperaturan+menteri+negara%2Fketua+lembaga%2Fbadan%2Ckeputusan+bersama%2Ckonvensi+internasional%2Cinpres%2Ckeppres%2Cperpres%2Cpp%2Cpenpres%2Cperpu%2Cundang-undang+darurat%2Cuu%2Ctus+mpr%2Ctap+mpr&legal_status=&consolidated=&year=")
    }

}