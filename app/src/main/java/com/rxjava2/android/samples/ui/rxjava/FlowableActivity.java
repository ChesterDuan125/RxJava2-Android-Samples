package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class FlowableActivity extends BaseExampleActivity {

    @Override
    protected void practice() {
        // 之前是 Observable -- ObservableOnSubscribe
        // 现在是 Flowable -- FlowableOnSubscribe
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception { // ObservableEmitter --> FlowableEmitter
                        logcatD("发射----> 1");
                        e.onNext(1);
                        logcatD("发射----> 2");
                        e.onNext(2);
                        logcatD("发射----> 3");
                        e.onNext(3);
                        logcatD("发射----> 完成");
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER) // create方法中多了一个BackpressureStrategy类型的参数,先入门，背压策略后面再学
                .subscribeOn(Schedulers.newThread()) //为上下游分别指定各自的线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() { // Observer --> Subscriber
                    @Override
                    public void onSubscribe(Subscription s) {   // onSubscribe回调的参数不是Disposable而是Subscription
                        s.request(Long.MAX_VALUE);            // 注意此处，暂时先这么设置
                    }

                    @Override
                    public void onNext(Integer integer) {
                        logcatE("接收----> " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        logcatE("接收----> 完成");
                    }
                });
    }

    @Override
    protected void showCode() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        // 之前是 Observable -- ObservableOnSubscribe\n" +
                        "        // 现在是 Flowable -- FlowableOnSubscribe\n" +
                        "        Flowable.create(new FlowableOnSubscribe<Integer>() {\n" +
                        "                    @Override\n" +
                        "                    public void subscribe(FlowableEmitter<Integer> e) throws Exception { // ObservableEmitter --> FlowableEmitter\n" +
                        "                        logcatD(\"发射----> 1\");\n" +
                        "                        e.onNext(1);\n" +
                        "                        logcatD(\"发射----> 2\");\n" +
                        "                        e.onNext(2);\n" +
                        "                        logcatD(\"发射----> 3\");\n" +
                        "                        e.onNext(3);\n" +
                        "                        logcatD(\"发射----> 完成\");\n" +
                        "                        e.onComplete();\n" +
                        "                    }\n" +
                        "                }, BackpressureStrategy.BUFFER) // create方法中多了一个BackpressureStrategy类型的参数,先入门，背压策略后面再学\n" +
                        "                .subscribeOn(Schedulers.newThread()) //为上下游分别指定各自的线程\n" +
                        "                .observeOn(Schedulers.newThread())\n" +
                        "                .subscribe(new Subscriber<Integer>() { // Observer --> Subscriber\n" +
                        "                    @Override\n" +
                        "                    public void onSubscribe(Subscription s) {   // onSubscribe回调的参数不是Disposable而是Subscription\n" +
                        "                        s.request(Long.MAX_VALUE);            // 注意此处，暂时先这么设置\n" +
                        "                    }\n" +
                        "\n" +
                        "                    @Override\n" +
                        "                    public void onNext(Integer integer) {\n" +
                        "                        logcatE(\"接收----> \" + integer);\n" +
                        "                    }\n" +
                        "\n" +
                        "                    @Override\n" +
                        "                    public void onError(Throwable t) {\n" +
                        "                    }\n" +
                        "\n" +
                        "                    @Override\n" +
                        "                    public void onComplete() {\n" +
                        "                        logcatE(\"接收----> 完成\");\n" +
                        "                    }\n" +
                        "                });\n" +
                        "    }"
        );
    }
}
