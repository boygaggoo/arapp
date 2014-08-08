package com.fk.arapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fk.arapp.RestClient.RequestMethod;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
//import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.StrictMode;

public class Listing extends Activity implements SurfaceHolder.Callback,
		View.OnClickListener {

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	ArrayList<String> imageUrls = new ArrayList<String>();
	private JSONArray jsonArray;
	private Integer currentProductIndex;
	FkApp fkApp;

	Map<String, String> cameraTypeMapping = new HashMap<String,String>();
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listing_main);

		initializeCameraTypeMapping();

		fkApp = (FkApp) getApplicationContext();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.listing_control,
				null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);
		bindNavButtons();
	}
	
	private void initializeCameraTypeMapping() {
		this.cameraTypeMapping.put(Category.CAT_WATCHES, "back");
		this.cameraTypeMapping.put(Category.CAT_SHADES, "front");
		this.cameraTypeMapping.put(Category.CAT_SHOES, "back");
		this.cameraTypeMapping.put(Category.CAT_JEWELRY, "front");
	}

	private void bindNavButtons() {
		View prevBtn = findViewById(R.id.previous);
		prevBtn.setOnClickListener(this);
		View nextBtn = findViewById(R.id.next);
		nextBtn.setOnClickListener(this);
		View addToCartBtn = findViewById(R.id.add_to_cart);
		addToCartBtn.setOnClickListener(this);
		View checkoutBtn = findViewById(R.id.checkout);
		checkoutBtn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadProducts();
		currentProductIndex = 0;
		Log.d("SHOPCUR", currentProductIndex.toString());
		displayProduct();
	}

	private void displayProduct() {
		if (jsonArray != null) {
			try {

				String imageUrl = ((JSONObject)jsonArray.get(currentProductIndex)).getString("url");
				ImageView imageView = (ImageView) findViewById(R.id.viewfinder_view);
				imageView.setImageBitmap(getImage(imageUrl));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Bitmap getImage(String url) {
		if(!fkApp.cachedImagesByUrl.containsKey(url)){
			Bitmap bitmap = getBitmapFromURL(url);
			fkApp.cachedImagesByUrl.put(url, bitmap);
    	}
		return fkApp.cachedImagesByUrl.get(url);
	}

	private void loadProducts() {
		try {
			RestClient client = new RestClient(
					"http://sleepy-retreat-2061.herokuapp.com/products");
			client.AddParam("category",
					getIntent().getStringExtra(Category.CATEGORY_KEY));
			client.Execute(RequestMethod.GET);
			String response = client.getResponse();
			Log.d("SHOP", response);
			jsonArray = new JSONArray(response);
			Log.d("SHOPJSON", jsonArray.get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			Log.e("src", src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Log.e("Bitmap", "returned");
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Exception", e.getMessage());
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
<<<<<<< HEAD
		//camera = Camera.open();
		String cameraType = getIntent().getStringExtra(Category.CATEGORY_KEY);
		if("front".equals(cameraTypeMapping.get(cameraType))){
			openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
		} else {
			openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
		}
=======
		// TODO: open frontcam for shades and backcam for shoes and watches
		camera = Camera.open();
>>>>>>> Temp changes
	}
	
	public void openCamera(int cameraType){
	    int cameraCount = 0;
	    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	    cameraCount = Camera.getNumberOfCameras();
	    for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
	        Camera.getCameraInfo(camIdx, cameraInfo);
	        if (cameraInfo.facing == cameraType) {
	            try {
	                this.camera = Camera.open(camIdx);
	            } catch (RuntimeException e) {
	                Log.e("FlipAR", "Camera failed to open: " + e.getLocalizedMessage());
	            }
	        }
	    }
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.previous:
			if (jsonArray != null) {
				if (currentProductIndex > 0) {
					currentProductIndex -= 1;
					displayProduct();
				}
			}
			break;
		case R.id.next:
			if (jsonArray != null) {
				if (currentProductIndex < (jsonArray.length() - 1)) {
					currentProductIndex += 1;
					displayProduct();
				}
			}
			break;

		case R.id.add_to_cart:
			if (jsonArray != null) {
				if (currentProductIndex < (jsonArray.length() - 1)) {
					currentProductIndex += 1;
					displayProduct();
				} else if (currentProductIndex > 0) {
					currentProductIndex -= 1;
					displayProduct();
				}
				try {
					if (fkApp.productTitles.contains(((JSONObject) jsonArray
							.get(currentProductIndex)).getString("title")) == false) {
						fkApp.productUrls.add(((JSONObject) jsonArray
								.get(currentProductIndex)).getString("url"));
						fkApp.productPrices.add(((JSONObject) jsonArray
								.get(currentProductIndex)).getString("price"));
						fkApp.productTitles.add(((JSONObject) jsonArray
								.get(currentProductIndex)).getString("title"));
						Log.d("Product count", fkApp.productUrls.size() + "");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		case R.id.checkout:
			Intent intent = new Intent(this, Checkout.class);
			startActivity(intent);
			break;

		}
	}
}
