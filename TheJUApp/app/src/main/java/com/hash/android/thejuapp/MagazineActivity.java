package com.hash.android.thejuapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MagazineActivity extends AppCompatActivity {

    private ProgressBar progressbar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);

        progressbar = (ProgressBar) findViewById(R.id.progressBar2);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

        String url = getIntent().getStringExtra("DOWNLOAD_URL");

        Log.d("url", url);
        if (url == null) {
            onBackPressed();
            finish();
        } else {
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    progressbar.setVisibility(View.GONE);
                }
            });
        }
    }


}
