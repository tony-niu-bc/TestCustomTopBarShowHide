package com.wzhnsc.testcustomtopbarshowhide;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MyWebView extends WebView {

    public interface WebViewScroll {
        public void dealScrollChanged(int l, int t, int oldl, int oldt);
    }

    private WebViewScroll mWebViewScroll;

    public MyWebView(Context context) {
        super(context);
    }

    // 没有如下两个构造函数报错：
    // java.lang.NoSuchMethodException:
    // <init> [class android.content.Context, interface android.util.AttributeSet]
    // android.view.InflateException:
    // Binary XML file line #45:
    // Error inflating class com.wzhnsc.testcustomtopbarshowhide.MyWebView
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollChangedListener(WebViewScroll wvScroll) {
        mWebViewScroll = wvScroll;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        mWebViewScroll.dealScrollChanged(l, t, oldl, oldt);
    }
}
