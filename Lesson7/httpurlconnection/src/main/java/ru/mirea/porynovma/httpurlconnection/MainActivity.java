package ru.mirea.porynovma.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.porynovma.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

    }

    public void onClick(View v) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService((Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadPageTask().execute("https://ipinfo.io/json");
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bind.ipView.setText("Загрузка...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                bind.ipView.setText("IP: " + responseJson.getString("ip"));
                bind.cityView.setText("Город: " + responseJson.getString("city"));
                bind.regionView.setText("Регион: " + responseJson.getString("region"));
                bind.countryView.setText("Страна: " + responseJson.getString("country"));
                String[] geo = responseJson.getString("loc").split(",");
                new GetWeatherTask().execute(
                        "https://api.open-meteo.com/v1/forecast?latitude=" +
                                geo[0] +
                                "&longitude=" +
                                geo[1] +
                                "&current_weather=true"
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }


    private class GetWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bind.weatherView.setText("Погода: Загрузка...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d(MainActivity.class.getSimpleName(), urls[0]);
                return downloadInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);
                JSONObject units = responseJson.getJSONObject("current_weather_units");
                JSONObject values = responseJson.getJSONObject("current_weather");
                String temperature = values.getString("temperature") + " " +
                        units.getString("temperature");
                String windSpeed = values.getString("windspeed") + " " +
                        units.getString("windspeed");
                String windDirection = values.getString("winddirection") +
                        units.getString("winddirection");
                bind.weatherView.setText(
                        "Погода: " +
                                "\nТемпература: " + temperature +
                                "\nСкорость ветра: " + windSpeed +
                                "\nНаправление ветра: " + windDirection                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    private String downloadInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}