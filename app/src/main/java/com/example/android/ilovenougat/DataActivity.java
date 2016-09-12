package com.example.android.ilovenougat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    public static final String LOG_TAG = DataActivity.class.getName();
    private DataAdapter Adapter;
    public String zbrandName,zdprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        final Button b = (Button) findViewById(R.id.id_button);
        final EditText et = (EditText) findViewById(R.id.id_edittext);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String txt = et.getText().toString();
                if (txt.length() != 0) {
                    String URL_SITE = "https://api.zappos.com/Search?term=" + txt + "&key=b743e26728e16b81da139182bb2094357c31d331";
                    DataAsyncTask task = new DataAsyncTask();
                    task.execute(URL_SITE);
                } else {
                    String URL_SITE = "https://api.zappos.com/Search?term=nike&key=b743e26728e16b81da139182bb2094357c31d331";
                    DataAsyncTask task = new DataAsyncTask();
                    task.execute(URL_SITE);
                }

            }
        });
        ListView earthquakeListView = (ListView) findViewById(R.id.id_list);
        Adapter = new DataAdapter(this, new ArrayList<Data>());
        earthquakeListView.setAdapter(Adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Data currentData = Adapter.getItem(position);
                zbrandName = currentData.getProductName();
                zdprice = currentData.getDprice();
                String new_url = "https://api.6pm.com/Search?term=" + zbrandName + "&key=524f01b7e2906210f7bb61dcbe1bfea26eb722eb";
                DataAsyncTask2 task = new DataAsyncTask2();
                task.execute(new_url);
            }
        });
}

    private void updateUi(final Data sixdata) {
        StringBuilder a=new StringBuilder(sixdata.getDprice());
        StringBuilder b=new StringBuilder(zdprice);
        a.deleteCharAt(0);
        b.deleteCharAt(0);
        String zapposprice=a.toString();
        final String sixPmprice=b.toString();

        if(Float.compare(Float.parseFloat(zapposprice),Float.parseFloat(sixPmprice)) < 0  && (new String(sixdata.getBrandName()).equals(zbrandName))) {
            setContentView(R.layout.ok);
            TextView txtview=(TextView)findViewById(R.id.id_oktxtview);
            txtview.setText("Congrats! The item is cheaper at 6pm: "+ sixdata.getDprice()+".");
            new DownloadImageTask((ImageView) findViewById(R.id.id_okimageview))
                    .execute(sixdata.getIurl());
            String message="Hey checkout this product at 6pm!" + sixdata.getIurl();
            Button button=(Button)findViewById(R.id.id_insidebutton);
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent=new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    String message="Brand name: "+sixdata.getProductName()+System.lineSeparator()+"Price: "+sixPmprice+ System.lineSeparator()+sixdata.getIurl();
                    intent.putExtra(Intent.EXTRA_TEXT,"Check out this cool item at 6pm at Discounted Price!/n");
                    intent.putExtra(Intent.EXTRA_TEXT,message);
                    startActivity(intent);
                }
            });
        }
    }

    private class DataAsyncTask extends AsyncTask<String, Void, List<Data>>{
        @Override
        protected List<Data> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Data> result = zjson.zdata(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Data> data){
            Adapter.clear();
            if (data != null && !data.isEmpty()) {
                Adapter.addAll(data);
            }
        }
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

    private class DataAsyncTask2 extends AsyncTask<String, Void, Data>{
        @Override
        protected Data doInBackground(String... urls) {
            if(urls.length<1 || urls[0] == null){
                return null;
            }
            Data data = sjson.sdata(urls[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Data result){
            if(result == null){
                return;
            }
            updateUi(result);
        }
    }
}