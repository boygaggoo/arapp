package com.fk.arapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Home extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindCategories();
    }


    private void bindCategories() {
		View catBtn = findViewById(R.id.cat_watches_button);
		catBtn.setOnClickListener(this);
		catBtn = findViewById(R.id.cat_shades_button);
		catBtn.setOnClickListener(this);
		catBtn = findViewById(R.id.cat_shades_button);
		catBtn.setOnClickListener(this);
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
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.cat_watches_button:
			openCategory(Category.CAT_WATCHES);
			break;
		case R.id.cat_shades_button:
			openCategory(Category.CAT_SHADES);
			break;
		case R.id.cat_shoes_button:
			openCategory(Category.CAT_SHOES);
			break;
		}
	}


	private void openCategory(String categoryName) {
		Intent intent = new Intent(this, Listing.class);
		intent.putExtra(Category.CATEGORY_KEY, categoryName);
		startActivity(intent);
	}
}
