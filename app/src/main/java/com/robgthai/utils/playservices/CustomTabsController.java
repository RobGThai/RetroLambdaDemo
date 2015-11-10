package com.robgthai.utils.playservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.ArraySet;
import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomTabsController {

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private CustomTabsCallback mConnectionCallback;

    private Set<CustomTabsUrl> urls;

    public CustomTabsController() {
        urls = new HashSet<>();
    }

    public CustomTabsController mayLaunch(String url) {
        urls.add(new CustomTabsUrl(url, null, null));

        return this;
    }

    public CustomTabsController mayLaunch(CustomTabsUrl url) {
        urls.add(url);

        return this;
    }

    private void preFetch() {
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

        for(CustomTabsUrl url : urls) {
            mCustomTabsSession.mayLaunchUrl(
                    Uri.parse(url.getUrl()), url.getBundle(), url.getOtherLikelyBundles());
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

    public void bind(Context c) {
        String packageName = "com.android.chrome";
        mConnection = createConnection();
        CustomTabsClient.bindCustomTabsService(c,
                packageName,
                mConnection);
    }

    public void unbind(ContextWrapper wrapper) {
        if (mConnection == null)
            return;

        wrapper.unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    public CustomTabsSession getSession() {
        return mCustomTabsSession;
    }

    class CustomTabsUrl {
        String url;
        Bundle bundle;
        List<Bundle> otherLikelyBundles;

        public CustomTabsUrl(String url, Bundle bundle, List<Bundle> otherLikelyBundles) {
            this.url = url;
            this.bundle = bundle;
            this.otherLikelyBundles = otherLikelyBundles;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }

        public List<Bundle> getOtherLikelyBundles() {
            return otherLikelyBundles;
        }

        public void setOtherLikelyBundles(List<Bundle> otherLikelyBundles) {
            this.otherLikelyBundles = otherLikelyBundles;
        }
    }
}
