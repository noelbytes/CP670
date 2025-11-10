package com.example.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_forecast);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // small toast and log so we can confirm the activity launched when debugging
        Toast.makeText(this, "WeatherForecast opened", Toast.LENGTH_SHORT).show();
        Log.i("WeatherForecast", "onCreate: activity started");

        // Show the horizontal progress bar
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        // Spinner: start fetch when selection changes
        Spinner spinner = findViewById(R.id.citySpinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String city = (String) parent.getItemAtPosition(position);
                    if (city != null && !city.isEmpty()) {
                        // start the ForecastQuery with the selected city
                        new ForecastQuery(WeatherForecast.this).execute(city);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // no-op
                }
            });
        }

        // Optionally start the ForecastQuery to fetch data immediately for first item
        if (spinner != null && spinner.getCount() > 0) {
            String initialCity = (String) spinner.getItemAtPosition(0);
            if (initialCity != null && !initialCity.isEmpty()) {
                new ForecastQuery(this).execute(initialCity);
            }
        }
    }

    // Static inner AsyncTask to fetch forecast data (min/max/current temps and an image)
    // Made static and using a WeakReference to the activity to avoid leaking the Activity.
    private static class ForecastQuery extends AsyncTask<String, Integer, String> {
        // Temperature values
        String minTemp;
        String maxTemp;
        String currentTemp;

        // Bitmap for the current weather image
        Bitmap weatherBitmap;

        // store the parsed icon code so onPostExecute can use a fallback mapping if needed
        String iconCodeParsed;

        // WeakReference to activity to update the UI safely
        private final WeakReference<WeatherForecast> activityRef;

        ForecastQuery(WeatherForecast activity) {
            activityRef = new WeakReference<>(activity);
        }

        private boolean fileExistance(String fname) {
            WeatherForecast activity = activityRef.get();
            if (activity == null) return false;
            File file = new File(activity.getFilesDir(), fname);
            boolean exists = file.exists();
            Log.i("ForecastQuery", "Checking local file: " + fname + " exists=" + exists + " path=" + file.getAbsolutePath());
            return exists;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WeatherForecast activity = activityRef.get();
            if (activity != null) {
                // optionally show a progress indicator in the activity
                ProgressBar pb = activity.findViewById(R.id.progressBar);
                if (pb != null) pb.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String cityParam = "ottawa"; // default
            if (params != null && params.length > 0 && params[0] != null) {
                cityParam = params[0];
            }

            try {
                // URL-encode the city name and append ",ca"
                String cityEncoded = Uri.encode(cityParam);
                String apiKey = "6e5980feecbeecd36a2b2a2efd77eeb0";
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + cityEncoded + ",ca&APPID=" + apiKey + "&mode=xml&units=metric";

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                int response = conn.getResponseCode();
                if (response != HttpURLConnection.HTTP_OK) {
                    Log.i("ForecastQuery", "HTTP response code: " + response);
                    return "HTTP_ERROR_" + response;
                }

                InputStream in = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(in, null);

                int eventType = parser.getEventType();
                String iconCode = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String name = parser.getName();
                        if ("temperature".equals(name)) {
                            String value = parser.getAttributeValue(null, "value");
                            // publish progress after retrieving value
                            if (value != null) {
                                currentTemp = value;
                                publishProgress(25);
                            }
                            String min = parser.getAttributeValue(null, "min");
                            // publish progress after retrieving min
                            if (min != null) {
                                minTemp = min;
                                publishProgress(50);
                            }
                            String max = parser.getAttributeValue(null, "max");
                            // publish progress after retrieving max
                            if (max != null) {
                                maxTemp = max;
                                publishProgress(75);
                            }
                            Log.i("ForecastQuery", "Parsed temps value=" + value + " min=" + min + " max=" + max);
                        } else if ("weather".equals(name)) {
                            iconCode = parser.getAttributeValue(null, "icon");
                            Log.i("ForecastQuery", "Parsed weather icon=" + iconCode);
                        }
                    }
                    eventType = parser.next();
                }

                // save parsed icon for later
                iconCodeParsed = iconCode;

                // If we found an icon code, try to load it from internal storage or download and save it
                if (iconCode != null) {
                    WeatherForecast activity = activityRef.get();
                    Context ctx = null;
                    if (activity != null) ctx = activity.getApplicationContext();
                    String iconFileName = iconCode + "@2x.png"; // cache the higher-res variant
                    Log.i("ForecastQuery", "Looking for local icon file: " + iconFileName);

                    if (fileExistance(iconFileName)) {
                        // load from local storage
                        FileInputStream fis = null;
                        BufferedInputStream bis = null;
                        try {
                            if (ctx != null) {
                                fis = ctx.openFileInput(iconFileName);
                                bis = new BufferedInputStream(fis);
                                weatherBitmap = BitmapFactory.decodeStream(bis);
                                Log.i("ForecastQuery", "Loaded icon from local storage: " + iconFileName);
                            }
                        } catch (FileNotFoundException e) {
                            Log.i("ForecastQuery", "Local file not found when opening: " + iconFileName);
                        } finally {
                            if (bis != null) try { bis.close(); } catch (IOException ignored) {}
                            if (fis != null) try { fis.close(); } catch (IOException ignored) {}
                        }
                    } else {
                        // download and save
                        // Use HTTPS and request the higher-resolution icon (wn @2x) to reduce pixelation
                        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                        Log.i("ForecastQuery", "Downloading icon from: " + iconUrl);
                        InputStream iconStream = null;
                        BufferedInputStream bis = null;
                        HttpURLConnection iconConn = null;
                        try {
                            URL u = new URL(iconUrl);
                            iconConn = (HttpURLConnection) u.openConnection();
                            iconConn.setConnectTimeout(10000);
                            iconConn.setReadTimeout(10000);
                            iconConn.setDoInput(true);
                            iconConn.connect();
                            if (iconConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                iconStream = iconConn.getInputStream();
                                bis = new BufferedInputStream(iconStream);
                                Bitmap image = BitmapFactory.decodeStream(bis);
                                if (image != null) {
                                    weatherBitmap = image;
                                    if (ctx != null) {
                                        FileOutputStream fos = null;
                                        BufferedOutputStream bos = null;
                                        try {
                                            fos = ctx.openFileOutput(iconFileName, Context.MODE_PRIVATE);
                                            bos = new BufferedOutputStream(fos);
                                            image.compress(Bitmap.CompressFormat.PNG, 80, bos);
                                            bos.flush();
                                            Log.i("ForecastQuery", "Saved icon to local storage: " + iconFileName + " path=" + new File(ctx.getFilesDir(), iconFileName).getAbsolutePath());
                                        } catch (Exception e) {
                                            Log.i("ForecastQuery", "Failed to save icon locally: " + e.getMessage());
                                        } finally {
                                            if (bos != null) try { bos.close(); } catch (IOException ignored) {}
                                            if (fos != null) try { fos.close(); } catch (IOException ignored) {}
                                        }
                                    }
                                } else {
                                    Log.i("ForecastQuery", "Downloaded image was null for: " + iconUrl);
                                }
                            } else {
                                Log.i("ForecastQuery", "Icon HTTP response: " + iconConn.getResponseCode());
                            }
                        } catch (Exception e) {
                            Log.i("ForecastQuery", "Failed to download icon: " + e.getMessage());
                        } finally {
                            if (bis != null) try { bis.close(); } catch (IOException ignored) {}
                            if (iconStream != null) try { iconStream.close(); } catch (IOException ignored) {}
                            if (iconConn != null) iconConn.disconnect();
                        }
                    }

                    // indicate finished (or icon downloaded) progress
                    publishProgress(100);
                } else {
                    // No icon code found, still indicate completion
                    Log.i("ForecastQuery", "No icon code parsed from XML");
                    publishProgress(100);
                }

                return "OK";
            } catch (Exception e) {
                Log.e("ForecastQuery", "doInBackground error", e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            WeatherForecast activity = activityRef.get();
            if (activity != null) {
                // update progress bar or other UI elements
                ProgressBar pb = activity.findViewById(R.id.progressBar);
                if (pb != null && values != null && values.length > 0) {
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(values[0]);
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            WeatherForecast activity = activityRef.get();
            if (activity != null) {
                // apply results to UI (set TextViews, ImageView, etc.)
                ImageView imageView = activity.findViewById(R.id.weatherImage);
                TextView current = activity.findViewById(R.id.currentTemp);
                TextView min = activity.findViewById(R.id.minTemp);
                TextView max = activity.findViewById(R.id.maxTemp);
                ProgressBar pb = activity.findViewById(R.id.progressBar);

                // If we don't have a downloaded bitmap, try to use a local drawable fallback mapped from the icon code
                if (weatherBitmap == null && iconCodeParsed != null) {
                    int fallbackRes = R.drawable.ic_cloud;
                    if (iconCodeParsed.startsWith("01")) fallbackRes = R.drawable.ic_sun;
                    else if (iconCodeParsed.startsWith("02") || iconCodeParsed.startsWith("03") || iconCodeParsed.startsWith("04")) fallbackRes = R.drawable.ic_cloud;
                    else if (iconCodeParsed.startsWith("09") || iconCodeParsed.startsWith("10")) fallbackRes = R.drawable.ic_rain;
                    else if (iconCodeParsed.startsWith("11")) fallbackRes = R.drawable.ic_thunder;
                    else if (iconCodeParsed.startsWith("13")) fallbackRes = R.drawable.ic_snow;

                    try {
                        weatherBitmap = BitmapFactory.decodeResource(activity.getResources(), fallbackRes);
                        Log.i("ForecastQuery", "Using fallback drawable for icon code: " + iconCodeParsed);
                    } catch (Exception e) {
                        Log.i("ForecastQuery", "Failed to load fallback drawable: " + e.getMessage());
                    }
                }

                // Update image view if we have a bitmap
                if (weatherBitmap != null && imageView != null) {
                    try {
                        // Determine target pixel size: prefer the ImageView measured size if available
                        int ivWidth = imageView.getWidth();
                        int ivHeight = imageView.getHeight();

                        Runnable setScaled = () -> {
                            try {
                                int w = imageView.getWidth();
                                int h = imageView.getHeight();
                                int targetPx = (w > 0 && h > 0) ? Math.max(w, h) : (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, activity.getResources().getDisplayMetrics());

                                int bw = weatherBitmap.getWidth();
                                int bh = weatherBitmap.getHeight();
                                if (bw > 0 && bh > 0) {
                                    int newW = targetPx;
                                    int newH = Math.max(1, (int) ((float) bh * targetPx / bw));
                                    Bitmap scaled = Bitmap.createScaledBitmap(weatherBitmap, newW, newH, true);
                                    imageView.setImageBitmap(scaled);
                                } else {
                                    imageView.setImageBitmap(weatherBitmap);
                                }
                            } catch (Exception e) {
                                imageView.setImageBitmap(weatherBitmap);
                                Log.i("ForecastQuery", "Scaling (posted) failed, used original bitmap: " + e.getMessage());
                            }
                        };

                        if (ivWidth > 0 && ivHeight > 0) {
                            // already measured -> run immediately
                            setScaled.run();
                        } else {
                            // not measured yet -> post to run after layout
                            imageView.post(setScaled);
                        }

                        Log.i("ForecastQuery", "ImageView updated with scaled bitmap");
                    } catch (Exception e) {
                        // fallback to using the original bitmap if scaling fails
                        imageView.setImageBitmap(weatherBitmap);
                        Log.i("ForecastQuery", "Scaling failed, used original bitmap: " + e.getMessage());
                    }
                } else if (imageView != null) {
                    // fallback drawable from resources (keeps UI responsive and visible)
                    imageView.setImageResource(R.drawable.ic_cloud);
                    Log.i("ForecastQuery", "No bitmap available; set fallback drawable");
                } else {
                    Log.i("ForecastQuery", "No bitmap available to set in ImageView and imageView is null");
                }

                // Update temperature TextViews using the formatted string resources
                if (current != null) {
                    if (currentTemp != null) {
                        current.setText(activity.getString(R.string.temp_current_format, currentTemp));
                        Log.i("ForecastQuery", "Set current temperature: " + currentTemp);
                    } else {
                        Log.i("ForecastQuery", "Current temp is null; leaving default text");
                    }
                }

                if (min != null) {
                    if (minTemp != null) {
                        // format expects: value then prefix (see strings.xml), keep argument order correct
                        min.setText(activity.getString(R.string.temp_min_format, minTemp, activity.getString(R.string.temp_min_prefix)));
                        Log.i("ForecastQuery", "Set min temperature: " + minTemp);
                    } else {
                        Log.i("ForecastQuery", "Min temp is null; leaving default text");
                    }
                }

                if (max != null) {
                    if (maxTemp != null) {
                        // format expects: value then prefix
                        max.setText(activity.getString(R.string.temp_max_format, maxTemp, activity.getString(R.string.temp_max_prefix)));
                        Log.i("ForecastQuery", "Set max temperature: " + maxTemp);
                    } else {
                        Log.i("ForecastQuery", "Max temp is null; leaving default text");
                    }
                }

                // Hide the progress bar when done
                if (pb != null) {
                    pb.setVisibility(View.INVISIBLE);
                    Log.i("ForecastQuery", "ProgressBar set to INVISIBLE");
                }

                Log.i("ForecastQuery", "onPostExecute complete: current=" + currentTemp + " min=" + minTemp + " max=" + maxTemp + " icon=" + iconCodeParsed);
            }
        }
    }
}

