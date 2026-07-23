# WebView JavascriptInterface 브리지 보존
-keepclassmembers class com.samguk.app.MainActivity$NativeBridge {
    public *;
}
-keepattributes JavascriptInterface
