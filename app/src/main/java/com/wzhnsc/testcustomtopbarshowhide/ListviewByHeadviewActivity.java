package com.wzhnsc.testcustomtopbarshowhide;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListviewByHeadviewActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private String[] mStr = new String[20];

    private int   mTouchSlop;

    private float mFirstY;
    private float mCurrentY;

    private boolean mShow = false; // true - 显示，false - 隐藏

    private ObjectAnimator mAnimator;

    private void toolbarAnim(boolean show) {
        if ((null != mAnimator)
         && (mAnimator.isRunning())) {
            mAnimator.cancel();
        }

        if (show) {
            mAnimator = ObjectAnimator.ofFloat(mToolbar, "translationY", mToolbar.getTranslationY(), 0);
        }
        else {
            mAnimator = ObjectAnimator.ofFloat(mToolbar, "translationY", mToolbar.getTranslationY(), -mToolbar.getHeight());
        }

        mAnimator.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview_by_headview);

        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        ListView lv = (ListView)findViewById(R.id.lv_data);

        View headView = new View(this);
        headView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                                                              (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
        lv.addHeaderView(headView);

        for (int i = 0; i < mStr.length; i++) {
            mStr[i] = "Item " + i;
        }

        lv.setAdapter(new ArrayAdapter<>(this,
                                         android.R.layout.simple_expandable_list_item_1,
                                         mStr));

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();

                        boolean up = false; // false - down, true - up

                        if ((mCurrentY - mFirstY) > mTouchSlop) {
                            up = false;
                        }
                        else if ((mFirstY - mCurrentY) > mTouchSlop) {
                            up = true;
                        }

                        if (up) {
                            if (mShow) { // 如果显示就隐藏
                                mShow = false;
                                toolbarAnim(false);
                            }
                        }
                        else {
                            if (!mShow) { // 如果隐藏就显示
                                mShow = true;
                                toolbarAnim(true);
                            }
                        }

                        break;
                }

                return false;
            }
        });
    }
}
