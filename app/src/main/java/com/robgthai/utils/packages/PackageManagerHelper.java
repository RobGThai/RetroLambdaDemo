package com.robgthai.utils.packages;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.robgthai.demo.retrolambda.R;

/**
 * Used to help retrieve other installed applications resources and Intents.
 */
public class PackageManagerHelper {

    private static final String CHROME = "com.android.chrome";

    public Intent getChromeIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(CHROME);
        return intent;
    }

    public Drawable getChromeIcon(ContextWrapper cw) throws PackageManager.NameNotFoundException {
        return cw.getPackageManager().getApplicationIcon(CHROME);
    }

    public Bitmap getChromeIconBitmap(ContextWrapper cw) {
        Bitmap bitmap;
        try {
            Drawable d = getChromeIcon(cw);
            bitmap = ((BitmapDrawable) d).getBitmap();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            bitmap = getLauncherIcon(cw);
        }

        return bitmap;
    }

    public Bitmap getLauncherIcon(ContextWrapper cw) {
        return BitmapFactory.decodeResource(cw.getResources(), R.mipmap.ic_launcher);
    }
}
