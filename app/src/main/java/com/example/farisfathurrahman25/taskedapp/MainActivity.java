package com.example.farisfathurrahman25.taskedapp;

/*
Reference(s):
- http://androidmkab.com/2016/12/13/how-to-parse-json/
- https://github.com/frsfth25/AndroidWeek10AsyncRest
- https://github.com/frsfth25/KODCU (private repository)
 */

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";

    private EditText inputText;
    private Button searchButton;
    ArrayList<Book> bookList;
    BookAdapter bookAdapter;
    private ListView lvItems;
    private String inputURL = "";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.editText);
        searchButton = findViewById(R.id.button);
        lvItems = findViewById(R.id.list_item);
    }

    public void buttonClicked(View view) {

        if (inputText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Search Query is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        inputURL = "https://www.googleapis.com/books/v1/volumes?q=%7B";
        String tmpInput = inputText.getText().toString().trim();
        tmpInput = tmpInput.replaceAll(" ", "_");

        Log.d(TAG, tmpInput);

        inputURL += tmpInput;

        bookList = new ArrayList<>();

        new JSONAsyncTask().execute(inputURL);

        bookAdapter = new BookAdapter(bookList, getApplicationContext());
        lvItems.setAdapter(bookAdapter);
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//for displaying progress bar
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
//establishing http connection
                //------------------>>
                HttpGet post = new HttpGet(urls[0]);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);

                Log.i(TAG, urls[0]);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                // if connected then access data
                if (status == 200) {

                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonO = new JSONObject(data);
                    JSONArray jArray = jsonO.getJSONArray("items");

                    Log.i(TAG, "" + jArray.length());

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        JSONObject volumeInfo = object.getJSONObject("volumeInfo");
                        Book book = new Book();

                        Log.i(TAG, volumeInfo.getString("title"));

                        //getting json object values from json array
                        book.setTitle(volumeInfo.getString("title"));

                        if (volumeInfo.has("authors")) {
                            JSONArray autArr = volumeInfo.getJSONArray("authors");
                            String author = autArr.getString(0);

                            Log.i(TAG, author);

                            book.setAuthor(author);
                        }


                        try {
                            //URL uri = new URL(volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));
                            //ImageDownloader downloader = new ImageDownloader();
                            Log.i(TAG, volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));
                            //downloader.execute(uri);

                            book.setImageURL(volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));
                        } catch (Exception e) {
                            Log.e(TAG, "Error on URL:" + e.getLocalizedMessage());
                        }

                        if (volumeInfo.has("averageRating")) {
                            book.setRating(Float.parseFloat(volumeInfo.getString("averageRating")));
                        } else {

                        }

                        //rev.setComment(object.getString("comment"));
                        //rev.setUsefulness(object.getString("usefulness"));

                        //getting value within json object
//                        JSONObject numStar = object.optJSONObject("ratings");
//                        String sta = numStar.getString("Overall");
                        //rev.setStars(sta);

                        //adding data to the arrayList
                        bookList.add(book);

                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException | JSONException | IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            // if data didn't get to be fetched from the url
            dialog.cancel();
            bookAdapter.notifyDataSetChanged();
            if (!result)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

    private class ImageDownloader extends AsyncTask<URL, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            Log.i(TAG, "onPreExecute is called.");
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Image is being downloaded...");
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            Log.i(TAG, "doInBackground is called.");
            Log.i(TAG, "User URL param count =" + urls.length);
            URL currentURL = urls[0];
            Bitmap resultingBitmap;

            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    publishProgress(i + 1);
                }

                //now download image
                URLConnection connection = currentURL.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                if (is != null) {
                    resultingBitmap = BitmapFactory.decodeStream(is);
                    return resultingBitmap;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            Log.i(TAG, "onProgressUpdate is called.");
            int currentVal = values[0];
            progressDialog.setProgress(currentVal * 10);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //super.onPostExecute(bitmap);
            Log.i(TAG, "onPostExecute is called.");
            ImageView imgView = findViewById(R.id.thumbnailView);
            if (bitmap != null) {
                imgView.setImageBitmap(bitmap);
            } else {
                Log.i(TAG, "There is no image on the URL.");
            }

            progressDialog.hide();

        }
    }
}
