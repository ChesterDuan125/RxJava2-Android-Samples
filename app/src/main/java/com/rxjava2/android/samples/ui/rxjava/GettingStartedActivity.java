package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 入门示例
 */
public class GettingStartedActivity extends BaseExampleActivity {

    @Override
    public void practice() {
        /* 完整写法 */
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
        // 订阅
        observable.subscribe(observer1);

        observable.subscribe(observer2);

        logcatE(">>>>>>>>>>>>>>>>>> 简写方式");

        /* 简写方式 */
        Observable.just("Hello", "RxJava")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        // 相当于 Observer#onNext， subscribe有重载方法，下面几个参数都可省略
                        logcatI("Consumer<String>: " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 相当于 Observer#onError
                        logcatI("Consumer<Throwable>: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // 相当于 Observer#onComplete
                        logcatI("Action");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 相当于 Observer#onSubscribe
                        logcatI("Consumer<Disposable>");
                    }
                });
    }

    @Override
    protected void showCode() {
        codeView.showCode(
                "    public void practice() {\n" +
                        "        /* 完整写法 */\n" +
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
                        "\n" +
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
                        "        // 订阅\n" +
                        "        observable.subscribe(observer1);\n" +
                        "\n" +
                        "        observable.subscribe(observer2);\n" +
                        "\n" +
                        "        logcatE(\">>>>>>>>>>>>>>>>>> 简写方式\");\n" +
                        "\n" +
                        "        /* 简写方式 */\n" +
                        "        Observable.just(\"Hello\", \"RxJava\")\n" +
                        "                .subscribe(new Consumer<String>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(String s) throws Exception {\n" +
                        "                        // 相当于 Observer#onNext， subscribe有重载方法，下面几个参数都可省略\n" +
                        "                        logcatI(\"Consumer<String>: \" + s);\n" +
                        "                    }\n" +
                        "                }, new Consumer<Throwable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Throwable throwable) throws Exception {\n" +
                        "                        // 相当于 Observer#onError\n" +
                        "                        logcatI(\"Consumer<Throwable>: \" + throwable.getMessage());\n" +
                        "                    }\n" +
                        "                }, new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // 相当于 Observer#onComplete\n" +
                        "                        logcatI(\"Action\");\n" +
                        "                    }\n" +
                        "                }, new Consumer<Disposable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Disposable disposable) throws Exception {\n" +
                        "                        // 相当于 Observer#onSubscribe\n" +
                        "                        logcatI(\"Consumer<Disposable>\");\n" +
                        "                    }\n" +
                        "                });\n" +
                        "    }"
        );
    }
}
