package com.rxjava2.android.samples.ui.rxjava;

import android.view.View;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.ui.BaseExampleActivity;
import com.rxjava2.android.samples.ui.cache.model.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Cold Observable 转换为 Hot Observable
 *
 * Hot Observable无论有没有观察者进行订阅， 事件始终都会发生。
 * 当Hot Observable有多个订阅者时(多个观察者进行订阅时) ， Hot Observable与订阅者们的关系是一对多的关系， 可以与多个订阅者共享信息。
 *
 * Cold Observable是只有观察者订阅了， 才开始执行发射数据流的代码。
 * 并且Cold Observable和Observer只能是一对一的关系。当有多个不同的订阅者时， 消息是重新完整发送的。
 * 也就是说， 对Cold Observable而言， 有多个Observer的时候， 它们各自的事件是独立的。
 *
 * Cold Observable 转换为 Hot Observable
 * 1. publish 操作符
 * 2. Subject
 * 3. Processor
 */
public class Cold2HotActivity extends BaseExampleActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cold2hot;
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
        findViewById(R.id.btn_des3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svRun.setVisibility(View.GONE);
                codeView.setVisibility(View.VISIBLE);
                showCode3();
            }
        });
        findViewById(R.id.btn_run_practice3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeView.setVisibility(View.GONE);
                tvRunPractice.setText("");
                svRun.setVisibility(View.VISIBLE);
                practice3();
            }
        });
    }

    @Override
    protected void practice() {
        logcatD("practice: " + Thread.currentThread());
        Consumer<Long> subscriber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
            }
        };
        Consumer<Long> subscriber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);
            }
        };
        Consumer<Long> subscriber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatE(System.currentTimeMillis() + " subscriber3: "  + Thread.currentThread() + " " + aLong);
            }
        };

        // 使用publish操作符，可以让 Cold Observable转换为 Hot Observable；
        // 它将原先 Observable 转换 ConnectableObservable，ConnectableObservable是线程安全的
        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(10)
                        .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式
            }
        }).observeOn(Schedulers.newThread())
                .publish();
        // 注意，生成的 ConnectableObservable 需要调用 connect() 才能真正执行
        connectableObservable.connect();

        connectableObservable.subscribe(subscriber1);
        connectableObservable.subscribe(subscriber2);

        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的
        connectableObservable.subscribe(subscriber3);

    }

    protected void practice2() {
        logcatD("practice2: " + Thread.currentThread());
        Consumer<Long> subscriber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
            }
        };
        Consumer<Long> subscriber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);
            }
        };
        Consumer<Long> subscriber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatE(System.currentTimeMillis() + " subscriber3: "  + Thread.currentThread() + " " + aLong);
            }
        };

        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(10)
                        .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式
            }
        }).observeOn(Schedulers.newThread());

        // Subject 既是 Observable ，又是 Observer , 不是线程安全的
        // PublishSubject 先作为观察者订阅被观察者
        PublishSubject<Long> publishSubject = PublishSubject.create();
        observable.subscribe(publishSubject);

        // 再作为被观察者将接收到的事件转发出去
        publishSubject.subscribe(subscriber1);
        publishSubject.subscribe(subscriber2);

        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的
        publishSubject.subscribe(subscriber3);

    }

    protected void practice3() {
        logcatD("practice3: " + Thread.currentThread());
        Consumer<Long> subscriber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
            }
        };
        Consumer<Long> subscriber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);
            }
        };
        Consumer<Long> subscriber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                logcatE(System.currentTimeMillis() + " subscriber3: "  + Thread.currentThread() + " " + aLong);
            }
        };

        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(10)
                        .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式
            }
        }).observeOn(Schedulers.newThread());

        // Processor 和 Subject 作用相同，但Processor是RxJava2新增类，继承自Flowable，支持背压
        // 先作为观察者订阅被观察者
        PublishProcessor<Long> publishProcessor = PublishProcessor.create();
        observable.toFlowable(BackpressureStrategy.BUFFER)
                .subscribe(publishProcessor);

        // 再作为被观察者将接收到的事件转发出去
        publishProcessor.subscribe(subscriber1);
        publishProcessor.subscribe(subscriber2);

        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的
        publishProcessor.subscribe(subscriber3);

    }

    @Override
    protected void showCode() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        logcatD(\"practice: \" + Thread.currentThread());\n" +
                        "        Consumer<Long> subscriber1 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        Consumer<Long> subscriber2 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        Consumer<Long> subscriber3 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatE(System.currentTimeMillis() + \" subscriber3: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "\n" +
                        "        // 使用publish操作符，可以让 Cold Observable转换为 Hot Observable；\n" +
                        "        // 它将原先 Observable 转换 ConnectableObservable，ConnectableObservable是线程安全的\n" +
                        "        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {\n" +
                        "                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                        .take(10)\n" +
                        "                        .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式\n" +
                        "            }\n" +
                        "        }).observeOn(Schedulers.newThread())\n" +
                        "                .publish();\n" +
                        "        // 注意，生成的 ConnectableObservable 需要调用 connect() 才能真正执行\n" +
                        "        connectableObservable.connect();\n" +
                        "\n" +
                        "        connectableObservable.subscribe(subscriber1);\n" +
                        "        connectableObservable.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(50L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的\n" +
                        "        connectableObservable.subscribe(subscriber3);\n" +
                        "\n" +
                        "    }"
        );
    }

    private void showCode2() {
        codeView.showCode(
                "    protected void practice2() {\n" +
                        "        logcatD(\"practice2: \" + Thread.currentThread());\n" +
                        "        Consumer<Long> subscriber1 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        Consumer<Long> subscriber2 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        Consumer<Long> subscriber3 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatE(System.currentTimeMillis() + \" subscriber3: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "\n" +
                        "        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {\n" +
                        "                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                        .take(10)\n" +
                        "                        .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式\n" +
                        "            }\n" +
                        "        }).observeOn(Schedulers.newThread());\n" +
                        "\n" +
                        "        // Subject 既是 Observable ，又是 Observer , 不是线程安全的\n" +
                        "        // PublishSubject 先作为观察者订阅被观察者\n" +
                        "        PublishSubject<Long> publishSubject = PublishSubject.create();\n" +
                        "        observable.subscribe(publishSubject);\n" +
                        "\n" +
                        "        // 再作为被观察者将接收到的事件转发出去\n" +
                        "        publishSubject.subscribe(subscriber1);\n" +
                        "        publishSubject.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(50L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的\n" +
                        "        publishSubject.subscribe(subscriber3);\n" +
                        "\n" +
                        "    }"
        );
    }

    private void showCode3() {
        codeView.showCode(
                "    protected void practice3() {\n" +
                        "        logcatD(\"practice3: \" + Thread.currentThread());\n" +
                        "        Consumer<Long> subscriber1 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        Consumer<Long> subscriber2 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "        Consumer<Long> subscriber3 = new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) {\n" +
                        "                logcatE(System.currentTimeMillis() + \" subscriber3: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "            }\n" +
                        "        };\n" +
                        "\n" +
                        "        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {\n" +
                        "                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                        .take(10)\n" +
                        "                        .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式\n" +
                        "            }\n" +
                        "        }).observeOn(Schedulers.newThread());\n" +
                        "\n" +
                        "        // Processor 和 Subject 作用相同，但Processor是RxJava2新增类，继承自Flowable，支持背压\n" +
                        "        // 先作为观察者订阅被观察者\n" +
                        "        PublishProcessor<Long> publishProcessor = PublishProcessor.create();\n" +
                        "        observable.toFlowable(BackpressureStrategy.BUFFER)\n" +
                        "                .subscribe(publishProcessor);\n" +
                        "\n" +
                        "        // 再作为被观察者将接收到的事件转发出去\n" +
                        "        publishProcessor.subscribe(subscriber1);\n" +
                        "        publishProcessor.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(50L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的\n" +
                        "        publishProcessor.subscribe(subscriber3);\n" +
                        "\n" +
                        "    }"
        );
    }
}
