package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

EditText cityName;
TextView textView;
public void findWeather(View view){
    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
    DownloadTasks tasks = new DownloadTasks();
    tasks.execute("https://api.weatherbit.io/v2.0/current?city="+ cityName.getText().toString()+"&key=e38a9013b6974fdf80ddffa0bdf7543b");
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.editText);
        textView = findViewById(R.id.textView2);




    }
    public class DownloadTasks extends AsyncTask<String, Void ,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!= -1){
                    char current = (char) data;
                    result +=current;
                    data = reader.read();
                }
                return result;

            }
            catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            Pattern p = Pattern.compile("temp\"(.*?),\"");
            Matcher m = p.matcher(result);
            String message= null;
            while (m.find()){
                String temp = m.group(1).replace("}]", "");
                textView.setText("Temperature"+ temp + "°C"+"\n");
            }
            Log.i("website content" , result);



        }
    }
}
