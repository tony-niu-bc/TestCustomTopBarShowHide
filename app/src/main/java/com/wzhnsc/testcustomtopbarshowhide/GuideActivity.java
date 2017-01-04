package com.wzhnsc.testcustomtopbarshowhide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GuideActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        findViewById(R.id.drop_in_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.drop_in_viewgroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, PullPushActivity.class));
            }
        });
    }
}
