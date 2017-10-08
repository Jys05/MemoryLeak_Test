package com.blend.memoryleak_test;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView tv_timeTip;
    private int time = 0;
    private int time2 = 0;
    private TextView tv_timeTip2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().add(this);
        setContentView(R.layout.activity_second);
        initView();
    }

    public void onMemoryLeak(View view) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_timeTip.setText(String.format(getString(R.string.timeTip), "" + time++));
                handler.postDelayed(this, 1000);
            }
        });
    }

    private Handler mHandler = new Handler();

    public void onNotMemoryLeak(View veiw) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tv_timeTip2.setText(String.format(getString(R.string.timeTip), "" + time2++));
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickToast(View view) {
        ToastUtil.show("请查看是否内存泄漏");
    }

    public void onClickGetHeapSize(View view) {
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        int heapSize = activityManager.getMemoryClass();
//        ToastUtil.show("系统剩余内存:" + (info.availMem >> 10) + "k");
        ToastUtil.show("HeapSize阈值:" + heapSize + "M");

    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance().remove(this);
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    private void initView() {
        tv_timeTip = (TextView) findViewById(R.id.tv_timeTip);
        tv_timeTip2 = (TextView) findViewById(R.id.tv_timeTip2);
    }
}
