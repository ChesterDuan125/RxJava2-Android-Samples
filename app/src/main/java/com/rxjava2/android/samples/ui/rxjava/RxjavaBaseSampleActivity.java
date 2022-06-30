package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * Rxjava基础示例
 */
public class RxjavaBaseSampleActivity extends BaseExampleActivity {
    public static final String BASE_GETTING_START = "入门示例";
    public static final String BASE_DO_OPERATOR = "do操作符";
    public static final String BASE_COLD2HOT_PUBLISH = "Observable cold2hot: publish()";
    public static final String BASE_COLD2HOT_SUBJECT = "Observable cold2hot: Subject";
    public static final String BASE_COLD2HOT_PROCESSOR = "Observable cold2hot: Processor";
    public static final String BASE_HOT2COLD_REFCOUNT = "Observable hot2cold: refCount()";
    public static final String BASE_HOT2COLD_SHARE = "Observable hot2cold: share()";
    public static final String BASE_FLOWABLE = "Flowable入门";
    public static final String BASE_SINGLE = "Single入门";
    public static final String BASE_COMPLETABLE = "Completable入门";
    public static final String BASE_MAYBE = "Maybe入门";
    public static final String BASE_SUBJECT_ASYNC = "AsyncSubject入门";
    public static final String BASE_SUBJECT_BEHAVIOR = "BehaviorSubject入门";
    public static final String BASE_SUBJECT_REPLAY = "ReplaySubject入门";
    public static final String BASE_SUBJECT_PUBLISH = "PublishSubject入门";

    @Override
    protected void practice() {
        switch (mOperator) {
            case BASE_GETTING_START:
                practiceGettingStart();
                break;
            case BASE_DO_OPERATOR:
                practiceDoOperator();
                break;
            case BASE_COLD2HOT_PUBLISH:
                practiceCold2HotPublish();
                break;
            case BASE_COLD2HOT_SUBJECT:
                practiceCold2HotSubject();
                break;
            case BASE_COLD2HOT_PROCESSOR:
                practiceCold2HotPorcessor();
                break;
            case BASE_HOT2COLD_REFCOUNT:
                practiceHot2ColdRefCount();
                break;
            case BASE_HOT2COLD_SHARE:
                practiceHot2ColdShare();
                break;
            case BASE_FLOWABLE:
                practiceFlowable();
                break;
            case BASE_SINGLE:
                practiceSingle();
                break;
            case BASE_COMPLETABLE:
                practiceCompletable();
                break;
            case BASE_MAYBE:
                practiceMaybe();
                break;
            case BASE_SUBJECT_ASYNC:
                practiceAsyncSubject();
                break;
            case BASE_SUBJECT_BEHAVIOR:
                practiceBehaviorSubject();
                break;
            case BASE_SUBJECT_REPLAY:
                practiceReplaySubject();
                break;
            case BASE_SUBJECT_PUBLISH:
                practicePublishSubject();
                break;
            default:
                break;
        }
    }

