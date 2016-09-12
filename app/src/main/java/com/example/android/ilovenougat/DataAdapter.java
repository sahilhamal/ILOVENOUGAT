package com.example.android.ilovenougat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends ArrayAdapter<Data> {

    private static final String LOG_TAG = DataAdapter.class.getSimpleName();

    public DataAdapter(Activity context, ArrayList<Data> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Data currentData = getItem(position);

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.id_magnitude);
        magnitudeTextView.setText("Brand: " + currentData.getBrandName());

        TextView cityTextView = (TextView) listItemView.findViewById(R.id.id_cityname);
        cityTextView.setText("Original Price: " + currentData.getPrice());

        TextView dpricetxtview = (TextView) listItemView.findViewById(R.id.id_dprice);
        dpricetxtview.setText("Discounted Price: " + currentData.getDprice());

        TextView percentTextView = (TextView) listItemView.findViewById(R.id.id_dprice);
        percentTextView.setText("Percent Off: " + currentData.getDiscount());

        ImageView imageview=(ImageView) listItemView.findViewById(R.id.id_imageview);

        new DownloadImageTask((ImageView) listItemView.findViewById(R.id.id_imageview))
                .execute(currentData.getIurl());

        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

