package com.wzhnsc.testcustomtopbarshowhide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private RelativeLayout mrlTopBar;
    private MyWebView      mWebView;

    // 操作栏渐变处理标识
    private boolean bIsUpdate = true;

    // 现在网页向下滑动标识
    private boolean bIsNowGoDown = false;
    // 之前网页向下滑动标识
    private boolean bIsPreGoDown = false;

    private float mImageAlpha = 1.0f;
    private float mWillAddAlpha = 0.1f;

    private Handler mHandler;

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

        @Override
        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private void titleBarShowOrHide(boolean IsVisible) {
        if (null != mWebView
         && mWebView.canGoBack()) {
            return;
        }

        if (IsVisible) {
            mWillAddAlpha = 0.1f;
            mrlTopBar.setVisibility(View.VISIBLE);
        }
        else {
            mWillAddAlpha = -0.1f;
        }

        bIsUpdate = false;

        updateAlpha();
    }

    public void updateAlpha() {
        if (bIsUpdate) {
            return;
        }

        mImageAlpha += mWillAddAlpha;

        if (mImageAlpha > 1.0f) {
            mImageAlpha = 1.0f;
        }

        if (mImageAlpha < 0.0f) {
            mImageAlpha = 0.0f;
        }

        if (null != mHandler) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mrlTopBar.setAlpha(mImageAlpha);

                    if (mImageAlpha == 0) {
                        mrlTopBar.setVisibility(View.GONE);
                        bIsUpdate = true;
                    }

                    if (mImageAlpha == 255) {
                        bIsUpdate = true;
                    }

                    updateAlpha();
                }

            }, 80);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mHandler = new Handler();

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
                if (t > oldt) {
                    bIsPreGoDown = bIsNowGoDown;
                    bIsNowGoDown = true;
                } else {
                    bIsPreGoDown = bIsNowGoDown;
                    bIsNowGoDown = false;
                }

                if (bIsPreGoDown != bIsNowGoDown) {
                    titleBarShowOrHide(!bIsNowGoDown);
                }
            }
        });
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("http://www.lagou.com");

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
