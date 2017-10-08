package com.blend.memoryleak_test;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;


/**
 * Created by Sujiayong on 2017/9/30.
 * Toast工具
 * 管理Toast
 */

public class ToastUtil {

    private static Toast mInstance = null;
    private static Activity mCurrentActivity = null;

    /**
     * @param content
     */
    public static void show(String content) {
        Activity temp = AppManager.getInstance().currentActivity();
        if (temp == null) {
            return;
        }
        if (temp != mCurrentActivity) {
            mCurrentActivity = temp;
            mInstance = null;
        }
        if (mInstance == null) {
            mInstance = Toast.makeText(mCurrentActivity, content, Toast.LENGTH_SHORT);
        } else {
            mInstance.setText(content);
        }
        mInstance.show();
//        //清理对象——为了防止内存泄漏
//        setObjectEmpty();
    }

    /**
     * @param content
     */
    public static void showForMain(final String content) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                show(content);
            }
        });
    }

    public static void show(int code, String message) {
        show(code + "    " + message);
    }

    /**
     * @param resId
     */
    public static void show(@StringRes int resId) {
        Activity temp = AppManager.getInstance().currentActivity();
        if (temp == null) {
            return;
        }
        if (temp != mCurrentActivity) {
            mCurrentActivity = temp;
            mInstance = null;
        }
        if (mInstance == null) {
            mInstance = Toast.makeText(mCurrentActivity, resId, Toast.LENGTH_SHORT);
        } else {
            mInstance.setText(resId);
        }
        mInstance.show();
//        //清理对象——为了防止内存泄漏
//        setObjectEmpty();
    }


    /**
     * 清理对象——为了防止内存泄漏
     */
    private static void setObjectEmpty(){
        mCurrentActivity = null;
        mInstance = null;
    }
}
