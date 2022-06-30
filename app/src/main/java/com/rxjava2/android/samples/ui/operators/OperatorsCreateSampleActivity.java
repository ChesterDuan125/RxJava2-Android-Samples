package com.rxjava2.android.samples.ui.operators;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 创建操作符相关示例
 */
public class OperatorsCreateSampleActivity extends BaseExampleActivity {
    public static final String OPERATOR_CREATE = "create()";
    public static final String OPERATOR_JUST = "just()";
    public static final String OPERATOR_FROM = "from()";



    @Override
    public void practice() {
        switch (mOperator) {
            case OPERATOR_CREATE:
                practiceCreate();
                break;
            case OPERATOR_JUST:
                practiceJust();
                break;
            case OPERATOR_FROM:
                practiceFrom();
                break;
            default:
                break;
        }

    }

    private void practiceCreate() {
        // 创建2个观察者
        Observer<String> observer1 = new Observer<String>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // 订阅，早于 ObservableOnSubscribe#subscribe 回调
                logcatD("onSubscribe 1");
                // 可以使用Disposable取消订阅
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                // 常规事件，n次
                logcatD("onNext 1: " + s);
                if (mDisposable != null) {
                    // 取消订阅
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // 异常事件, 1次，如果产生，将是事件序列中的最后一个事件
                logcatD("onError 1: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                // 结束事件，1次，如果产生，将是事件序列中的最后一个事件"
                logcatD("onComplete 1");
            }
        };

        Observer<String> observer2 = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                logcatE("onSubscribe 2");
            }

            @Override
            public void onNext(@NonNull String s) {
                logcatE("onNext 2: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                logcatE("onError 2: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                logcatE("onComplete 2");
            }
        };

        // 一个函数式接口，用于 观察者Observer 订阅 被观察者Observable 后，接收一个发射器Emitter，用于发送数据
        ObservableOnSubscribe<String> observableOnSubscribe = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                // 有观察者进行了订阅
                logcatI("subscribe");

