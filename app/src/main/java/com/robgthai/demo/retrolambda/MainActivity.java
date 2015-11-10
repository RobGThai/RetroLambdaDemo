package com.robgthai.demo.retrolambda;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.api.service.ZenService;
import com.robgthai.demo.retrolambda.dagger.DaggerZenComponent;
import com.robgthai.utils.playservices.CustomTabsController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtHello;
    ZenService service;

    private CustomTabsController webController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = DaggerZenComponent.create().createService();

        txtHello = (TextView) findViewById(R.id.txtHello);
        callZen();

        webController = new CustomTabsController();
        webController.mayLaunch("https://www.kaidee.com/")
                        .bind(this);

        txtHello.setOnClickListener(this);
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
                showWeb("http://www.kaidee.com");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        webController.unbind(this);
    }

    private Intent getChromeIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        return intent;
    }

    private Drawable getChromeIcon() throws PackageManager.NameNotFoundException {
        return getPackageManager().getApplicationIcon("com.android.chrome");
    }

    private Bitmap getChromeIconBitmap() {
        Bitmap bitmap;
        try {
            Drawable d = getChromeIcon();
            bitmap = ((BitmapDrawable) d).getBitmap();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            bitmap = getLauncherIcon();
        }

        return bitmap;
    }

    private Bitmap getLauncherIcon() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    private void showWeb(String url) {
        Intent intent = getChromeIntent(url);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        CustomTabsIntent webIntent = new CustomTabsIntent.Builder(webController.getSession())
                .setToolbarColor(Color.GREEN)
                .setShowTitle(true)
                .setActionButton(
                        getChromeIconBitmap(),
                        "Open in Chrome",
                        pendingIntent)
                .build();
        webIntent.launchUrl(this, Uri.parse(url));
    }
}
