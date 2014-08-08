package com.fk.arapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Checkout extends ListActivity {

	CustomAdapter mAdapter; // Custom Adapter
	ArrayList<Data> mList;
	FkApp fkApp;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		for (int i = 0; i < fkApp.productUrls.size(); i++) {
			mList.add(new Data(fkApp.productTitles.get(i), fkApp.productPrices.get(i), fkApp.productUrls.get(i)));
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fkApp = (FkApp) getApplicationContext();

		setContentView(R.layout.list);
		mList = new ArrayList<Data>();

		mAdapter = new CustomAdapter(getApplicationContext(), // instiating the
																// object
				R.layout.single_item, mList);
		setListAdapter(mAdapter);
	}

	private class CustomAdapter extends ArrayAdapter<Data> {

		ArrayList<Data> items;

		public CustomAdapter(Context context, int textViewResourceId,
				List<Data> objects) {
			super(context, textViewResourceId, objects);
			this.items = (ArrayList<Data>) objects;
		}

		/*
		 * function for getting the view and binding the data to items
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.single_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.description = (TextView) view
						.findViewById(R.id.description);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag(); // getting the tag
			}

			Data singleItem = items.get(position);

			if (singleItem != null) { // binding the values
				holder.title.setText(singleItem.title);
				holder.description.setText(singleItem.description);
				holder.image.setImageBitmap(singleItem.bitMap);
			}

			return view;
		}

		class ViewHolder { // creating a ViewHolder
			TextView title;
			TextView description;
			ImageView image;
		}

	}

	class Data {
		String title;
		String description;
		String url;
		Bitmap bitMap = null;

		/**
		 * @param title
		 * @param description
		 * @param string
		 * @param bitMap
		 */
		Data(String title, String description, String string) {
			this.title = title;
			this.description = description;
			this.url = string;
			this.bitMap = fkApp.cachedImagesByUrl.get(url);
		}
	}
}
