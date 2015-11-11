package com.robgthai.utils.playservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to open Custom Chrome Tabs (ala WebView).
 */
public class CustomTabsController {

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;

    private Set<CustomTabsUrl> urls;

    public CustomTabsController() {
        urls = new HashSet<>();
    }

    /**
     * Add url to the page you expect user to open.
     * Added url is to be pre-fetched.
     * @param url
     * @return this
     */
    public CustomTabsController mayLaunch(@NonNull String url) {
        mayLaunch(new CustomTabsUrl(url, null, null));

        return this;
    }

    /**
     * Add fully configured {@link CustomTabsUrl} to be pre-fetched.
     * @param url
     * @return this
     * @see CustomTabsUrl
     */
    public CustomTabsController mayLaunch(@NonNull CustomTabsUrl url) {
        urls.add(url);

        return this;
    }

    /**
     * Create new session and pre-fetch data from supplied urls.
     */
    private void preFetch() {
        CustomTabsCallback mConnectionCallback = new CustomTabsCallback() {
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

        for(CustomTabsUrl url : urls) {
            mCustomTabsSession.mayLaunchUrl(
                    Uri.parse(url.url), url.bundle, url.otherLikelyBundles);
        }

    }

    private CustomTabsServiceConnection createConnection() {
        return new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                Log.d("ChromeCustom", "Connected");
                mClient = customTabsClient;
                mClient.warmup(0L);
                preFetch();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("ChromeCustom", "Disconnected");
            }
        };
    }

    /**
     * Bind local client to {@link android.support.customtabs.CustomTabsService}.
     * @param c
     */
    public void bind(@NonNull Context c) {
        String packageName = "com.android.chrome";
        mConnection = createConnection();
        CustomTabsClient.bindCustomTabsService(c,
                packageName,
                mConnection);
    }

    /**
     * Unbind service.
     * @param wrapper
     */
    public void unbind(@NonNull ContextWrapper wrapper) {
        if (mConnection == null)
            return;

        wrapper.unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    /**
     * Return current session.
     * @return
     */
    public CustomTabsSession getSession() {
        return mCustomTabsSession;
    }

}
