package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.model.ApiUser;
import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.utils.AppConstant;
import com.rxjava2.android.samples.utils.Utils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class MapExampleActivity extends AppCompatActivity {

    private static final String TAG = MapExampleActivity.class.getSimpleName();
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
     * Here we are getting ApiUser Object from api server
     * then we are converting it into User Object because
     * may be our database support User Not ApiUser Object
     * Here we are using Map Operator to do that
     */
    private void doSomeWork() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<ApiUser>, List<User>>() {

                    @Override
                    public List<User> apply(List<ApiUser> apiUsers) {
                        return Utils.convertApiUserListToUserList(apiUsers);
                    }
                })
                .subscribe(getObserver());
    }

    private Observable<List<ApiUser>> getObservable() {
        return Observable.create(new ObservableOnSubscribe<List<ApiUser>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ApiUser>> e) {
                if (!e.isDisposed()) {
                    e.onNext(Utils.getApiUserList());
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


    public void practice(View view) {

        Observable.create(new ObservableOnSubscribe<List<ApiUser>>() {

            @Override
            public void subscribe(ObservableEmitter<List<ApiUser>> emitter) throws Exception {
                textView.append("subscribe: emitter.isDisposed() = " + emitter.isDisposed() + "\n");

                if (!emitter.isDisposed()) {
                    textView.append("subscribe: emitter.onNext() " + "\n");
                    emitter.onNext(Utils.getApiUserList());

                    textView.append("subscribe: emitter.onComplete() " + "\n");
                    emitter.onComplete();
                }
            }
        })
                .map(new Function<List<ApiUser>, List<User>>() {
                    @Override
                    public List<User> apply(List<ApiUser> apiUsers) throws Exception {
                        return Utils.convertApiUserListToUserList(apiUsers);
                    }
                })
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        textView.setText("onSubscribe : isDisposable = " + d.isDisposed() + "\n");
                    }

                    @Override
                    public void onNext(List<User> users) {
                        textView.append("onNext" + "\n");

                        for (User user : users) {
                            textView.append("onNext: " + user.toString() + "\n");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError: e = " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        textView.append("onCompleted");
                    }
                });
    }

    public void showDes(View view) {
        setDes();
    }

    private void setDes() {
        textView.setText(" create、map操作符示例：\n" +
                "\n" +
                " Observable.create(new ObservableOnSubscribe<List<ApiUser>>() {\n" +
                "\n" +
                "            @Override\n" +
                "            public void subscribe(ObservableEmitter<List<ApiUser>> emitter) throws Exception {\n" +
                "                textView.append(\"subscribe: emitter.isDisposed() = \" + emitter.isDisposed() + \"\\n\");\n" +
                "\n" +
                "                if (!emitter.isDisposed()) {\n" +
                "                    textView.append(\"subscribe: emitter.onNext() \" + \"\\n\");\n" +
                "                    emitter.onNext(Utils.getApiUserList());\n" +
                "\n" +
                "                    textView.append(\"subscribe: emitter.onComplete() \" + \"\\n\");\n" +
                "                    emitter.onComplete();\n" +
                "                }\n" +
                "            }\n" +
                "        })\n" +
                "                .map(new Function<List<ApiUser>, List<User>>() {\n" +
                "                    @Override\n" +
                "                    public List<User> apply(List<ApiUser> apiUsers) throws Exception {\n" +
                "                        return Utils.convertApiUserListToUserList(apiUsers);\n" +
                "                    }\n" +
                "                })\n" +
                "                .subscribe(new Observer<List<User>>() {\n" +
                "                    @Override\n" +
                "                    public void onSubscribe(Disposable d) {\n" +
                "                        textView.setText(\"onSubscribe : isDisposable = \" + d.isDisposed() + \"\\n\");\n" +
                "                    }\n" +
                "\n" +
                "                    @Override\n" +
                "                    public void onNext(List<User> users) {\n" +
                "                        textView.append(\"onNext\" + \"\\n\");\n" +
                "\n" +
                "                        for (User user : users) {\n" +
                "                            textView.append(\"onNext: \" + user.toString() + \"\\n\");\n" +
                "                        }\n" +
                "                    }\n" +
                "\n" +
                "                    @Override\n" +
                "                    public void onError(Throwable e) {\n" +
                "                        textView.append(\"onError: e = \" + e.toString());\n" +
                "                    }\n" +
                "\n" +
                "                    @Override\n" +
                "                    public void onComplete() {\n" +
                "                        textView.append(\"onCompleted\");\n" +
                "                    }\n" +
                "                });\n" +
                "\n" +
                "    public static List<ApiUser> getApiUserList() {\n" +
                "\n" +
                "        List<ApiUser> apiUserList = new ArrayList<>();\n" +
                "\n" +
                "        ApiUser apiUserOne = new ApiUser();\n" +
                "        apiUserOne.firstname = \"Amit\";\n" +
                "        apiUserOne.lastname = \"Shekhar\";\n" +
                "        apiUserList.add(apiUserOne);\n" +
                "\n" +
                "        ApiUser apiUserTwo = new ApiUser();\n" +
                "        apiUserTwo.firstname = \"Manish\";\n" +
                "        apiUserTwo.lastname = \"Kumar\";\n" +
                "        apiUserList.add(apiUserTwo);\n" +
                "\n" +
                "        ApiUser apiUserThree = new ApiUser();\n" +
                "        apiUserThree.firstname = \"Sumit\";\n" +
                "        apiUserThree.lastname = \"Kumar\";\n" +
                "        apiUserList.add(apiUserThree);\n" +
                "\n" +
                "        return apiUserList;\n" +
                "    }\n" +
                "\n" +
                "    public static List<User> convertApiUserListToUserList(List<ApiUser> apiUserList) {\n" +
                "\n" +
                "        List<User> userList = new ArrayList<>();\n" +
                "\n" +
                "        for (ApiUser apiUser : apiUserList) {\n" +
                "            User user = new User();\n" +
                "            user.firstname = apiUser.firstname;\n" +
                "            user.lastname = apiUser.lastname;\n" +
                "            userList.add(user);\n" +
                "        }\n" +
                "\n" +
                "        return userList;\n" +
                "    }\n" +
                "\n" +
                " 知识点：\n" +
                "\n" +
                " 1. create() 创建操作符，创建一个被观察者，接受ObservableOnSubscribe<T>对象，实现subscribe(@NonNull ObservableEmitter<T> emitter)方法\n" +
                " 2. ObservableEmitter<T> extends Emitter<T> ，发射器，用于向下游发送事件; isDisposed():查询是否解除订阅 true 代表 已经解除订阅\n" +
                " 3. map() 转换操作符，可以将被观察者发送的数据类型转变成其他的类型，接受Function<T, R>对象，实现R apply(@NonNull T t)方法\n");
    }


// create、map操作符示例：
//
// Observable.create(new ObservableOnSubscribe<List<ApiUser>>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<List<ApiUser>> emitter) throws Exception {
//                textView.append("subscribe: emitter.isDisposed() = " + emitter.isDisposed() + "\n");
//
//                if (!emitter.isDisposed()) {
//                    textView.append("subscribe: emitter.onNext() " + "\n");
//                    emitter.onNext(Utils.getApiUserList());
//
//                    textView.append("subscribe: emitter.onComplete() " + "\n");
//                    emitter.onComplete();
//                }
//            }
//        })
//                .map(new Function<List<ApiUser>, List<User>>() {
//                    @Override
//                    public List<User> apply(List<ApiUser> apiUsers) throws Exception {
//                        return Utils.convertApiUserListToUserList(apiUsers);
//                    }
//                })
//                .subscribe(new Observer<List<User>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        textView.setText("onSubscribe : isDisposable = " + d.isDisposed() + "\n");
//                    }
//
//                    @Override
//                    public void onNext(List<User> users) {
//                        textView.append("onNext" + "\n");
//
//                        for (User user : users) {
//                            textView.append("onNext: " + user.toString() + "\n");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        textView.append("onError: e = " + e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        textView.append("onCompleted");
//                    }
//                });
//
//    public static List<ApiUser> getApiUserList() {
//
//        List<ApiUser> apiUserList = new ArrayList<>();
//
//        ApiUser apiUserOne = new ApiUser();
//        apiUserOne.firstname = "Amit";
//        apiUserOne.lastname = "Shekhar";
//        apiUserList.add(apiUserOne);
//
//        ApiUser apiUserTwo = new ApiUser();
//        apiUserTwo.firstname = "Manish";
//        apiUserTwo.lastname = "Kumar";
//        apiUserList.add(apiUserTwo);
//
//        ApiUser apiUserThree = new ApiUser();
//        apiUserThree.firstname = "Sumit";
//        apiUserThree.lastname = "Kumar";
//        apiUserList.add(apiUserThree);
//
//        return apiUserList;
//    }
//
//    public static List<User> convertApiUserListToUserList(List<ApiUser> apiUserList) {
//
//        List<User> userList = new ArrayList<>();
//
//        for (ApiUser apiUser : apiUserList) {
//            User user = new User();
//            user.firstname = apiUser.firstname;
//            user.lastname = apiUser.lastname;
//            userList.add(user);
//        }
//
//        return userList;
//    }
//
// 知识点：
//
// 1. create() 创建操作符，创建一个被观察者，接受ObservableOnSubscribe<T>对象，实现subscribe(@NonNull ObservableEmitter<T> emitter)方法
// 2. ObservableEmitter<T> extends Emitter<T> ，发射器，用于向下游发送事件; isDisposed():查询是否解除订阅 true 代表 已经解除订阅
// 3. map() 转换操作符，可以将被观察者发送的数据类型转变成其他的类型，接受Function<T, R>对象，实现R apply(@NonNull T t)方法
//

}