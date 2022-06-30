package com.rxjava2.android.samples.ui.rxjava;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.R;

/**
 * RxJava基础知识列表
 */
public class RxJavaBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_base);
    }

    public void startGettingStarted(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_GETTING_START);
    }

    public void startDoOperators(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_DO_OPERATOR);
    }

    public void startCold2HotPublish(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_COLD2HOT_PUBLISH);
    }
    public void startCold2HotSubject(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_COLD2HOT_SUBJECT);
    }
    public void startCold2HotPorcessor(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_COLD2HOT_PROCESSOR);
    }

    public void startHot2ColdRefCount(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_HOT2COLD_REFCOUNT);
    }
    public void startHot2ColdShare(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_HOT2COLD_SHARE);
    }

    public void startFlowable(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_FLOWABLE);
    }

    public void startSingle(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_SINGLE);
    }

    public void startCompletable(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_COMPLETABLE);
    }

    public void startMaybe(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_MAYBE);
    }

    public void startAsyncSubject(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_SUBJECT_ASYNC);
    }
    public void startBehaviorSubject(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_SUBJECT_BEHAVIOR);
    }
    public void startReplaySubject(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_SUBJECT_REPLAY);
    }
    public void startPublishSubject(View view) {
        RxjavaBaseSampleActivity.actionStart(this, RxjavaBaseSampleActivity.class, RxjavaBaseSampleActivity.BASE_SUBJECT_PUBLISH);
    }
}