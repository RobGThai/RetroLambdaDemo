package com.robgthai.demo.retrolambda;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.api.service.GitHubService;
import com.github.api.service.ServiceBuilder;
import com.github.api.service.ZenService;

import java.io.IOException;
import java.util.concurrent.Future;

import retrofit.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class MainActivity extends AppCompatActivity {

    private Handler backgroundHandler;
    private TextView txtHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtHello = (TextView) findViewById(R.id.txtHello);
        setupBackgroundThread();
        callZen();
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

    private void callZen() {
        ZenService service = new ZenService(new ServiceBuilder().get());
        service.zen()
                .doOnError(e -> showZen(e.getLocalizedMessage()))
                .subscribe(s -> showZen(s));

    }

    private void showZen(String msg) {
        txtHello.setText(msg);
    }

    private void testObservable() {
        Observable.just("1", "2", "3", "4")
                  .subscribeOn(HandlerScheduler.from(backgroundHandler))
                  .observeOn(AndroidSchedulers.mainThread())
                  .map(s -> Integer.parseInt(s))
                  .subscribe(i -> Log.d("Test", "Testing: " + (i + 1)));
    }
}
