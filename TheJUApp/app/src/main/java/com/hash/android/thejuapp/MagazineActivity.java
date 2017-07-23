package com.hash.android.thejuapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

//        final String mimeType = "text/html";
//        final String encoding = "UTF-8";
//        String html = "<html><head><script type=\"text/javascript\" src=\"//e.issuu.com/embed.js\" async=\"true\"></script></head><body><div data-configid=\"26055985/46049093\" style=\"width:100%; height:490px;\" class=\"issuuembed\"></div></body></html>";
//            String html = "<html><body>Hello world</body></html>";

        String filename = getIntent().getStringExtra("DOWNLOAD_URL");
        Log.d("url", filename);
        if (filename == null) {
            onBackPressed();
            finish();
        } else {

            String doc = "<iframe src='http://docs.google.com/gview?embedded=true&url=" + filename + "' width='100%' height='100%' style='border: none;'></iframe>";
//            Log.d("url", filename);

            webView.loadData(doc, "text/html", "UTF-8");
//https://docs.google.com/gview?embedded=true&url=https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/journal_newsletters%2F4th%20Edition.pdf
            webView.setWebViewClient(new WebViewClient() {


                public void onPageFinished(WebView view, String url) {
                    // do your stuff here
//                progressbar.setVisibility(View.GONE);
                }
            });
        }
    }


}
