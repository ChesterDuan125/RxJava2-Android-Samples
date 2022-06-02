package com.rxjava2.android.samples.ui.rxjava;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

public class SingleActivity extends BaseExampleActivity {

    @Override
    protected void practice() {
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

    @Override
    protected void showCode() {
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
}
