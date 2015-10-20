package com.deal.monk;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class DealMonkApplication extends Application {

	public static ImageLoader imageLoader;
	public static Typeface fontHero;

	@Override
	public void onCreate() {
		super.onCreate();

//		fontHero = Typeface.createFromAsset(getAssets(),Constants.FONT_HERO);

	}

	public static ImageLoader getImageLoader(Context context) {

		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(getConfiguration(context));
		}

		return imageLoader;
	}

	private static ImageLoaderConfiguration getConfiguration(Context context) {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();

		return config;
	}
}
