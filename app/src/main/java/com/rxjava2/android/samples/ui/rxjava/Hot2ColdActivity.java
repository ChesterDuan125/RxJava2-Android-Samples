package com.rxjava2.android.samples.ui.rxjava;

import android.view.View;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.ui.BaseExampleActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class Hot2ColdActivity extends BaseExampleActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_hot2cold;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.btn_des2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svRun.setVisibility(View.GONE);
                codeView.setVisibility(View.VISIBLE);
                showCode2();
            }
        });
        findViewById(R.id.btn_run_practice2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeView.setVisibility(View.GONE);
                tvRunPractice.setText("");
                svRun.setVisibility(View.VISIBLE);
                practice2();
            }
        });
    }

    @Override
    protected void practice() {
        logcatD("practice: " + Thread.currentThread());

        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber3 = aLong -> logcatE(System.currentTimeMillis() + " subscriber3: "  + Thread.currentThread() + " " + aLong);

        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(10)
                        .subscribe(emitter::onNext);
            }
        }).observeOn(Schedulers.newThread()).publish();

        connectableObservable.connect();

        // 通过refCount()将ConnectableObservable转换为Observable，即把HotObservable转换为ColdObservable
        Observable<Long> observable = connectableObservable.refCount();

        Disposable disposable1 = observable.subscribe(subscriber1);
        Disposable disposable2 = observable.subscribe(subscriber2);
        Disposable disposable3 = observable.subscribe(subscriber3);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 如果不是所有的订阅者／观察者都取消了订阅 ，而只是部分取消，则部分的订阅者／观察者重
        // 新开始订阅时 不会从头开始数据流
        disposable1.dispose();
        disposable2.dispose();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logcatE("==========>subscriber1、subscriber2重新订阅");

        observable.subscribe(subscriber1);
        observable.subscribe(subscriber2);

    }

    protected void practice2() {
        logcatD("practice 2: " + Thread.currentThread());

        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber3 = aLong -> logcatE(System.currentTimeMillis() + " subscriber3: "  + Thread.currentThread() + " " + aLong);

        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(10)
                        .subscribe(emitter::onNext);
            }
        }).observeOn(Schedulers.newThread()).publish();

        connectableObservable.connect();

        // share()封装了refCount()， 运行效果和refCount()一样
        Observable<Long> observable = connectableObservable.share();

        Disposable disposable1 = observable.subscribe(subscriber1);
        Disposable disposable2 = observable.subscribe(subscriber2);
        Disposable disposable3 = observable.subscribe(subscriber3);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 如果不是所有的订阅者／观察者都取消了订阅 ，而只是部分取消，则部分的订阅者／观察者重
        // 新开始订阅时 不会从头开始数据流
        disposable1.dispose();
        disposable2.dispose();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logcatE("==========>subscriber1、subscriber2重新订阅");

        observable.subscribe(subscriber1);
        observable.subscribe(subscriber2);

    }

    @Override
    protected void showCode() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        logcatD(\"practice: \" + Thread.currentThread());\n" +
                        "\n" +
                        "        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber3 = aLong -> logcatE(System.currentTimeMillis() + \" subscriber3: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "\n" +
                        "        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {\n" +
                        "                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                        .take(10)\n" +
                        "                        .subscribe(emitter::onNext);\n" +
                        "            }\n" +
                        "        }).observeOn(Schedulers.newThread()).publish();\n" +
                        "\n" +
                        "        connectableObservable.connect();\n" +
                        "\n" +
                        "        // 通过refCount()将ConnectableObservable转换为Observable，即把HotObservable转换为ColdObservable\n" +
                        "        Observable<Long> observable = connectableObservable.refCount();\n" +
                        "\n" +
                        "        Disposable disposable1 = observable.subscribe(subscriber1);\n" +
                        "        Disposable disposable2 = observable.subscribe(subscriber2);\n" +
                        "        Disposable disposable3 = observable.subscribe(subscriber3);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        // 如果不是所有的订阅者／观察者都取消了订阅 ，而只是部分取消，则部分的订阅者／观察者重\n" +
                        "        // 新开始订阅时 不会从头开始数据流\n" +
                        "        disposable1.dispose();\n" +
                        "        disposable2.dispose();\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        logcatE(\"==========>subscriber1、subscriber2重新订阅\");\n" +
                        "\n" +
                        "        observable.subscribe(subscriber1);\n" +
                        "        observable.subscribe(subscriber2);\n" +
                        "\n" +
                        "    }"
        );
    }

    protected void showCode2() {
        codeView.showCode(
                "    protected void practice2() {\n" +
                        "        logcatD(\"practice 2: \" + Thread.currentThread());\n" +
                        "\n" +
                        "        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber3 = aLong -> logcatE(System.currentTimeMillis() + \" subscriber3: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "\n" +
                        "        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {\n" +
                        "                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                        .take(10)\n" +
                        "                        .subscribe(emitter::onNext);\n" +
                        "            }\n" +
                        "        }).observeOn(Schedulers.newThread()).publish();\n" +
                        "\n" +
                        "        connectableObservable.connect();\n" +
                        "\n" +
                        "        // share()封装了refCount()， 运行效果和refCount()一样\n" +
                        "        Observable<Long> observable = connectableObservable.share();\n" +
                        "\n" +
                        "        Disposable disposable1 = observable.subscribe(subscriber1);\n" +
                        "        Disposable disposable2 = observable.subscribe(subscriber2);\n" +
                        "        Disposable disposable3 = observable.subscribe(subscriber3);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        // 如果不是所有的订阅者／观察者都取消了订阅 ，而只是部分取消，则部分的订阅者／观察者重\n" +
                        "        // 新开始订阅时 不会从头开始数据流\n" +
                        "        disposable1.dispose();\n" +
                        "        disposable2.dispose();\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        logcatE(\"==========>subscriber1、subscriber2重新订阅\");\n" +
                        "\n" +
                        "        observable.subscribe(subscriber1);\n" +
                        "        observable.subscribe(subscriber2);\n" +
                        "\n" +
                        "    }"
        );
    }
}
