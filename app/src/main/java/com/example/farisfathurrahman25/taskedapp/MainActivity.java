package com.example.farisfathurrahman25.taskedapp;

/*
Reference(s):
- http://androidmkab.com/2016/12/13/how-to-parse-json/
- https://github.com/frsfth25/AndroidWeek10AsyncRest
- https://github.com/frsfth25/KODCU (private repository)
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";

    private EditText inputText;
    private Button searchButton;
    ArrayList<Book> bookList;
    BookAdapter bookAdapter;
    private ListView lvItems;
    private String inputURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.editText);
        searchButton = findViewById(R.id.button);
        lvItems = findViewById(R.id.list_item);

    }

    public void buttonClicked(View view) {

        if (inputText.getText().toString() == "") {
            Toast.makeText(this, "Search Query is Empty!", Toast.LENGTH_SHORT);
            return;
        }

        inputURL = "https://www.googleapis.com/books/v1/volumes?q=%7B";
        String tmpInput = inputText.getText().toString();
        tmpInput = tmpInput.replaceAll(" ", "_");

        inputURL += tmpInput;

 /*       Ion.with(this)
                .load(inputURL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i(TAG, result.toString());
                        parseResult(result);
                    }
                }); */

/*        //inputURL += URLEncoder.encode(addr, "UTF-8");
        URL url = new URL(inputURL);

        // read from the URL
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();
*/
        bookList = new ArrayList<>();

        new JSONAsyncTask().execute(inputURL);

        bookAdapter = new BookAdapter(bookList, getApplicationContext());
        lvItems.setAdapter(bookAdapter);

 /*       try {
            JSONObject obj = new JSONObject(str);
            //String pageName = obj.getJSONObject("pageInfo").getString("pageName");

            JSONArray arr = obj.getJSONArray("items");
            for (int i = 0; i < arr.length(); i++)
            {
                String title = arr.getJSONObject(i).getString("title");

                String authors = "";
                JSONArray arr2 = obj.getJSONArray("authors");
                for (int j=0; j<arr2.length(); j++)
                {
                    if (j!=arr2.length()-1)
                        authors += (arr2.getString(j) + ",");
                    else
                        authors += (arr2.getString(j) + ",");
                }

                result.add(new Book(title, "noImage", authors, 3));
            }

            itemCompletionResult(result);
        } catch (JSONException e)
        {
            e.printStackTrace();
        } */


    }

    private void parseResult(JSONObject obj) {

    }

    private void parseResult(JsonObject obj) {

        bookList = new ArrayList<>();

        try {
            JSONArray allBooks = new JSONArray(obj);
            for (int i = 0; i < allBooks.length(); i++) {
                JSONObject bookObject = allBooks.getJSONObject(i);
                //result.add(new Book(bookObject.getString("")))
            }

            itemCompletionResult(bookList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String result = obj.getAsJsonObject("value").get("joke").getAsString();
        //TextView t = findViewById(R.id.editText);
        //t.setText(result);


    }

    public void itemCompletionResult(ArrayList<Book> result) {
        if (result != null) {
            bookAdapter = new BookAdapter(result, getApplicationContext());
            lvItems.setAdapter(bookAdapter);
        } else {
            Toast.makeText(this, "No Result Found!", Toast.LENGTH_SHORT);
        }

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

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        JSONObject volumeInfo = object.getJSONObject("volumeInfo");
                        Book book = new Book();

                        Log.i(TAG, volumeInfo.getString("title"));

                        //getting json object values from json array
                        book.setTitle(volumeInfo.getString("title"));
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


}
