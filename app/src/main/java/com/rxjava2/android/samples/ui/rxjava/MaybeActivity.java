package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MaybeActivity extends BaseExampleActivity {

    @Override
    protected void practice() {
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

    @Override
    protected void showCode() {

    }
}
