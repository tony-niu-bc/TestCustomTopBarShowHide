package com.wzhnsc.testcustomtopbarshowhide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.util.Locale;

public class MainActivity extends Activity {

    private RelativeLayout mrlTopBar;
    private MyWebView      mWebView;

    private float mImageAlpha = 1.0f;
    private float mWillAddAlpha = 0.1f;

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        // True if the host application wants to leave the current WebView and handle the url itself,
        // otherwise return false.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }
    }

    private static int mLastAlphaValue = 255;
    private static int mAlphaValue = 255;
    private void titleBarShowOrHide(boolean isUp, int shiftValue) {
        String strLog = String.format(Locale.getDefault(),
                "titleBarShowOrHide - mWebView = %s",
                mWebView.toString());

        Log.w("wzhnsc", strLog);

        if (null != mWebView
         && mWebView.canGoBack()) {
            return;
        }

        mAlphaValue = 255 - shiftValue;

        mAlphaValue = (0 > mAlphaValue) ? 0 : mAlphaValue;

        strLog = String.format(Locale.getDefault(),
                "titleBarShowOrHide(isUp = %b, shiftValue = %d), mAlphaValue = %d",
                isUp, shiftValue, mAlphaValue);

        Log.w("wzhnsc", strLog);

        if (mLastAlphaValue != mAlphaValue) {
            if (isUp) {
                if (0 == mAlphaValue) {
                    mrlTopBar.setVisibility(View.GONE);
                }
            }
            else {
                mrlTopBar.setVisibility(View.VISIBLE);
            }

            mrlTopBar.setAlpha(mImageAlpha);

            getWindow().getDecorView().postInvalidate();
        }

        mLastAlphaValue = mAlphaValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mrlTopBar = (RelativeLayout)findViewById(R.id.rl_top_bar);

        mWebView = (MyWebView)findViewById(R.id.web_view);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setBackgroundColor(Color.WHITE);
        mWebView.setScrollChangedListener(new MyWebView.WebViewScroll() {
            @Override
            public void dealScrollChanged(int l, int t, int oldl, int oldt) {
                String strLog = String.format(Locale.getDefault(),
                                              "dealScrollChanged(l = %d, t = %d, oldl = %d, oldt = %d)",
                                              l, t, oldl, oldt);

                Log.w("wzhnsc", strLog);

                // 纵向：正值向上滑动，负值向下滑动
                titleBarShowOrHide(((t - oldt) > 0), t);
            }
        });

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("https://github.com/wzhnsc");

        WebSettings webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (null != mWebView) {
            mWebView.onPause();
        }
    }
}
