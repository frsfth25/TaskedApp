package com.example.farisfathurrahman25.taskedapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";

    private EditText inputText;
    private Button searchButton;

    private String inputURL = "https://www.googleapis.com/books/v1/volumes?q=%7B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String tmpInput = inputText.getText().toString();
        tmpInput = tmpInput.replaceAll(" ", "_");

        inputURL += tmpInput;

        Ion.with(this)
                .load(inputURL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i(TAG, result.toString());
                        parseResult(result);

                    }
                });

    }

    private void parseResult(JsonObject obj) {
        try {
            JSONArray allBooks = new JSONArray(obj);
            for (int i = 0; i < allBooks.length(); i++) {
                JSONObject bookObject = allBooks.getJSONObject(i);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String result = obj.getAsJsonObject("value").get("joke").getAsString();
        //TextView t = findViewById(R.id.editText);
        //t.setText(result);


    }
}
