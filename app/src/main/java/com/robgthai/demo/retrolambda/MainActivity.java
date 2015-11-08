package com.robgthai.demo.retrolambda;

import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.api.service.ZenService;
import com.robgthai.demo.retrolambda.dagger.DaggerZenComponent;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class MainActivity extends AppCompatActivity {

    private TextView txtHello;
    ZenService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = DaggerZenComponent.create().createService();

        txtHello = (TextView) findViewById(R.id.txtHello);
        callZen();
    }

    private void callZen() {
        service.zen()
                .doOnError(e -> showZen(e.getLocalizedMessage()))
                .subscribe(s -> showZen(s));
    }

    private void showZen(String msg) {
        txtHello.setText(msg);
    }
}
