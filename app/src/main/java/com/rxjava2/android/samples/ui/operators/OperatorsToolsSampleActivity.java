package com.rxjava2.android.samples.ui.operators;

import com.rxjava2.android.samples.ui.BaseExampleActivity;

import io.reactivex.Observable;

public class OperatorsToolsSampleActivity  extends BaseExampleActivity {

    public static final String OPERATOR_REPEAT = "repeat()";

    @Override
    protected void practice() {
        switch (mOperator) {
            case OPERATOR_REPEAT:
                practiceRepeat();
                break;
            default:
                break;
        }
    }

    private void practiceRepeat() {
        // 指定重复次数
        Observable.just("Hello repeat()")
                .repeat(3)
                .subscribe(this::logcatD);
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