    private void practiceGettingStart() {
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
                        logcatI("onNext: " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 相当于 Observer#onError
                        logcatI("onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // 相当于 Observer#onComplete
                        logcatI("onComplete");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 相当于 Observer#onSubscribe
                        logcatI("onSubscribe");
                    }
                });
    }

    private void practiceDoOperator() {
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

    private void practiceCold2HotPublish() {
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

        // create() 创建的是一个 cold Observable， 事件是独立的
        Observable<Long> longObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(10)
                        .subscribe(aLong -> {
                            emitter.onNext(aLong); // 可以使用方法引用 简化 Lambda表达式： emitter::onNext
                        });
            }
        }).observeOn(Schedulers.newThread());

        logcatE("============> create()创建的cold Observable测试开始");
        longObservable.subscribe(subscriber1);
        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 事件是独立的，从头接收数据流
        logcatE("subscriber2 开始订阅");
        Disposable disposable = longObservable.subscribe(subscriber2);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 取消订阅
        logcatE("subscriber2 取消订阅");
        disposable.dispose();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logcatE("subscriber2 重新订阅");
        // 重头接收数据流
        longObservable.subscribe(subscriber2);

        try {
            Thread.sleep(600L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 使用publish操作符，可以让 Cold Observable转换为 Hot Observable；
        // 它将原先 Observable 转换 ConnectableObservable，ConnectableObservable是线程安全的
        ConnectableObservable<Long> connectableObservable = longObservable.publish();

        // 注意，生成的 ConnectableObservable 需要调用 connect() 才能真正执行
        connectableObservable.connect();

        logcatE("============> publish()转换后的hot Observable测试开始");
        connectableObservable.subscribe(subscriber1);

        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的
        connectableObservable.subscribe(subscriber2);
    }

    private void practiceCold2HotSubject() {
        logcatD("practice2: " + Thread.currentThread());
        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);

        // create() 创建的是一个 cold Observable， 事件是独立的
        Observable<Long> observable = Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .take(10)
                    .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式
        }).observeOn(Schedulers.newThread());

        // Subject 既是 Observable ，又是 Observer , 不是线程安全的
        // PublishSubject.create() 创建的是Hot Observable
        PublishSubject<Long> publishSubject = PublishSubject.create();
        // PublishSubject 先作为观察者订阅被观察者
        observable.subscribe(publishSubject);

        // 再作为被观察者将接收到的事件转发出去
        publishSubject.subscribe(subscriber1);

        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的
        publishSubject.subscribe(subscriber2);

    }

    private void practiceCold2HotPorcessor() {
        logcatD("practice3: " + Thread.currentThread());
        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + " subscriber1: "  + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + " subscriber2: "  + Thread.currentThread() + " " + aLong);

        Observable<Long> observable = Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .take(10)
                    .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式
        }).observeOn(Schedulers.newThread());

        // Processor 和 Subject 作用相同，但Processor是RxJava2新增类，继承自Flowable，支持背压
        // PublishProcessor.create() 创建的是Hot Observable
        PublishProcessor<Long> publishProcessor = PublishProcessor.create();
        // 先作为观察者订阅被观察者
        observable.toFlowable(BackpressureStrategy.BUFFER)
                .subscribe(publishProcessor);

        // 再作为被观察者将接收到的事件转发出去
        publishProcessor.subscribe(subscriber1);

        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的
        publishProcessor.subscribe(subscriber2);
    }

    private void practiceHot2ColdRefCount() {
        logcatD("practice: " + Thread.currentThread());

        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + " subscriber1: " + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + " subscriber2: " + Thread.currentThread() + " " + aLong);

        // 通过publish()获取一个 Hot Observable
        ConnectableObservable<Long> connectableObservable =
                Observable.create((ObservableOnSubscribe<Long>) emitter ->
                        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                                .take(10)
                                .subscribe(emitter::onNext))
                        .observeOn(Schedulers.newThread())
                        .publish();
        // 不需要调用connect()，参考下面refcount()的注释
