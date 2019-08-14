package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.utils.AppConstant;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class SimpleExampleActivity extends AppCompatActivity {

    private static final String TAG = SimpleExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = findViewById(R.id.btn);
        textView = findViewById(R.id.textView);
        setDes();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSomeWork();
            }
        });

    }

    /*
     * simple example to emit two value one by one
     */
    private void doSomeWork() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<String> getObservable() {
        return Observable.just("Cricket", "Football");
    }

    private Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
                textView.setText("onSubscribe");
            }

            @Override
            public void onNext(String value) {
                textView.append(" onNext : value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onNext : value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }
        };
    }


    /* ======================================= 分割线 ========================================= */

    public void practice(View view) {

        Observable.just("hello", "rxjava")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        textView.setText("onSubscribe\n");
                    }

                    @Override
                    public void onNext(String s) {
                        textView.append("onNext:" + s + "\n");

                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError\n");
                    }

                    @Override
                    public void onComplete() {
                        textView.append("onComplete\n");
                    }
                });

    }

    public void showDes(View v){
        setDes();
    }


    private void setDes(){
        textView.setText(" Rxjava入门示例：\n" +
                "\n" +
                " Observable.just(\"hello\", \"rxjava\")\n" +
                "         .subscribeOn(Schedulers.io())\n" +
                "            .observeOn(AndroidSchedulers.mainThread())\n" +
                "            .subscribe(new Observer<String>() {\n" +
                "        @Override\n" +
                "        public void onSubscribe(Disposable d) {\n" +
                "            textView.setText(\"onSubscribe\\n\");\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public void onNext(String s) {\n" +
                "            textView.append(\"onNext:\" + s + \"\\n\");\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public void onError(Throwable e) {\n" +
                "            textView.append(\"onError\\n\");\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public void onComplete() {\n" +
                "            textView.append(\"onComplete\\n\");\n" +
                "        }\n" +
                "    });\n" +
                "\n" +
                " 知识点：\n" +
                "\n" +
                " 1. Observable 被观察者\n" +
                " 2. Observer 观察者\n" +
                " 3. just() 创建操作符，创建一个被观察者，并发送事件，发送的事件最多10个，详见just()重载方法\n" +
                " 4. subscribeOn() 指定被观察者所在线程\n" +
                " 5. observeOn() 指定观察者所在线程\n" +
                " 6. subscribe() 建立订阅关系\n" +
                " 7. onSubscribe(Disposable d) 订阅事件，1次，观察者与被观察者成功建立连接时产生\n" +
                " 8. onNext(String s) 常规事件，n次\n" +
                " 9. onError(Throwable e) 异常事件,1次，如果产生，将是事件序列中的最后一个事件\n" +
                " 10. onComplete() 结束事件，1次，如果产生，将是事件序列中的最后一个事件\" \n");
    }

// Rxjava入门示例：
//
// Observable.just("hello", "rxjava")
//         .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Observer<String>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            textView.setText("onSubscribe\n");
//        }
//
//        @Override
//        public void onNext(String s) {
//            textView.append("onNext:" + s + "\n");
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            textView.append("onError\n");
//        }
//
//        @Override
//        public void onComplete() {
//            textView.append("onComplete\n");
//        }
//    });
//
// 知识点：
//
// 1. Observable 被观察者
// 2. Observer 观察者
// 3. just() 创建操作符，创建一个被观察者，并发送事件，发送的事件最多10个，详见just()重载方法
// 4. subscribeOn() 指定被观察者所在线程
// 5. observeOn() 指定观察者所在线程
// 6. subscribe() 建立订阅关系
// 7. onSubscribe(Disposable d) 订阅事件，1次，观察者与被观察者成功建立连接时产生
// 8. onNext(String s) 常规事件，n次
// 9. onError(Throwable e) 异常事件,1次，如果产生，将是事件序列中的最后一个事件
// 10. onComplete() 结束事件，1次，如果产生，将是事件序列中的最后一个事件"

}