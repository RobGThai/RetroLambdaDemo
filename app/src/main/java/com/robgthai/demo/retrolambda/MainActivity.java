package com.robgthai.demo.retrolambda;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.api.service.ZenService;
import com.robgthai.demo.retrolambda.dagger.DaggerZenComponent;
import com.robgthai.utils.packages.PackageManagerHelper;
import com.robgthai.utils.playservices.CustomTabsController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtHello;
    ZenService service;

    private CustomTabsController webController;
    private PackageManagerHelper packageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = DaggerZenComponent.create().createService();

        txtHello = (TextView) findViewById(R.id.txtHello);
        callZen();

        webController = initCustomTabsController();
        packageHelper = new PackageManagerHelper();

        txtHello.setOnClickListener(this);
    }

    private CustomTabsController initCustomTabsController() {
        CustomTabsController webController = new CustomTabsController();
        webController.mayLaunch("https://www.kaidee.com/")
                .bind(this);
        return webController;
    }

    private void callZen() {
        service.zen()
                .doOnError(e -> showZen(e.getLocalizedMessage()))
                .subscribe(s -> showZen(s)); // Will come back to look at this later.
    }

    private void showZen(String msg) {
        txtHello.setText(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHello:
                showWeb("https://www.kaidee.com");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        webController.unbind(this);
    }

    private void showWeb(String url) {
        Intent intent = packageHelper.getChromeIntent(url);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        CustomTabsIntent webIntent = new CustomTabsIntent.Builder(webController.getSession())
                .setToolbarColor(Color.GREEN)
                .setShowTitle(true)
                .setActionButton(
                        packageHelper.getChromeIconBitmap(this),
                        "Open in Chrome",
                        pendingIntent)
                .build();
        webIntent.launchUrl(this, Uri.parse(url));
    }
}