//        connectableObservable.connect();

        // 通过refCount()将 ConnectableObservable 转换为Observable，即把 HotObservable转换为 ColdObservable
        Observable<Long> observable = connectableObservable.refCount();

        // refCount() 把 ConnectableObservable 的连接和断开自动化了，当有Subscriber开始订阅，则开始 connect
        logcatE("==========>subscriber1、subscriber1 开始订阅");
        Disposable disposable1 = observable.subscribe(subscriber1);
        observable.subscribe(subscriber2);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // refCount() 使得该Observable，只有当最后一个观察者完成，才会断开与下层ConnectableObservable的连接
        logcatE("==========>subscriber1 取消订阅");
        disposable1.dispose();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logcatE("==========>subscriber1 重新订阅");
        // 如果只是部分Subscriber取消订阅，再重新开始订阅时，不会从头开始接收数据流
        observable.subscribe(subscriber1);
    }

    private void practiceHot2ColdShare() {
        logcatD("practice 2: " + Thread.currentThread());

        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + " subscriber1: " + Thread.currentThread() + " " + aLong);
        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + " subscriber2: " + Thread.currentThread() + " " + aLong);

        ConnectableObservable<Long> connectableObservable =
                Observable.create((ObservableOnSubscribe<Long>) emitter ->
                        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                                .take(10)
                                .subscribe(emitter::onNext))
                        .observeOn(Schedulers.newThread())
                        .publish();

        connectableObservable.connect();

        // share()封装了refCount()， 运行效果和refCount()一样
        Observable<Long> observable = connectableObservable.share();

        logcatE("==========>subscriber1 开始订阅");
        Disposable disposable1 = observable.subscribe(subscriber1);
        observable.subscribe(subscriber2);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logcatE("==========>subscriber1 取消订阅");
        disposable1.dispose();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logcatE("==========>subscriber1 重新订阅");
        observable.subscribe(subscriber1);
    }

    private void practiceFlowable() {
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
                        // 注意此处，暂时先这么设置，否则订阅者无法接收到数据
                        s.request(Long.MAX_VALUE);
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

    private void practiceSingle() {
        Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> emitter) throws Exception {
                logcatD("subscribe");

                logcatD("onSuccess: 111");
                // onSuccess()用于发射数据（在 Observable/Flowable 中使用 onNext() 来发射数据），
                //而且只能发射一个数据，后面即使再发射数据 不会做任何处理。
                emitter.onSuccess(111L);
                emitter.onSuccess(222L);
                emitter.onSuccess(333L);


                // 调用onSuccess后，如果再调用onError会报错如下：
                // io.reactivex.exceptions.UndeliverableException:The exception could not be delivered
                // to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with.
//                emitter.onError(new Throwable("测试一下onError"));
            }
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                logcatE("accept: " + aLong);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                logcatE("accept: " + throwable.getMessage());
            }
        });

        /*简写方式*/
        logcatI("===========>简写方式");
        Single.create(emitter -> {
            logcatD("subscribe 2");

            emitter.onError(new Throwable("测试一下onError"));

        }).subscribe(new BiConsumer<Object, Throwable>() { // 这里也可以Lambda简写，只是为了演示可以使用BiConsumer简化
            @Override
            public void accept(Object o, Throwable throwable) throws Exception {
                logcatE("o: " + o + " thowable: " + throwable);
            }
        });
    }

    private void practiceCompletable() {
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

    private void practiceMaybe() {
        Maybe.create(new MaybeOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<Long> emitter) throws Exception {
                logcatD("subscribe");

                emitter.onSuccess(123L);
                // 只能发送一次数据，再次发送，发射器不会处理
                // 相当于发射一次后，就断开订阅了
                emitter.onSuccess(234L);

                // 有数据发送了，或调用了onError后，就不会再发射onComplete
                emitter.onComplete();

                // 再次发送onError会报错
//                emitter.onError(new Throwable("测试一下onError"));
            }
        }).subscribe(new MaybeObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // 打印
                logcatE("onSubscribe");
            }

            @Override
            public void onSuccess(@NonNull Long aLong) {
                // 打印1次
                logcatE("onSuccess: " + aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // 不打印
                logcatE("onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                // 不打印
                logcatE("onComplete");
            }
        });
    }

    private void practiceAsyncSubject() {
        // Observer 会接收 AsyncSubject onComplete（）之前的最后一个数据
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("asyncSubject 1");
        asyncSubject.onNext("asyncSubject 2");

        asyncSubject.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                logcatD("onNext: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                logcatD("onError: " + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                logcatD("onComplete");
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                logcatD("onSubscribe: " + disposable);
            }
        });

        // 注意， subject.onComplete（） 必须要调用才开始发送数据，否则观察者将接收不到任何数据
        asyncSubject.onComplete();

        asyncSubject.onNext("asyncSubject 3");

        // onComplete后调用onError，会出现异常：UndeliverableException
