package com.example.hw1.concurrence;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.lang.ref.WeakReference;

public class CustomHandlerThread extends HandlerThread {

    CustomHandler customHandler;
    private WeakReference<UiThreadCallback> uiThreadCallback;

    public CustomHandlerThread(String name) {
        super(name, Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        customHandler = new CustomHandler(getLooper());
    }

    public void addMessage(int message) {
        if (customHandler != null) {
            customHandler.sendEmptyMessage(message);
        }
    }

    public void postRunnable(Runnable runnable){
        if(customHandler != null) {
            customHandler.post(runnable);
        }
    }

    public void setUiThreadCallback(UiThreadCallback callback) {
        this.uiThreadCallback = new WeakReference<>(callback);
    }

    private class CustomHandler extends Handler {
        public CustomHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        Thread.sleep(1000);
                        if(!Thread.interrupted() && uiThreadCallback != null && uiThreadCallback.get() != null){
                            Message message = Util.createMessage(Util.MESSAGE_ID, "Thread " + String.valueOf(Thread.currentThread().getId()) + " completed");
                            uiThreadCallback.get().publishToUiThread(message);
                        }
                    } catch (InterruptedException e){
                        Log.e(Util.LOG_TAG,"HandlerThread interrupted");
                    }
                    break;
                default:
                    break;
            }
        }

    }
}
