package com.robgthai.demo.retrolambda;

import android.content.ComponentName;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.api.service.ZenService;
import com.robgthai.demo.retrolambda.dagger.DaggerZenComponent;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtHello;
    ZenService service;

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private CustomTabsCallback mConnectionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = DaggerZenComponent.create().createService();

        txtHello = (TextView) findViewById(R.id.txtHello);
        callZen();

        setupWeb();
        txtHello.setOnClickListener(this);
    }

    private void callZen() {
        service.zen()
                .doOnError(e -> showZen(e.getLocalizedMessage()))
                .subscribe(s -> showZen(s));
    }

    private void showZen(String msg) {
        txtHello.setText(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHello:
                showWeb("http://www.kaidee.com");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindCustomTabs();
    }

    private void unbindCustomTabs() {
        if (mConnection == null)
            return;

        this.unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    private void setupWeb() {
        String packageName = "com.android.chrome";
        mConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                Log.d("ChromeCustom", "Connected");
                mClient = customTabsClient;
                mClient.warmup(0L);
                prefetchWebContent();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("ChromeCustom", "Disconnected");
            }
        };

        CustomTabsClient.bindCustomTabsService(this,
                packageName,
                mConnection);
    }

    private void prefetchWebContent() {
        mConnectionCallback = new CustomTabsCallback() {
            @Override
            public void extraCallback(String callbackName, Bundle args) {
                super.extraCallback(callbackName, args);
                Log.d("ChromeCustom", "extra callback " + callbackName);
            }

            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
                Log.d("ChromeCustom", "navigationEvent " + navigationEvent);
            }
        };

        mCustomTabsSession = mClient.newSession(mConnectionCallback);
        mCustomTabsSession.mayLaunchUrl(
            Uri.parse("http://www.kaidee.com"), null, null);
    }


    private void showWeb(String url) {
        CustomTabsIntent intent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setToolbarColor(Color.GREEN)
                .setShowTitle(false)
                .build();
        intent.launchUrl(this, Uri.parse(url));

    }
}
