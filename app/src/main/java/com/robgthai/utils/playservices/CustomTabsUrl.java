package com.robgthai.utils.playservices;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Contain configuration of internet address which to be used durin pre-fetching.
 */
public class CustomTabsUrl {
    protected String url;
    protected Bundle bundle;
    protected List<Bundle> otherLikelyBundles;

    public CustomTabsUrl(@Nullable String url,
                         @Nullable Bundle bundle,
                         @Nullable List<Bundle> otherLikelyBundles) {
        this.url = url;
        this.bundle = bundle;
        this.otherLikelyBundles = otherLikelyBundles;
    }
}
