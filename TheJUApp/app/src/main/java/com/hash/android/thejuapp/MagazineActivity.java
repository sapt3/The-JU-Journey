package com.hash.android.thejuapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MagazineActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = "<html><head><script type=\"text/javascript\" src=\"//e.issuu.com/embed.js\" async=\"true\"></script></head><body><div data-configid=\"26055985/46049093\" style=\"width:100%; height:490px;\" class=\"issuuembed\"></div></body></html>";
//            String html = "<html><body>Hello world</body></html>";
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

}
