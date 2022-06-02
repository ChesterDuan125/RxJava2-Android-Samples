package com.rxjava2.android.samples.ui.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.R;

/**
 * RxJava 基础
 */
public class RxJavaBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_base);
    }

    public void startGettingStarted(View view) {
        startActivity(new Intent(this, GettingStartedActivity.class));
    }

    public void startDoOperators(View view) {
        startActivity(new Intent(this, DoOperatorsActivity.class));
    }

    public void startCold2Hot(View view) {
        startActivity(new Intent(this, Cold2HotActivity.class));
    }

    public void startHot2Cold(View view) {
        startActivity(new Intent(this, Hot2ColdActivity.class));
    }

    public void startFlowable(View view) {
        startActivity(new Intent(this, FlowableActivity.class));
    }

    public void startSingle(View view) {
        startActivity(new Intent(this, SingleActivity.class));
    }

    public void startCompletable(View view) {
        startActivity(new Intent(this, CompletableActivity.class));
    }

    public void startMaybe(View view) {
        startActivity(new Intent(this, MaybeActivity.class));
    }
}