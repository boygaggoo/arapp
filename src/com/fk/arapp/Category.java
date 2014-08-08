package com.fk.arapp;

import android.app.Activity;
import android.os.Bundle;

public class Category extends Activity {
	
	public static final String CATEGORY_KEY = "CATEGORY";
	public static final String CAT_WATCHES = "watches";
	public static final String CAT_SHADES = "shades";
	public static final String CAT_SHOES = "shoes";
	public static final String CAT_JEWELRY = "jewelry";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_listing);
	}

}