//        asyncSubject.onError(new Throwable("测试onError"));
    }

    private void practiceBehaviorSubject() {
        // Observer 会先接 BehaviorSubject 被订阅之前的最后一个数据 , 再接收订阅之后发射过来的数据。
        // 如果 BehaviorSubject  被订阅之前没有发送任何数据 则会发送一个默认数据。
//        BehaviorSubject<String> behaviorSubject = BehaviorSubject.createDefault("123");

        // 如果不发送下面2个数据，默认则会先发送 "123"
//        behaviorSubject.onNext("behaviorSubject 1");
//        behaviorSubject.onNext("behaviorSubject 2");

        // 创建没有默认数据的BehaviorSubject， 订阅前不会发送默认数据
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();

        behaviorSubject.subscribe(s -> {
            logcatE("onNext: " + s);

        }, t -> {
            logcatE("onError: " + t.getMessage());

        }, () -> {
            logcatE("onComplete");

        }, disposable -> {
            logcatE("onSubscribe: " + disposable);

        });

        behaviorSubject.onNext("behaviorSubject 3");
        behaviorSubject.onNext("behaviorSubject 4");

        // 与AsyncSubject不同，不用调用onComplete也可以发送数据
//        behaviorSubject.onComplete();

        behaviorSubject.onNext("behaviorSubject 5");

        // 可以在onComplete调用前，获取到最后一次发送的数据
        logcatI(behaviorSubject.getValue());
    }

    private void practiceReplaySubject() {
        // ReplaySubject 会发射所有来自原始 Observable 的数据给观察者，无论它们是何时订阅的。
        // createWithSize(n)表示只缓存订阅前最后发送的n条数据。
        // ReplaySubject 除了可以限制缓存数据的数量，还能限制缓存的时间，使用 createWithTime()即可。
        ReplaySubject<String> replaySubject = ReplaySubject.createWithSize(2);
        replaySubject.onNext("replaySubject 1");
        replaySubject.onNext("replaySubject 2");
        replaySubject.onNext("replaySubject 3");

        replaySubject.subscribe(s -> {
            logcatE("onNext: " + s);

        }, t -> {
            logcatE("onError: " + t.getMessage());

        }, () -> {
            logcatE("onComplete");

        }, disposable -> {
            logcatE("onSubscribe: " + disposable);

        });

        replaySubject.onNext("replaySubject 4");

        replaySubject.onError(new Throwable("测试onError"));

        // onError或onComplete是事件序列最后一个事件，执行其中一个后，另一个不会执行，且先执行onComplete后再执行onError会报错
        replaySubject.onComplete();
    }

    private void practicePublishSubject() {
        // Observer 只接收 PublishSubject 被订阅之后发送的数据
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("publishSubject 1");
        publishSubject.onNext("publishSubject 2");

        publishSubject.subscribe(s -> {
            logcatE("onNext: " + s);

        }, t -> {
            logcatE("onError: " + t.getMessage());

        }, () -> {
            logcatE("onComplete");

        }, disposable -> {
            logcatE("onSubscribe: " + disposable);

        });

        publishSubject.onNext("publishSubject 3");
        publishSubject.onNext("publishSubject 4");
        publishSubject.onComplete();
    }

    @Override
    protected void showCode() {
        switch (mOperator) {
            case BASE_GETTING_START:
                showGettingStart();
                break;
            case BASE_DO_OPERATOR:
                showDoOperator();
                break;
            case BASE_COLD2HOT_PUBLISH:
                showCold2HotPublish();
                break;
            case BASE_COLD2HOT_SUBJECT:
                showCold2HotSubject();
                break;
            case BASE_COLD2HOT_PROCESSOR:
                showCold2HotPorcessor();
                break;
            case BASE_HOT2COLD_REFCOUNT:
                showHot2ColdRefCount();
                break;
            case BASE_HOT2COLD_SHARE:
                showHot2ColdShare();
                break;
            case BASE_FLOWABLE:
                showFlowable();
                break;
            case BASE_SINGLE:
                showSingle();
                break;
            case BASE_COMPLETABLE:
                showCompletable();
                break;
            case BASE_MAYBE:
                showMaybe();
                break;
            case BASE_SUBJECT_ASYNC:
                showAsyncSubject();
                break;
            case BASE_SUBJECT_BEHAVIOR:
                showBehaviorSubject();
                break;
            case BASE_SUBJECT_REPLAY:
                showReplaySubject();
                break;
            case BASE_SUBJECT_PUBLISH:
                showPublishSubject();
                break;
            default:
                break;
        }
    }

    private void showGettingStart() {
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
                        "                        logcatI(\"onNext: \" + s);\n" +
                        "                    }\n" +
                        "                }, new Consumer<Throwable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Throwable throwable) throws Exception {\n" +
                        "                        // 相当于 Observer#onError\n" +
                        "                        logcatI(\"onError: \" + throwable.getMessage());\n" +
                        "                    }\n" +
                        "                }, new Action() {\n" +
                        "                    @Override\n" +
                        "                    public void run() throws Exception {\n" +
                        "                        // 相当于 Observer#onComplete\n" +
                        "                        logcatI(\"onComplete\");\n" +
                        "                    }\n" +
                        "                }, new Consumer<Disposable>() {\n" +
                        "                    @Override\n" +
                        "                    public void accept(Disposable disposable) throws Exception {\n" +
                        "                        // 相当于 Observer#onSubscribe\n" +
                        "                        logcatI(\"onSubscribe\");\n" +
                        "                    }\n" +
                        "                });\n" +
                        "    }"
        );
    }

    private void showDoOperator(){
        codeView.showCode(
                "    private void practiceDoOperator() {\n" +
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

    private void showCold2HotPublish() {
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
                        "\n" +
                        "        // create() 创建的是一个 cold Observable， 事件是独立的\n" +
                        "        Observable<Long> longObservable = Observable.create(new ObservableOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {\n" +
                        "                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                        .take(10)\n" +
                        "                        .subscribe(aLong -> {\n" +
                        "                            emitter.onNext(aLong); // 可以使用方法引用 简化 Lambda表达式： emitter::onNext\n" +
                        "                        });\n" +
                        "            }\n" +
                        "        }).observeOn(Schedulers.newThread());\n" +
                        "\n" +
                        "        logcatE(\"============> create()创建的cold Observable测试开始\");\n" +
                        "        longObservable.subscribe(subscriber1);\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 事件是独立的，从头接收数据流\n" +
                        "        logcatE(\"subscriber2 开始订阅\");\n" +
                        "        Disposable disposable = longObservable.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 取消订阅\n" +
                        "        logcatE(\"subscriber2 取消订阅\");\n" +
                        "        disposable.dispose();\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        logcatE(\"subscriber2 重新订阅\");\n" +
                        "        // 重头接收数据流\n" +
                        "        longObservable.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(600L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "        // 使用publish操作符，可以让 Cold Observable转换为 Hot Observable；\n" +
                        "        // 它将原先 Observable 转换 ConnectableObservable，ConnectableObservable是线程安全的\n" +
                        "        ConnectableObservable<Long> connectableObservable = longObservable.publish();\n" +
                        "\n" +
                        "        // 注意，生成的 ConnectableObservable 需要调用 connect() 才能真正执行\n" +
                        "        connectableObservable.connect();\n" +
                        "\n" +
                        "        logcatE(\"============> publish()转换后的hot Observable测试开始\");\n" +
                        "        connectableObservable.subscribe(subscriber1);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(50L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的\n" +
                        "        connectableObservable.subscribe(subscriber2);\n" +
                        "    }"
        );
    }

    private void showCold2HotSubject() {
        codeView.showCode(
                "    protected void practice2() {\n" +
                        "        logcatD(\"practice2: \" + Thread.currentThread());\n" +
                        "        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "\n" +
                        "        // create() 创建的是一个 cold Observable， 事件是独立的\n" +
                        "        Observable<Long> observable = Observable.create((ObservableOnSubscribe<Long>) emitter -> {\n" +
                        "            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                    .take(10)\n" +
                        "                    .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式\n" +
                        "        }).observeOn(Schedulers.newThread());\n" +
                        "\n" +
                        "        // Subject 既是 Observable ，又是 Observer , 不是线程安全的\n" +
                        "        // PublishSubject.create() 创建的是Hot Observable\n" +
                        "        PublishSubject<Long> publishSubject = PublishSubject.create();\n" +
                        "        // PublishSubject 先作为观察者订阅被观察者\n" +
                        "        observable.subscribe(publishSubject);\n" +
                        "\n" +
                        "        // 再作为被观察者将接收到的事件转发出去\n" +
                        "        publishSubject.subscribe(subscriber1);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(50L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的\n" +
                        "        publishSubject.subscribe(subscriber2);\n" +
                        "\n" +
                        "    }"
        );
    }

    private void showCold2HotPorcessor() {
        codeView.showCode(
                "    protected void practice3() {\n" +
                        "        logcatD(\"practice3: \" + Thread.currentThread());\n" +
                        "        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + \" subscriber1: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + \" subscriber2: \"  + Thread.currentThread() + \" \" + aLong);\n" +
                        "\n" +
                        "        Observable<Long> observable = Observable.create((ObservableOnSubscribe<Long>) emitter -> {\n" +
                        "            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                    .take(10)\n" +
                        "                    .subscribe(emitter::onNext); // 方法引用 简化 Lambda表达式\n" +
                        "        }).observeOn(Schedulers.newThread());\n" +
                        "\n" +
                        "        // Processor 和 Subject 作用相同，但Processor是RxJava2新增类，继承自Flowable，支持背压\n" +
                        "        // PublishProcessor.create() 创建的是Hot Observable\n" +
                        "        PublishProcessor<Long> publishProcessor = PublishProcessor.create();\n" +
                        "        // 先作为观察者订阅被观察者\n" +
                        "        observable.toFlowable(BackpressureStrategy.BUFFER)\n" +
                        "                .subscribe(publishProcessor);\n" +
                        "\n" +
                        "        // 再作为被观察者将接收到的事件转发出去\n" +
                        "        publishProcessor.subscribe(subscriber1);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(50L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        // 订阅的晚，就会少接收一些事件，Hot Observable的事件是共享的\n" +
                        "        publishProcessor.subscribe(subscriber2);\n" +
                        "    }"
        );
    }

    private void showHot2ColdRefCount() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        logcatD(\"practice: \" + Thread.currentThread());\n" +
                        "\n" +
                        "        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + \" subscriber1: \" + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + \" subscriber2: \" + Thread.currentThread() + \" \" + aLong);\n" +
                        "\n" +
                        "        // 通过publish()获取一个 Hot Observable\n" +
                        "        ConnectableObservable<Long> connectableObservable =\n" +
                        "                Observable.create((ObservableOnSubscribe<Long>) emitter ->\n" +
                        "                        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                                .take(10)\n" +
                        "                                .subscribe(emitter::onNext))\n" +
                        "                        .observeOn(Schedulers.newThread())\n" +
                        "                        .publish();\n" +
                        "        // 不需要调用connect()，参考下面refcount()的注释\n" +
                        "//        connectableObservable.connect();\n" +
                        "\n" +
                        "        // 通过refCount()将 ConnectableObservable 转换为Observable，即把 HotObservable转换为 ColdObservable\n" +
                        "        Observable<Long> observable = connectableObservable.refCount();\n" +
                        "\n" +
                        "        // refCount() 把 ConnectableObservable 的连接和断开自动化了，当有Subscriber开始订阅，则开始 connect\n" +
                        "        logcatE(\"==========>subscriber1、subscriber1 开始订阅\");\n" +
                        "        Disposable disposable1 = observable.subscribe(subscriber1);\n" +
                        "        observable.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        // refCount() 使得该Observable，只有当最后一个观察者完成，才会断开与下层ConnectableObservable的连接\n" +
                        "        logcatE(\"==========>subscriber1 取消订阅\");\n" +
                        "        disposable1.dispose();\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        logcatE(\"==========>subscriber1 重新订阅\");\n" +
                        "        // 如果只是部分Subscriber取消订阅，再重新开始订阅时，不会从头开始接收数据流\n" +
                        "        observable.subscribe(subscriber1);\n" +
                        "    }"
        );
    }

    private void showHot2ColdShare() {
        codeView.showCode(
                "    protected void practice2() {\n" +
                        "        logcatD(\"practice 2: \" + Thread.currentThread());\n" +
                        "\n" +
                        "        Consumer<Long> subscriber1 = aLong -> logcatD(System.currentTimeMillis() + \" subscriber1: \" + Thread.currentThread() + \" \" + aLong);\n" +
                        "        Consumer<Long> subscriber2 = aLong -> logcatI(System.currentTimeMillis() + \" subscriber2: \" + Thread.currentThread() + \" \" + aLong);\n" +
                        "\n" +
                        "        ConnectableObservable<Long> connectableObservable =\n" +
                        "                Observable.create((ObservableOnSubscribe<Long>) emitter ->\n" +
                        "                        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())\n" +
                        "                                .take(10)\n" +
                        "                                .subscribe(emitter::onNext))\n" +
                        "                        .observeOn(Schedulers.newThread())\n" +
                        "                        .publish();\n" +
                        "\n" +
                        "        connectableObservable.connect();\n" +
                        "\n" +
                        "        // share()封装了refCount()， 运行效果和refCount()一样\n" +
                        "        Observable<Long> observable = connectableObservable.share();\n" +
                        "\n" +
                        "        logcatE(\"==========>subscriber1 开始订阅\");\n" +
                        "        Disposable disposable1 = observable.subscribe(subscriber1);\n" +
                        "        observable.subscribe(subscriber2);\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        logcatE(\"==========>subscriber1 取消订阅\");\n" +
                        "        disposable1.dispose();\n" +
                        "\n" +
                        "        try {\n" +
                        "            Thread.sleep(20L);\n" +
                        "        } catch (InterruptedException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "\n" +
                        "        logcatE(\"==========>subscriber1 重新订阅\");\n" +
                        "        observable.subscribe(subscriber1);\n" +
                        "    }"
        );
    }

    private void showFlowable() {
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
                        "                        // 注意此处，暂时先这么设置，否则订阅者无法接收到数据\n" +
                        "                        s.request(Long.MAX_VALUE);\n" +
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

    private void showSingle() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        Single.create(new SingleOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull SingleEmitter<Long> emitter) throws Exception {\n" +
                        "                logcatD(\"subscribe\");\n" +
                        "\n" +
                        "                logcatD(\"onSuccess: 111\");\n" +
                        "                // onSuccess()用于发射数据（在 Observable/Flowable 中使用 onNext() 来发射数据），\n" +
                        "                //而且只能发射一个数据，后面即使再发射数据 不会做任何处理。\n" +
                        "                emitter.onSuccess(111L);\n" +
                        "                emitter.onSuccess(222L);\n" +
                        "                emitter.onSuccess(333L);\n" +
                        "\n" +
                        "\n" +
                        "                // 调用onSuccess后，如果再调用onError会报错如下：\n" +
                        "                // io.reactivex.exceptions.UndeliverableException:The exception could not be delivered\n" +
                        "                // to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with.\n" +
                        "//                emitter.onError(new Throwable(\"测试一下onError\"));\n" +
                        "            }\n" +
                        "        }).subscribe(new Consumer<Long>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Long aLong) throws Exception {\n" +
                        "                logcatE(\"accept: \" + aLong);\n" +
                        "            }\n" +
                        "        }, new Consumer<Throwable>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Throwable throwable) throws Exception {\n" +
                        "                logcatE(\"accept: \" + throwable.getMessage());\n" +
                        "            }\n" +
                        "        });\n" +
                        "\n" +
                        "        /*简写方式*/\n" +
                        "        logcatI(\"===========>简写方式\");\n" +
                        "        Single.create(emitter -> {\n" +
                        "            logcatD(\"subscribe 2\");\n" +
                        "\n" +
                        "            emitter.onError(new Throwable(\"测试一下onError\"));\n" +
                        "\n" +
                        "        }).subscribe(new BiConsumer<Object, Throwable>() { // 这里也可以Lambda简写，只是为了演示可以使用BiConsumer简化\n" +
                        "            @Override\n" +
                        "            public void accept(Object o, Throwable throwable) throws Exception {\n" +
                        "                logcatE(\"o: \" + o + \" thowable: \" + throwable);\n" +
                        "            }\n" +
                        "        });\n" +
                        "    }"
        );
    }

    private void showCompletable() {
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

    private void showMaybe() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        Maybe.create(new MaybeOnSubscribe<Long>() {\n" +
                        "            @Override\n" +
                        "            public void subscribe(@NonNull MaybeEmitter<Long> emitter) throws Exception {\n" +
                        "                logcatD(\"subscribe\");\n" +
                        "\n" +
                        "                emitter.onSuccess(123L);\n" +
                        "                // 只能发送一次数据，再次发送，发射器不会处理\n" +
                        "                // 相当于发射一次后，就断开订阅了\n" +
                        "                emitter.onSuccess(234L);\n" +
                        "\n" +
                        "                // 有数据发送了，或调用了onError后，就不会再发射onComplete\n" +
                        "                emitter.onComplete();\n" +
                        "\n" +
                        "                // 再次发送onError会报错\n" +
                        "//                emitter.onError(new Throwable(\"测试一下onError\"));\n" +
                        "            }\n" +
                        "        }).subscribe(new MaybeObserver<Long>() {\n" +
                        "            @Override\n" +
                        "            public void onSubscribe(@NonNull Disposable d) {\n" +
                        "                // 打印\n" +
                        "                logcatE(\"onSubscribe\");\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onSuccess(@NonNull Long aLong) {\n" +
                        "                // 打印1次\n" +
                        "                logcatE(\"onSuccess: \" + aLong);\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onError(@NonNull Throwable e) {\n" +
                        "                // 不打印\n" +
                        "                logcatE(\"onError: \" + e.getMessage());\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onComplete() {\n" +
                        "                // 不打印\n" +
                        "                logcatE(\"onComplete\");\n" +
                        "            }\n" +
                        "        });\n" +
                        "    }"
        );
    }

    private void showAsyncSubject() {
        codeView.showCode(
                "    protected void practice() {\n" +
                        "        // Observer 会接收 AsyncSubject onComplete（）之前的最后一个数据\n" +
                        "        AsyncSubject<String> asyncSubject = AsyncSubject.create();\n" +
                        "        asyncSubject.onNext(\"asyncSubject 1\");\n" +
                        "        asyncSubject.onNext(\"asyncSubject 2\");\n" +
                        "\n" +
                        "        asyncSubject.subscribe(new Consumer<String>() {\n" +
                        "            @Override\n" +
                        "            public void accept(String s) throws Exception {\n" +
                        "                logcatD(\"onNext: \" + s);\n" +
                        "            }\n" +
                        "        }, new Consumer<Throwable>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Throwable throwable) throws Exception {\n" +
                        "                logcatD(\"onError: \" + throwable.getMessage());\n" +
                        "            }\n" +
                        "        }, new Action() {\n" +
                        "            @Override\n" +
                        "            public void run() throws Exception {\n" +
                        "                logcatD(\"onComplete\");\n" +
                        "            }\n" +
                        "        }, new Consumer<Disposable>() {\n" +
                        "            @Override\n" +
                        "            public void accept(Disposable disposable) throws Exception {\n" +
                        "                logcatD(\"onSubscribe: \" + disposable);\n" +
                        "            }\n" +
                        "        });\n" +
                        "\n" +
                        "        // 注意， subject.onComplete（） 必须要调用才开始发送数据，否则观察者将接收不到任何数据\n" +
                        "        asyncSubject.onComplete();\n" +
                        "\n" +
                        "        asyncSubject.onNext(\"asyncSubject 3\");\n" +
                        "\n" +
                        "        // onComplete后调用onError，会出现异常：UndeliverableException\n" +
                        "//        asyncSubject.onError(new Throwable(\"测试onError\"));\n" +
                        "    }"
        );
    }

    private void showBehaviorSubject() {
        codeView.showCode(
                "    private void practice2() {\n" +
                        "        // Observer 会先接 BehaviorSubject 被订阅之前的最后一个数据 , 再接收订阅之后发射过来的数据。\n" +
                        "        // 如果 BehaviorSubject  被订阅之前没有发送任何数据 则会发送一个默认数据。\n" +
                        "//        BehaviorSubject<String> behaviorSubject = BehaviorSubject.createDefault(\"123\");\n" +
                        "\n" +
                        "        // 如果不发送下面2个数据，默认则会先发送 \"123\"\n" +
                        "//        behaviorSubject.onNext(\"behaviorSubject 1\");\n" +
                        "//        behaviorSubject.onNext(\"behaviorSubject 2\");\n" +
                        "\n" +
                        "        // 创建没有默认数据的BehaviorSubject， 订阅前不会发送默认数据\n" +
                        "        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();\n" +
                        "\n" +
                        "        behaviorSubject.subscribe(s -> {\n" +
                        "            logcatE(\"onNext: \" + s);\n" +
                        "\n" +
                        "        }, t -> {\n" +
                        "            logcatE(\"onError: \" + t.getMessage());\n" +
                        "\n" +
                        "        }, () -> {\n" +
                        "            logcatE(\"onComplete\");\n" +
                        "\n" +
                        "        }, disposable -> {\n" +
                        "            logcatE(\"onSubscribe: \" + disposable);\n" +
                        "\n" +
                        "        });\n" +
                        "\n" +
                        "        behaviorSubject.onNext(\"behaviorSubject 3\");\n" +
                        "        behaviorSubject.onNext(\"behaviorSubject 4\");\n" +
                        "\n" +
                        "        // 与AsyncSubject不同，不用调用onComplete也可以发送数据\n" +
                        "//        behaviorSubject.onComplete();\n" +
                        "\n" +
                        "        behaviorSubject.onNext(\"behaviorSubject 5\");\n" +
                        "\n" +
                        "        // 可以在onComplete调用前，获取到最后一次发送的数据\n" +
                        "        logcatI(behaviorSubject.getValue());\n" +
                        "    }"
        );
    }

    private void showReplaySubject() {
        codeView.showCode(
                "    private void practice3() {\n" +
                        "        // ReplaySubject 会发射所有来自原始 Observable 的数据给观察者，无论它们是何时订阅的。\n" +
                        "        // createWithSize(n)表示只缓存订阅前最后发送的n条数据。\n" +
                        "        // ReplaySubject 除了可以限制缓存数据的数量，还能限制缓存的时间，使用 createWithTime()即可。\n" +
                        "        ReplaySubject<String> replaySubject = ReplaySubject.createWithSize(2);\n" +
                        "        replaySubject.onNext(\"replaySubject 1\");\n" +
                        "        replaySubject.onNext(\"replaySubject 2\");\n" +
                        "        replaySubject.onNext(\"replaySubject 3\");\n" +
                        "\n" +
                        "        replaySubject.subscribe(s -> {\n" +
                        "            logcatE(\"onNext: \" + s);\n" +
                        "\n" +
                        "        }, t -> {\n" +
                        "            logcatE(\"onError: \" + t.getMessage());\n" +
                        "\n" +
                        "        }, () -> {\n" +
                        "            logcatE(\"onComplete\");\n" +
                        "\n" +
                        "        }, disposable -> {\n" +
                        "            logcatE(\"onSubscribe: \" + disposable);\n" +
                        "\n" +
                        "        });\n" +
                        "\n" +
                        "        replaySubject.onNext(\"replaySubject 4\");\n" +
                        "\n" +
                        "        replaySubject.onError(new Throwable(\"测试onError\"));\n" +
                        "\n" +
                        "        // onError或onComplete是事件序列最后一个事件，执行其中一个后，另一个不会执行，且先执行onComplete后再执行onError会报错\n" +
                        "        replaySubject.onComplete();\n" +
                        "    }"
        );
    }

    private void showPublishSubject() {
        codeView.showCode(
                "    private void practice4() {\n" +
                        "        // Observer 只接收 PublishSubject 被订阅之后发送的数据\n" +
                        "        PublishSubject<String> publishSubject = PublishSubject.create();\n" +
                        "        publishSubject.onNext(\"publishSubject 1\");\n" +
                        "        publishSubject.onNext(\"publishSubject 2\");\n" +
                        "\n" +
                        "        publishSubject.subscribe(s -> {\n" +
                        "            logcatE(\"onNext: \" + s);\n" +
                        "\n" +
                        "        }, t -> {\n" +
                        "            logcatE(\"onError: \" + t.getMessage());\n" +
                        "\n" +
                        "        }, () -> {\n" +
                        "            logcatE(\"onComplete\");\n" +
                        "\n" +
                        "        }, disposable -> {\n" +
                        "            logcatE(\"onSubscribe: \" + disposable);\n" +
                        "\n" +
                        "        });\n" +
                        "\n" +
                        "        publishSubject.onNext(\"publishSubject 3\");\n" +
                        "        publishSubject.onNext(\"publishSubject 4\");\n" +
                        "        publishSubject.onComplete();\n" +
                        "    }"
        );
    }

}
