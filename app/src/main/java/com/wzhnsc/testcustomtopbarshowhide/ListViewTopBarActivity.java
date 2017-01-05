package com.wzhnsc.testcustomtopbarshowhide;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewTopBarActivity extends AppCompatActivity {
    private TextView mtvTitle;
    private float mtvTitleHeight = 0;

    private ListView mlvData;

    private boolean mIsShowingTitle = true;
    private boolean mIsSlideDown    = true;

    // 获取标题数据列表
    private List<String> getTitleDatas() {
        List<String> titleArray = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            titleArray.add(String.format(Locale.getDefault(), "这是第 %02d 项", i));
        }

        return titleArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview_topbar);

        mtvTitle = (TextView)findViewById(R.id.tv_title_bar);

        mlvData = (ListView)findViewById(R.id.lv_data);
        mlvData.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getTitleDatas()));

        mlvData.setOnTouchListener(new View.OnTouchListener() {
            private float lastX;
            private float lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    lastY = event.getY();

                    break;

                case MotionEvent.ACTION_MOVE:
                    float x = event.getX();
                    float y = event.getY();

                    float xGapDistance = Math.abs(x - lastX);
                    float yGapDistance = Math.abs(y - lastY);

                    // 检测是否向下滑动，true - 向下，false - 向上
                    mIsSlideDown = y - lastY > 5;

                    lastX = x;
                    lastY = y;

                    // 滑动合法的情况下，才判断是否显隐
                    if ((yGapDistance > 8) && (xGapDistance < 8)) {
                        titleShowOrHide(500);
                    }

                    break;

                default:
                    break;
                }

                return false;
            }
        });

        mtvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("SlideShowHideHF", "postDelayed - mtvTitleHeight = " + mtvTitleHeight);
                mtvTitleHeight = mtvTitle.getHeight();
            }
        }, 500);
    }

    private void titleShowOrHide(int duration) {
        boolean isShow;

        Log.e("SlideShowHideHF",
              "titleShowOrHide - mIsShowingTitle = " + mIsShowingTitle +
              " mIsSlideDown = " + mIsSlideDown);

        // 隐蔽标题时，且是向下的，就显示
        if (!mIsShowingTitle && mIsSlideDown) {
            // 显示此标题
            isShow = true;
        }
        // 显示标题时，且是向上的，就隐蔽
        else if (mIsShowingTitle && !mIsSlideDown) {
            // 隐蔽标题
            isShow = false;
        }
        else {
            return;
        }

        Log.e("SlideShowHideHF", "titleShowOrHide - isShow = " + isShow);

        if (isShow) {
            ObjectAnimator oa = ObjectAnimator.ofFloat(mtvTitle, "y", -mtvTitleHeight, 0f);
            oa.setDuration(duration);
            oa.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mlvData.setPadding(mlvData.getPaddingLeft(),
                                       (int)mtvTitleHeight,
                                       mlvData.getPaddingRight(),
                                       mlvData.getPaddingBottom());
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            oa.start();
        }
        else { // 把标题隐藏
            ObjectAnimator oa = ObjectAnimator.ofFloat(mtvTitle, "y", 0f, -mtvTitleHeight);
            oa.setDuration(duration);
            oa.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mlvData.setPadding(mlvData.getPaddingLeft(),
                                       0,
                                       mlvData.getPaddingRight(),
                                       mlvData.getPaddingBottom());
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            oa.start();
        }

        mIsShowingTitle = isShow;
    }
}
