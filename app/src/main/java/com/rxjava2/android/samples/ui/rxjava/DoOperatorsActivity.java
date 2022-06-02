package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * do 操作符
 * 跟踪Observable整个生命周期
 */
public class DoOperatorsActivity extends BaseExampleActivity {

    @Override
    protected void practice() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                logcatI("subscribe");
                emitter.onNext("Hello");
                emitter.onNext("RxJava");
                emitter.onError(new Throwable("测试一下onError"));
                emitter.onComplete();
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅时回调， 时序上 doOnSubscribe > Observer#onSubscribe > ObservableOnSubscribe#subscribe
                        logcatD("doOnSubscribe");
                        // 可以取消订阅
//                        disposable.dispose();
                    }
                })
                .doOnLifecycle(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅（doOnSubscribe）后回调，可以取消订阅
                        logcatD("doOnLifecycle 1");
//                        disposable.dispose();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // 下游调用dispose()后回调，比如在Observer#onSubscribe中调用Disposable#dispose
                        logcatD("doOnLifecycle 2");
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        // 下游调用dispose()后回调，比如在Observer#onSubscribe中调用Disposable#dispose
                        // 比 “doOnLifecycle 2” 打印早
                        logcatD("doOnDispose");
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        // Observable每onNext发射一次数据，就会回调一次；在Observer#onNext()前回调，可以对数据预处理
                        logcatD("doOnNext: " + s);
                    }
                })
                .doAfterNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        // Observable每onNext发射一次数据，就会回调一次；在Observer#onNext()后回调
                        logcatD("doAfterNext: " + s);
                    }
                })
                .doOnEach(new Consumer<Notification<String>>() {
                    @Override
                    public void accept(Notification<String> stringNotification) throws Exception {
                        // Observable每发射一次数据就回调一次，包括onNext、onError、onCompleted
                        logcatD("doOnEach: " + (stringNotification.isOnNext() ? "onNext" : stringNotification.isOnComplete() ?  "onComplete" : "onError"));
                        if (stringNotification.isOnNext()) {
                            logcatD("doOnEach onNext: " + stringNotification.getValue());
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        // Observable正常终止，即发射onComplete后回调
                        logcatD("doOnComplete");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 发射onError后回调
                        logcatD("doOnError");
                    }
                })

                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        // 发射onComplete或onError时回调。 这与doAfterTerminate的不同之处在于，这发生在onComplete或onError通知之前。
                        logcatD("doOnTerminate");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        // 发生在onComplete或onError发射之后
                        logcatD("doAfterTerminate");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        // Observable终止后被回调，无论是正常终止还是异常终止，比doAfterTerminate早
                        logcatD("doFinally");
                    }
                })

                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        logcatE("onSubscribe");
//                        d.dispose();
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        logcatE("onNext: " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        logcatE("onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        logcatE("onComplete");
                    }
                });

    }

    @Override
    protected void showCode() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        Observable.create(new ObservableOnSubscribe<String>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {\n" +
                        "                logcatI(\"subscribe\");\n" +
                        "                emitter.onNext(\"Hello\");\n" +
                        "                emitter.onNext(\"RxJava\");\n" +
                        "                emitter.onError(new Throwable(\"测试一下onError\"));\n" +
                        "                emitter.onComplete();\n" +
                        "            }\n" +
                        "        })\n" +
                        "                .doOnSubscribe(new Consumer<Disposable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Disposable disposable) throws Exception {\n" +
                        "                        // 订阅时回调， 时序上 doOnSubscribe > Observer#onSubscribe > ObservableOnSubscribe#subscribe\n" +
                        "                        logcatD(\"doOnSubscribe\");\n" +
                        "                        // 可以取消订阅\n" +
                        "//                        disposable.dispose();\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doOnLifecycle(new Consumer<Disposable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Disposable disposable) throws Exception {\n" +
                        "                        // 订阅（doOnSubscribe）后回调，可以取消订阅\n" +
                        "                        logcatD(\"doOnLifecycle 1\");\n" +
                        "//                        disposable.dispose();\n" +
                        "                    }\n" +
                        "                }, new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // 下游调用dispose()后回调，比如在Observer#onSubscribe中调用Disposable#dispose\n" +
                        "                        logcatD(\"doOnLifecycle 2\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doOnDispose(new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // 下游调用dispose()后回调，比如在Observer#onSubscribe中调用Disposable#dispose\n" +
                        "                        // 比 “doOnLifecycle 2” 打印早\n" +
                        "                        logcatD(\"doOnDispose\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doOnNext(new Consumer<String>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(String s) throws Exception {\n" +
                        "                        // Observable每onNext发射一次数据，就会回调一次；在Observer#onNext()前回调，可以对数据预处理\n" +
                        "                        logcatD(\"doOnNext: \" + s);\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doAfterNext(new Consumer<String>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(String s) throws Exception {\n" +
                        "                        // Observable每onNext发射一次数据，就会回调一次；在Observer#onNext()后回调\n" +
                        "                        logcatD(\"doAfterNext: \" + s);\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doOnEach(new Consumer<Notification<String>>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Notification<String> stringNotification) throws Exception {\n" +
                        "                        // Observable每发射一次数据就回调一次，包括onNext、onError、onCompleted\n" +
                        "                        logcatD(\"doOnEach: \" + (stringNotification.isOnNext() ? \"onNext\" : stringNotification.isOnComplete() ?  \"onComplete\" : \"onError\"));\n" +
                        "                        if (stringNotification.isOnNext()) {\n" +
                        "                            logcatD(\"doOnEach onNext: \" + stringNotification.getValue());\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doOnComplete(new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // Observable正常终止，即发射onComplete后回调\n" +
                        "                        logcatD(\"doOnComplete\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doOnError(new Consumer<Throwable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Throwable throwable) throws Exception {\n" +
                        "                        // 发射onError后回调\n" +
                        "                        logcatD(\"doOnError\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "\n" +
                        "                .doOnTerminate(new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // 发射onComplete或onError时回调。 这与doAfterTerminate的不同之处在于，这发生在onComplete或onError通知之前。\n" +
                        "                        logcatD(\"doOnTerminate\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doAfterTerminate(new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // 发生在onComplete或onError发射之后\n" +
                        "                        logcatD(\"doAfterTerminate\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .doFinally(new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // Observable终止后被回调，无论是正常终止还是异常终止，比doAfterTerminate早\n" +
                        "                        logcatD(\"doFinally\");\n" +
                        "                    }\n" +
                        "                })\n" +
                        "\n" +
                        "                .subscribe(new Observer<String>() {\n" +
                        "                    @Override\n" +
                        "                    public void onSubscribe(@NonNull Disposable d) {\n" +
                        "                        logcatE(\"onSubscribe\");\n" +
                        "//                        d.dispose();\n" +
                        "                    }\n" +
                        "\n" +
                        "                    @Override\n" +
                        "                    public void onNext(@NonNull String s) {\n" +
                        "                        logcatE(\"onNext: \" + s);\n" +
                        "                    }\n" +
                        "\n" +
                        "                    @Override\n" +
                        "                    public void onError(@NonNull Throwable e) {\n" +
                        "                        logcatE(\"onError: \" + e.getMessage());\n" +
                        "                    }\n" +
                        "\n" +
                        "                    @Override\n" +
                        "                    public void onComplete() {\n" +
                        "                        logcatE(\"onComplete\");\n" +
                        "                    }\n" +
                        "                });\n" +
                        "\n" +
                        "    }"
        );
    }
}
