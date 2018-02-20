package com.p82018.sw806f18.p8androidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    WebView hubView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        hubView = (WebView)findViewById(R.id.hub_view);
        hubView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                hubView.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        WebSettings hubSettings = hubView.getSettings();
        hubSettings.setJavaScriptEnabled(true);
        String summary =
                "<!DOCTYPE html>" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<a href=\"http://www.google.com\"> Open Google</a> " +
                "<p id=\"demo\"></p>\n" +
                "<input type=\"submit\" value=\"Submit\" onClick=\"f();\">\n" +
                "<script>\n" +
                        "function f(){window.location = \"http://www.google.com\"}\n" +
                "\n" +
                "</script> \n" +
                "\n" +
                "</body>\n" +
                "</html>";
        //hubView.loadUrl("https://stackoverflow.com/questions/7305089/how-to-load-external-webpage-inside-webview");
        hubView.loadData(summary, "text/html", null);
        hubView.getUrl();
    }

    @Override
    public void onBackPressed() {
        if(hubView.canGoBack())
        {
            hubView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
