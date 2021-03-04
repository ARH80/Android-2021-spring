package com.example.hw1.concurrence;

import android.os.Message;

public interface UiThreadCallback {
    void publishToUiThread(Message massage);
}
