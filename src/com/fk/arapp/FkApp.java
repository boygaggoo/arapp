package com.fk.arapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Application;
import android.graphics.Bitmap;

public class FkApp extends Application{

	ArrayList<String> productUrls = new ArrayList<String>();
	ArrayList<String> productPrices = new ArrayList<String>();
	ArrayList<String> productTitles = new ArrayList<String>();

	Map<String, Bitmap> cachedImagesByUrl = new HashMap<String, Bitmap>();
	Map<String, JSONArray> cachedJsonByCategory = new HashMap<String, JSONArray>();

}