                // 先判断一下发射器是否终止
                if (!emitter.isDisposed()) {
//                    emitter.onError(new Throwable("测试一下onError"));
                    // 发送数据
                    emitter.onNext("Hello");
                    emitter.onNext("RxJava");
                    // 发送完成信号
                    emitter.onComplete();
                }
            }
        };
        // 创建被观察者
        Observable<String> observable = Observable.create(observableOnSubscribe);
        // 订阅
        observable.subscribe(observer1);
        observable.subscribe(observer2);
    }

    protected void practiceJust() {
        // 创建一个发射指定值的 Observable

        // just() 有10个重载方法
        Observable.just("Hello", "Rxjava", "just()")
                .subscribe(
                        s -> logcatD(s),
                        e -> logcatE("onError: " + Log.getStackTraceString(e)),
                        () -> logcatD("onComplete"));

        // RxJava2中，如果 just()中 传入 null ，则会抛出一个空指针异常
        try {
            Observable.just(null)
                    .subscribe(
                            o -> logcatI(o.toString()),
                            throwable -> logcatE(Log.getStackTraceString(throwable)),
                            () -> logcatD("onComplete"));
        } catch (Exception e) {
            logcatE("try: " + Log.getStackTraceString(e));
        }
    }

    protected void practiceFrom() {
        logcatE("======> fromArray 1");
        Observable.fromArray("Hello", "fromArray()")
                .subscribe(s -> logcatD("onNext: " + s),
                        e -> logcatE("onError: " + e.getMessage()),
                        () -> logcatD("onComplete"),
                        disposable -> logcatD("onSubscribe: " + disposable));

        logcatE("======> fromArray 2");
        String[] array = {"Hello", "fromArray()"};
        Observable.fromArray(array)
                .subscribe(this::logcatI);

        logcatE("======> fromArray");
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            arrayList.add(i);
        }
        Observable.fromIterable(arrayList)
                .subscribe(i -> logcatD(String.valueOf(i)));

        logcatE("======> fromFuture");
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                logcatI("模拟一些耗时任务: " + Thread.currentThread());
                Thread.sleep(2000);
                return "ok";
            }
        });

        // 重载方法，如果过了指定的时长，Future还没有返回值，那么这个 Observable 就会发射onError并终止。
        try {
            Observable.fromFuture(future, 1, TimeUnit.SECONDS)
                    .subscribe(this::logcatD,
                            e -> logcatE("onError: " + Log.getStackTraceString(e)));
        } catch (Exception e) {
            logcatE("try: " + e);
        }

    }


    @Override
    protected void showCode() {
        switch (mOperator) {
            case OPERATOR_CREATE:
                showCreate();
                break;
            case OPERATOR_JUST:
                showJust();
                break;
            case OPERATOR_FROM:
                showFrom();
                break;
            default:
                break;
        }

    }

    private void showCreate() {
        codeView.showCode(
                "    public void practice() {\n" +
                        "        // 创建2个观察者\n" +
                        "        Observer<String> observer1 = new Observer<String>() {\n" +
                        "            private Disposable mDisposable;\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onSubscribe(@NonNull Disposable d) {\n" +
                        "                // 订阅，早于 ObservableOnSubscribe#subscribe 回调\n" +
                        "                logcatD(\"onSubscribe 1\");\n" +
                        "                // 可以使用Disposable取消订阅\n" +
                        "                mDisposable = d;\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onNext(@NonNull String s) {\n" +
                        "                // 常规事件，n次\n" +
                        "                logcatD(\"onNext 1: \" + s);\n" +
                        "                if (mDisposable != null) {\n" +
                        "                    // 取消订阅\n" +
                        "                    mDisposable.dispose();\n" +
                        "                }\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onError(@NonNull Throwable e) {\n" +
                        "                // 异常事件, 1次，如果产生，将是事件序列中的最后一个事件\n" +
                        "                logcatD(\"onError 1: \" + e.getMessage());\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onComplete() {\n" +
                        "                // 结束事件，1次，如果产生，将是事件序列中的最后一个事件\"\n" +
                        "                logcatD(\"onComplete 1\");\n" +
                        "            }\n" +
                        "        };\n" +
                        "\n" +
                        "        Observer<String> observer2 = new Observer<String>() {\n" +
                        "            @Override\n" +
                        "            public void onSubscribe(@NonNull Disposable d) {\n" +
                        "                logcatE(\"onSubscribe 2\");\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onNext(@NonNull String s) {\n" +
                        "                logcatE(\"onNext 2: \" + s);\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onError(@NonNull Throwable e) {\n" +
                        "                logcatE(\"onError 2: \" + e.getMessage());\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onComplete() {\n" +
                        "                logcatE(\"onComplete 2\");\n" +
                        "            }\n" +
                        "        };\n" +
                        "\n" +
                        "        // 一个函数式接口，用于 观察者Observer 订阅 被观察者Observable 后，接收一个发射器Emitter，用于发送数据\n" +
                        "        ObservableOnSubscribe<String> observableOnSubscribe = new ObservableOnSubscribe<String>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {\n" +
                        "                // 有观察者进行了订阅\n" +
                        "                logcatI(\"subscribe\");\n" +
                        "\n" +
                        "                // 先判断一下发射器是否终止\n" +
                        "                if (!emitter.isDisposed()) {\n" +
                        "//                    emitter.onError(new Throwable(\"测试一下onError\"));\n" +
                        "                    // 发送数据\n" +
                        "                    emitter.onNext(\"Hello\");\n" +
                        "                    emitter.onNext(\"RxJava\");\n" +
                        "                    // 发送完成信号\n" +
                        "                    emitter.onComplete();\n" +
                        "                }\n" +
                        "            }\n" +
                        "        };\n" +
                        "        // 创建被观察者\n" +
                        "        Observable<String> observable = Observable.create(observableOnSubscribe);\n" +
                        "        // 订阅\n" +
                        "        observable.subscribe(observer1);\n" +
                        "        observable.subscribe(observer2);\n" +
                        "    }"
        );
    }

    protected void showJust() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        // 创建一个发射指定值的 Observable\n" +
                        "\n" +
                        "        // just() 有10个重载方法\n" +
                        "        Observable.just(\"Hello\", \"Rxjava\", \"just()\")\n" +
                        "                .subscribe(\n" +
                        "                        s -> logcatD(s),\n" +
                        "                        e -> logcatE(\"onError: \" + Log.getStackTraceString(e)),\n" +
                        "                        () -> logcatD(\"onComplete\"));\n" +
                        "\n" +
                        "        // RxJava2中，如果 just()中 传入 null ，则会抛出一个空指针异常\n" +
                        "        try {\n" +
                        "            Observable.just(null)\n" +
                        "                    .subscribe(\n" +
                        "                            o -> logcatI(o.toString()),\n" +
                        "                            throwable -> logcatE(Log.getStackTraceString(throwable)),\n" +
                        "                            () -> logcatD(\"onComplete\"));\n" +
                        "        } catch (Exception e) {\n" +
                        "            logcatE(\"try: \" + Log.getStackTraceString(e));\n" +
                        "        }\n" +
                        "    }"
        );
    }

    protected void showFrom() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        logcatE(\"======> fromArray 1\");\n" +
                        "        Observable.fromArray(\"Hello\", \"fromArray()\")\n" +
                        "                .subscribe(s -> logcatD(\"onNext: \" + s),\n" +
                        "                        e -> logcatE(\"onError: \" + e.getMessage()),\n" +
                        "                        () -> logcatD(\"onComplete\"),\n" +
                        "                        disposable -> logcatD(\"onSubscribe: \" + disposable));\n" +
                        "\n" +
                        "        logcatE(\"======> fromArray 2\");\n" +
                        "        String[] array = {\"Hello\", \"fromArray()\"};\n" +
                        "        Observable.fromArray(array)\n" +
                        "                .subscribe(this::logcatI);\n" +
                        "\n" +
                        "        logcatE(\"======> fromArray\");\n" +
                        "        ArrayList<Integer> arrayList = new ArrayList<>();\n" +
                        "        for (int i = 0; i < 5; i++) {\n" +
                        "            arrayList.add(i);\n" +
                        "        }\n" +
                        "        Observable.fromIterable(arrayList)\n" +
                        "                .subscribe(i -> logcatD(String.valueOf(i)));\n" +
                        "\n" +
                        "        logcatE(\"======> fromFuture\");\n" +
                        "        ExecutorService executorService = Executors.newCachedThreadPool();\n" +
                        "        Future<String> future = executorService.submit(new Callable<String>() {\n" +
                        "            @Override\n" +
                        "            public String call() throws Exception {\n" +
                        "                logcatI(\"模拟一些耗时任务: \" + Thread.currentThread());\n" +
                        "                Thread.sleep(2000);\n" +
                        "                return \"ok\";\n" +
                        "            }\n" +
                        "        });\n" +
                        "\n" +
                        "        // 重载方法，如果过了指定的时长，Future还没有返回值，那么这个 Observable 就会发射onError并终止。\n" +
                        "        try {\n" +
                        "            Observable.fromFuture(future, 1, TimeUnit.SECONDS)\n" +
                        "                    .subscribe(this::logcatD,\n" +
                        "                            e -> logcatE(\"onError: \" + Log.getStackTraceString(e)));\n" +
                        "        } catch (Exception e) {\n" +
                        "            logcatE(\"try: \" + e);\n" +
                        "        }\n" +
                        "\n" +
                        "    }"
        );
    }
}
