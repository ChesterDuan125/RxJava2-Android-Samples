package com.rxjava2.android.samples.ui.operators;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.R;

/**
 * 创建操作符列表
 */
public class OperatorsCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators_create);
    }

    public void startCreate(View view) {
        OperatorsCreateSampleActivity.actionStart(this, OperatorsCreateSampleActivity.class, OperatorsCreateSampleActivity.OPERATOR_CREATE);
    }

    public void startJust(View view) {
        OperatorsCreateSampleActivity.actionStart(this, OperatorsCreateSampleActivity.class, OperatorsCreateSampleActivity.OPERATOR_JUST);
    }

    public void startFrom(View view) {
        OperatorsCreateSampleActivity.actionStart(this, OperatorsCreateSampleActivity.class, OperatorsCreateSampleActivity.OPERATOR_FROM);
    }
}
