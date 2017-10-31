package com.hearound.hearound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewPost extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    // TODO: set url
    private final String API_URL = "http://18.216.21.133/api";
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                JSONObject json = getJSONBody();
                if (json.length() == 0) {
                    return;
                }

                try {
                    APIConnection api = new APIConnection();
                    //API_URL + "/posts"
                    api.post(API_URL + "/posts", json, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            final String error = e.toString();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("**** post ****", error);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // Don't need to do anything with the response at this point
                            return;
                        }
                    });
                } catch (Exception e) {
                    Log.e("**** onClick ****", "error with POST: " + e);
                }

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }

        });

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    JSONObject getJSONBody() {
        JSONObject json = new JSONObject();

        EditText titleField = (EditText)findViewById(R.id.title);
        EditText postField = (EditText)findViewById(R.id.postBody);
        EditText latField = (EditText)findViewById(R.id.latitude);
        EditText lngField = (EditText)findViewById(R.id.longitude);

        try {
            json.put("title", titleField.getText().toString());
            json.put("body", postField.getText().toString());
            json.put("lat", latField.getText().toString());
            json.put("lng", lngField.getText().toString());
        } catch (Exception e) {
            // TODO: add user dialogue
            Log.e("**** getJSONBody ****", "empty text box: " + e);
        }

        Log.d("$$$$$$$$$ ", json.toString());
        return json;
    }
}
