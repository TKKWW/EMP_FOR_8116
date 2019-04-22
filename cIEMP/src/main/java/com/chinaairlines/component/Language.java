package com.chinaairlines.component;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Language {

	public void setLanguage(Context context, Locale locale) {
		// Force locale
		Resources res = context.getResources();
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(conf, dm);
	}
	
}
