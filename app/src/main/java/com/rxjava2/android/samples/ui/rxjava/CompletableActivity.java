package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CompletableActivity extends BaseExampleActivity {
    @Override
    protected void practice() {
        // 简易版Hello Completable
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                logcatD("Hello Completable");
            }
        }).subscribe();

        // 经常配合andThen()使用
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Exception {
                logcatD("subscribe");
                // 延时2秒后发送onComplete()
                TimeUnit.SECONDS.sleep(2);
                logcatD("onComplete");
                emitter.onComplete();
            }
            // 上面onComplete()执行后，将执行andThen()
            // andThen()有5个重载方法，对应5种观察者模式
        }).andThen(Observable.range(1, 10))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        logcatE("accept: " + integer);
                    }
                });
    }

    @Override
    protected void showCode() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        // 简易版Hello Completable\n" +
                        "        Completable.fromAction(new Action() {\n" +
                        "            @Override\n" +
                        "            public void run() throws Exception {\n" +
                        "                logcatD(\"Hello Completable\");\n" +
                        "            }\n" +
                        "        }).subscribe();\n" +
                        "\n" +
                        "        // 经常配合andThen()使用\n" +
                        "        Completable.create(new CompletableOnSubscribe() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull CompletableEmitter emitter) throws Exception {\n" +
                        "                logcatD(\"subscribe\");\n" +
                        "                // 延时2秒后发送onComplete()\n" +
                        "                TimeUnit.SECONDS.sleep(2);\n" +
                        "                logcatD(\"onComplete\");\n" +
                        "                emitter.onComplete();\n" +
                        "            }\n" +
                        "            // 上面onComplete()执行后，将执行andThen()\n" +
                        "            // andThen()有5个重载方法，对应5种观察者模式\n" +
                        "        }).andThen(Observable.range(1, 10))\n" +
                        "                .subscribe(new Consumer<Integer>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Integer integer) throws Exception {\n" +
                        "                        logcatE(\"accept: \" + integer);\n" +
                        "                    }\n" +
                        "                });\n" +
                        "    }"
        );
    }
}
