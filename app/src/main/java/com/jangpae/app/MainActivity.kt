package com.jangpae.app

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jangpae.app.databinding.ActivityMainBinding

/**
 * 무장쟁패: 패왕의 궤적 — A형 WebView 셸 (영찬영하 Daddy / J패밀리)
 * 로직/UI 전부 assets/index.html. Kotlin은 얇은 셸.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val web: WebView = binding.webview
        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            allowContentAccess = true
        }
        // 게임 렌더 성능: 하드웨어 가속 레이어
        web.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        web.webViewClient = WebViewClient()
        web.addJavascriptInterface(NativeBridge(), "Android")
        web.loadUrl("file:///android_asset/index.html")
    }

    /** 게임 → 네이티브 브리지 (진동/토스트 등 확장 지점) */
    inner class NativeBridge {
        @JavascriptInterface
        fun toast(msg: String) {
            runOnUiThread { Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show() }
        }

        @JavascriptInterface
        fun appInfo(): String = "무장쟁패 v${BuildConfig.VERSION_NAME} · 영찬영하 Daddy · J패밀리"
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    private fun hideSystemBars() {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            )
    }

    override fun onPause() {
        super.onPause()
        binding.webview.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
    }
}
