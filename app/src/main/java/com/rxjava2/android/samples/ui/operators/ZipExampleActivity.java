package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.utils.AppConstant;
import com.rxjava2.android.samples.utils.Utils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class ZipExampleActivity extends AppCompatActivity {

    private static final String TAG = ZipExampleActivity.class.getSimpleName();
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
     * Here we are getting two user list
     * One, the list of cricket fans
     * Another one, the list of football fans
     * Then we are finding the list of users who loves both
     */
    private void doSomeWork() {
        Observable.zip(getCricketFansObservable(), getFootballFansObservable(),
                new BiFunction<List<User>, List<User>, List<User>>() {
                    @Override
                    public List<User> apply(List<User> cricketFans, List<User> footballFans) {
                        return Utils.filterUserWhoLovesBoth(cricketFans, footballFans);
                    }
                })
//                // Run on a background thread
//                .subscribeOn(Schedulers.io())
//                // Be notified on the main thread
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<List<User>> getCricketFansObservable() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) {
                textView.append("getCricketFansObservable  subscribe \n");
                if (!e.isDisposed()) {
                    textView.append("getCricketFansObservable  onNext \n");
                    e.onNext(Utils.getUserListWhoLovesCricket());

                    textView.append("getCricketFansObservable  onComplete \n");
                    e.onComplete();
                }
            }
        });
    }

    private Observable<List<User>> getFootballFansObservable() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) {
                textView.append("getFootballFansObservable  subscribe \n");
                if (!e.isDisposed()) {
                    textView.append("getFootballFansObservable  onNext \n");
                    e.onNext(Utils.getUserListWhoLovesFootball());

                    textView.append("getFootballFansObservable  onComplete \n");
                    e.onComplete();
                }
            }
        });
    }

    private Observer<List<User>> getObserver() {
        return new Observer<List<User>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
                textView.setText("onSubscribe");
            }

            @Override
            public void onNext(List<User> userList) {
                textView.append(" onNext");
                textView.append(AppConstant.LINE_SEPARATOR);
                for (User user : userList) {
                    textView.append(" firstname : " + user.firstname);
                    textView.append(AppConstant.LINE_SEPARATOR);
                }
                Log.d(TAG, " onNext : " + userList.size());
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


    public void practice(View view){
        Observable.zip(getCricketFansObservable(), getFootballFansObservable(), new BiFunction<List<User>, List<User>, List<User>>() {
            @Override
            public List<User> apply(List<User> users, List<User> users2) throws Exception {
                textView.append("BiFunction apply \n" );
                return Utils.filterUserWhoLovesBoth(users, users2);
            }
        })
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        textView.setText("onSubcribe :  isDespond = " + d.isDisposed() + "\n");
                    }

                    @Override
                    public void onNext(List<User> users) {
                        textView.append("onNext \n");

                        for (User user:users) {
                            textView.append("onNext : " + user.firstname + "\n");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError : " + e.toString() + "\n");
                    }

                    @Override
                    public void onComplete() {
                        textView.append("onComplete \n");
                    }
                });

    }

    public void showDes(View view){
        setDes();
    }

    private void setDes(){
        textView.setText("");
    }


//
//    组合操作符
// 会将多个被观察者合并，根据各个被观察者发送事件的顺序一个个结合起来，最终发送的事件数量会与源 Observable 中最少事件的数量一样


}