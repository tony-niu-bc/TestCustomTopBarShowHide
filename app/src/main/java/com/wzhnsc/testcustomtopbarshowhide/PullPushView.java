package com.wzhnsc.testcustomtopbarshowhide;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class PullPushView extends LinearLayout {
    // 用于完成滚动操作的实例
    private Scroller mScroller;

    // 拖动的最小移动像素数
    //private int mTouchSlop;

    private RelativeLayout mrlTopBar;
    private MyWebView      mWebView;

    public PullPushView(Context context) {
        super(context);
        init(context);
    }

    public PullPushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullPushView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private int mChangeDirection = 0; // 0 - 无状态，1 - 向上移动过，2 - 向下移动过
    private void init(Context context) {
        // 创建Scroller的实例
        mScroller = new Scroller(context);

        // 获取拖动的最小移动像素数值
        //ViewConfiguration configuration = ViewConfiguration.get(context);
        //mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        // 加载布局
        View contentView = inflate(context, R.layout.layout_pull_push, this);

        mrlTopBar = (RelativeLayout)contentView.findViewById(R.id.rl_top_bar);

        mWebView = (MyWebView)contentView.findViewById(R.id.web_view);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setBackgroundColor(Color.WHITE);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        mWebView.setScrollChangedListener(new MyWebView.WebViewScroll() {
            @Override
            public void dealScrollChanged(int l, int t, int oldl, int oldt) {
                String strLog = String.format(Locale.getDefault(),
                        "dealScrollChanged(l = %d, t = %d, oldl = %d, oldt = %d), mChangeDirection = %d, mrlTopBar.getHeight() =%d",
                        l, t, oldl, oldt, mChangeDirection, mrlTopBar.getHeight());

                Log.w("wzhnsc", strLog);

                if ((t > (mrlTopBar.getHeight() / 2))
                 && (1 != mChangeDirection)) {
                    Log.w("wzhnsc", "mChangeDirection = 1");

                    mChangeDirection = 1;

                    mScroller.startScroll(0, 0, 0, mrlTopBar.getHeight() * -1, 200);
                    // 调用invalidate（）请求重绘，这就是View系统重绘的源头，即scroll动态效果的触发者
                    // 父容器(ViewGroup)调用dispatchDraw函数，其中会对它的每个孩子调用drawChild函数，
                    // 在drawChild函数中会调用computeScroll方法。
                    invalidate();
                }
                else if ((t < (mrlTopBar.getHeight() / 2))
                      && (2 != mChangeDirection)
                      && (0 != mChangeDirection)  ) {
                    Log.w("wzhnsc", "mChangeDirection = 2");

                    mChangeDirection = 2;

                    mScroller.startScroll(0, mrlTopBar.getHeight() * -1, 0, mrlTopBar.getHeight(), 200);

                    mrlTopBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mWebView.loadUrl("https://github.com/wzhnsc");
    }

    @Override
    public void computeScroll() {
        String strLog = String.format(Locale.getDefault(),
                "computeScroll - mScroller.computeScrollOffset() = %b, mScroller.getCurrX() = %d, mScroller.getCurrY() = %d",
                mScroller.computeScrollOffset(), mScroller.getCurrX(), mScroller.getCurrY());

        Log.w("wzhnsc", strLog);

        // 在此完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            if ((1 == mChangeDirection)
             && (Math.abs(mScroller.getCurrY()) >= mrlTopBar.getHeight())) {
                mrlTopBar.setVisibility(View.GONE);
                scrollTo(0, 0);
                mScroller.abortAnimation();
            }

            if ((2 == mChangeDirection)
             && (0 == mScroller.getCurrY())) {
                mScroller.abortAnimation();
            }

            invalidate();
        }
    }
}
