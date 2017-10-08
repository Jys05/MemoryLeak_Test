package com.blend.memoryleak_test;

import android.app.Activity;


import java.util.ConcurrentModificationException;
import java.util.Stack;

/**
 * Created by Sujiayong on 2017/9/30.
 * 管理APP中的生成的activity
 */

public class AppManager {
    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private AppManager() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 将Activity添加到堆栈当中
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (!activityStack.empty() && activityStack.lastElement() == activity) {
            return;
        }
        activityStack.add(activity);
    }

    /**
     * 通过类名判断某个Activity是否存在
     *
     * @param name
     * @return
     */
    public boolean exist(String name) {
        for (int i = 0; i <= activityStack.size() - 1; i++) {
            if (activityStack.get(i).getLocalClassName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过类来判断某种activity是否存在
     *
     * @param c
     * @return
     */
    public boolean exist(Class<?> c) {
        for (int i = 0; i <= activityStack.size() - 1; i++) {
            if (activityStack.get(i).getClass().getName().equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将Activity从堆栈中移除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 获取APP当前的Activity
     *
     * @return 若APP并没有启动Activity，则返回null
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.size() == 0) {
            return null;
        } else {
            return activityStack.lastElement();
        }
    }

    public <T> T getActivity(Class<T> c) {
        for (int i = 0; i <= activityStack.size() - 1; i++) {
            if (activityStack.get(i).getClass().getName().equals(c.getName())) {
                return (T) activityStack.get(i);
            }
        }
        return null;
    }

    /**
     * 结束指定的Activity,并从堆栈中移除
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束当前的activity，并从堆栈中移除
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定类名的Activity类
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }catch (ConcurrentModificationException exception){
        }

    }

    /**
     * 结束应用产生的所有Activity
     */
    public void finishAllActivity() {
        for (Activity item : activityStack) {
            if (null != item) {
                item.finish();
            }
        }
        activityStack.clear();
    }


}
