package com.rxjava2.android.samples.ui.operators;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class OperatorsToolsSampleActivity  extends BaseExampleActivity {

    public static final String OPERATOR_REPEAT = "repeat()";
    public static final String OPERATOR_REPEAT_WHEN = "repeatWhen()";
    public static final String OPERATOR_REPEAT_UNTIL = "repeatUntil()";

    @Override
    protected void practice() {
        switch (mOperator) {
            case OPERATOR_REPEAT:
                practiceRepeat();
                break;
            case OPERATOR_REPEAT_WHEN:
                practiceRepeatWhen();
                break;
            case OPERATOR_REPEAT_UNTIL:
                practiceRepeatUntil();
                break;
            default:
                break;
        }
    }


    private void practiceRepeat() {
        // 指定重复次数
        Observable.just("Hello repeat()")
                .repeat(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        logcatE("onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        logcatD("onNext: " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        logcatE("onError");
                    }

                    @Override
                    public void onComplete() {
                        logcatE("onComplete");
                    }
                });
    }

    private void practiceRepeatWhen() {
        Observable<Integer> observable = Observable.range(0, 3);
        logcatI("observalbe: " + observable);

        Observable<Integer> observable1 = observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                logcatD("apply: " + objectObservable);
                // 5秒后
                return Observable.timer(5, TimeUnit.SECONDS);
            }
        });
        logcatI("observalbe1: " + observable1);

        observable1.subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        logcatE("onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        logcatD("onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        logcatE("onError");
                    }

                    @Override
                    public void onComplete() {
                        logcatE("onComplete");
                    }
                });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void practiceRepeatUntil() {

    }



    @Override
    protected void showCode() {
        switch (mOperator) {
            case OPERATOR_REPEAT:
                showRepeat();
                break;
            default:
                break;
        }
    }

    private void showRepeat() {
        codeView.showCode(
                "    private void practiceRepeat() {\n" +
                        "        // 指定重复次数\n" +
                        "        Observable.just(\"Hello repeat()\")\n" +
                        "                .repeat(3)\n" +
                        "                .subscribe(this::logcatD);\n" +
                        "    }"
        );
    }
}
