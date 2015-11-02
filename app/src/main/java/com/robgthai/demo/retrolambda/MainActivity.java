package com.robgthai.demo.retrolambda;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class MainActivity extends AppCompatActivity {

    private Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBackgroundThread();
        testObservable();
    }

    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }

    private void setupBackgroundThread() {
        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void testObservable() {
        Observable.just("1", "2", "3", "4")
                  .subscribeOn(HandlerScheduler.from(backgroundHandler))
                  .observeOn(AndroidSchedulers.mainThread())
                  .map(s -> Integer.parseInt(s))
                  .subscribe(i -> Log.d("Test", "Testing: " + (i + 1)));
    }
}
