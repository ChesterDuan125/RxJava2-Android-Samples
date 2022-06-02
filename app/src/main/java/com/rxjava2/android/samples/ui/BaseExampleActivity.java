package com.rxjava2.android.samples.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.R;

import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
import static com.rxjava2.android.samples.utils.AppConstant.LINE_SEPARATOR;

public abstract class BaseExampleActivity extends AppCompatActivity {
    private String tag;
    protected Button btnDes;
    protected Button btnRunPractice;
    protected CodeView codeView;
    protected TextView tvRunPractice;
    protected View svRun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getClass().getSimpleName();
        setContentView(getLayoutId());

        initView();
    }

    protected int getLayoutId() {
        return R.layout.activity_example;
    }

    protected void initView() {
        btnDes = findViewById(R.id.btn_des);
        svRun = findViewById(R.id.sv_run);
        btnRunPractice = findViewById(R.id.btn_run_practice);

        codeView = findViewById(R.id.code_view);
        tvRunPractice = findViewById(R.id.tv_run_practice);

        codeView.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor();
        showCode();

        btnRunPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeView.setVisibility(View.GONE);
                tvRunPractice.setText("");
                svRun.setVisibility(View.VISIBLE);
                practice();
            }
        });

        btnDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svRun.setVisibility(View.GONE);
                codeView.setVisibility(View.VISIBLE);
                showCode();
            }
        });
    }

    private void showResult(String s, String color){
        tvRunPractice.post(new Runnable() {
            @Override
            public void run() {
                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
                SpannableString spannableString = new SpannableString(s);
                spannableString.setSpan(span, 0, spannableString.length(), SPAN_INCLUSIVE_EXCLUSIVE);
                tvRunPractice.append(spannableString);
                tvRunPractice.append(LINE_SEPARATOR);
            }
        });
    }

    protected void logcatD(String s) {
        Log.d(tag, s);
        showResult(s, "#0070BB");
    }

    protected void logcatE(String s) {
        Log.e(tag, s);
        showResult(s, "#FF0000");
    }

    protected void logcatI(String s) {
        Log.i(tag, s);
        showResult(s, "#48BB31");
    }

    protected abstract void practice();

    protected abstract void showCode();


}
