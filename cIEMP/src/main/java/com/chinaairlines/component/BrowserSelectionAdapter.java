package com.chinaairlines.component;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaairlines.ciemp.R;

import net.openid.appauth.browser.BrowserDescriptor;
import net.openid.appauth.browser.BrowserSelector;

import java.util.ArrayList;
import java.util.List;

public final class BrowserSelectionAdapter extends BaseAdapter {

    private static final int LOADER_ID = 101;

    private Context mContext;
    private ArrayList<BrowserInfo> mBrowsers;
    private static String pkgname = "";

    /**
     * Creates the adapter, using the loader manager from the specified activity.
     */
    public BrowserSelectionAdapter(@NonNull final Activity activity) {
        mContext = activity;
        pkgname = mContext.getPackageName();
        if (Variable.isDebug) {
            Log.e("Package name 1", " "+pkgname);
        }
        initializeItemList();
        activity.getLoaderManager().initLoader(
                LOADER_ID,
                null,
                new BrowserLoaderCallbacks());
    }

    public static final class BrowserInfo {
        public final BrowserDescriptor mDescriptor;
        public final CharSequence mLabel;
        public final Drawable mIcon;

        BrowserInfo(BrowserDescriptor descriptor, CharSequence label, Drawable icon) {
            mDescriptor = descriptor;
            mLabel = label;
            mIcon = icon;
        }
    }

    @Override
    public int getCount() {
        return mBrowsers.size();
    }

    @Override
    public BrowserInfo getItem(int position) {
        return mBrowsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.browser_selector_layout, parent, false);
        }

        BrowserInfo info = mBrowsers.get(position);

        TextView labelView = (TextView) convertView.findViewById(R.id.browser_label);
        ImageView iconView = (ImageView) convertView.findViewById(R.id.browser_icon);
        if (info == null) {
            labelView.setText("AppAuth heuristic selection");
            iconView.setImageResource(R.drawable.appauth_96dp);
        } else {
            CharSequence label = info.mLabel;
            if (info.mDescriptor.useCustomTab) {
                label = String.format(mContext.getString(R.string.custom_tab_label), label);
            }
            labelView.setText(label);
            iconView.setImageDrawable(info.mIcon);
        }

        return convertView;
    }

    private void initializeItemList() {
        mBrowsers = new ArrayList<>();
        mBrowsers.add(null);
    }

    private final class BrowserLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<BrowserInfo>> {

        @Override
        public Loader<List<BrowserInfo>> onCreateLoader(int id, Bundle args) {
            return new BrowserLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<List<BrowserInfo>> loader, List<BrowserInfo> data) {
            initializeItemList();
            mBrowsers.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<List<BrowserInfo>> loader) {
            initializeItemList();
            notifyDataSetChanged();
        }
    }

    private static class BrowserLoader extends AsyncTaskLoader<List<BrowserInfo>> {

        private List<BrowserInfo> mResult;

        BrowserLoader(Context context) {
            super(context);
        }

        @Override
        public List<BrowserInfo> loadInBackground() {
            List<BrowserDescriptor> descriptors = BrowserSelector.getAllBrowsers(getContext());
            ArrayList<BrowserInfo> infos = new ArrayList<>(descriptors.size());

            PackageManager pm = getContext().getPackageManager();
            for (BrowserDescriptor descriptor : descriptors) {
                try {
                    ApplicationInfo info = pm.getApplicationInfo(descriptor.packageName, 0);
                    CharSequence label = pm.getApplicationLabel(info);
                    Drawable icon = pm.getApplicationIcon(descriptor.packageName);
                    if (info.packageName.equals(pkgname)) {
                        if (Variable.isDebug) {
                            Log.e("Package name", info.packageName+" "+pkgname);
                        }
                        infos.add(new BrowserInfo(descriptor, label, icon));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    infos.add(new BrowserInfo(descriptor, descriptor.packageName, null));
                }
            }

            return infos;
        }

        @Override
        public void deliverResult(List<BrowserInfo> data) {
            if (isReset()) {
                mResult = null;
                return;
            }

            mResult = data;
            super.deliverResult(mResult);
        }

        @Override
        protected void onStartLoading() {
            if (mResult != null) {
                deliverResult(mResult);
            }
            forceLoad();
        }

        @Override
        protected void onReset() {
            mResult = null;
        }

        @Override
        public void onCanceled(List<BrowserInfo> data) {
            mResult = null;
            super.onCanceled(data);
        }
    }
}
