package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.R;

/**
 * 工具操作符列表
 */
public class OperatorsToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators_tools);
    }

    public void startRepeat(View view) {
        OperatorsToolsSampleActivity.actionStart(this, OperatorsToolsSampleActivity.class, OperatorsToolsSampleActivity.OPERATOR_REPEAT);
    }
}
