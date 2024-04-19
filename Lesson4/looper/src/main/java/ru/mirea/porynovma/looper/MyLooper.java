package ru.mirea.porynovma.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("ru.mirea.porynovma.looper.MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                synchronized (this) {
                    String age = msg.getData().getString("age");
                    String prof = msg.getData().getString("prof");

                    try {
                        wait(Long.parseLong(age) * 1000);
                    } catch (Exception e) {
                        Log.d(MainActivity.class.getSimpleName(), e.toString());
                        return;
                    }

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString(
                            "result",
                            String.format(
                                    "Вам %s лет и ваша профессия - %s",
                                    age, prof
                            )
                    );
                    message.setData(bundle);
                    mainHandler.sendMessage(message);
                }
            }
        };
        Looper.loop();
    }
}
