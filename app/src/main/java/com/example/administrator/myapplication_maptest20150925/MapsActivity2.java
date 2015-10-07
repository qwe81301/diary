package com.example.administrator.myapplication_maptest20150925;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity2 extends FragmentActivity {

    static final LatLng NUU = new LatLng(24.545001 , 120.812032);
    LatLng SPOT2 = new LatLng(24.8063383,120.9930696);
    LatLng SPOT3 = new LatLng(24.9033703,121.2689264);
    LatLng SPOT4 = new LatLng(25.05716,121.5078186);

        /*
        24.545001, 120.812032 聯大
        24.8063383,120.9930696 新竹世博台灣館
        24.9033703,121.2689264 桃園大溪小小兵彩繪牆
        25.05716,121.5078186  台北大稻埕
        */

    GoogleMap map;
    ArrayList<LatLng> markerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();
        Marker nuu = map.addMarker(new MarkerOptions().position(NUU).title("聯合大學").snippet(""));
        Marker spot2 = map.addMarker(new MarkerOptions().position(SPOT2).title("新竹世博台灣館").snippet(""));
        Marker spot3 = map.addMarker(new MarkerOptions().position(SPOT3).title("桃園大溪小小兵彩繪牆").snippet(""));
        Marker spot4 = map.addMarker(new MarkerOptions().position(SPOT4).title("台北大稻埕").snippet(""));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NUU, 16));

//            if (map != null) {
//
//                // Enable MyLocation Button in the Map
//                map.setMyLocationEnabled(true);
//
//                // Setting onclick event listener for the map
//                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//                    @Override
//                    public void onMapClick(LatLng point) {
//
//                        // Already two locations
//                        if (markerPoints.size() > 1) {
//                            markerPoints.clear();
//                            map.clear();
//                        }
//
//                        // Adding new item to the ArrayList
//                        markerPoints.add(point);
//
//                        // Creating MarkerOptions
//                        MarkerOptions options = new MarkerOptions();
//
//                        // Setting the position of the marker
//                        options.position(point);
//
//                        /**
//                         * 起始及終點位置符號顏色
//                         */
//                        if (markerPoints.size() == 1) {
//                            options.icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); //起點符號顏色
//                        } else if (markerPoints.size() == 2) {
//                            options.icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)); //終點符號顏色
//                        }
//
//                        // Add new marker to the Google Map Android API V2
//                        map.addMarker(options);
//
//                        // Checks, whether start and end locations are captured
//                        if (markerPoints.size() >= 2) {
//                            LatLng origin = markerPoints.get(0);
//                            LatLng dest = markerPoints.get(1);
//
//                            // Getting URL to the Google Directions API
//                            String url = getDirectionsUrl(origin, dest);
//
//
//                            DownloadTask downloadTask = new DownloadTask();
//
//                            // Start downloading json data from Google Directions
//                            // API
//                            downloadTask.execute(url);
//                        }
//
//                    }
//                });
//            }
        String url = getDirectionsUrl(24.545001 , 120.812032 , 24.8063383,120.9930696);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

        String url2 = getDirectionsUrl(24.8063383,120.9930696 , 24.9033703,121.2689264);
        DownloadTask downloadTask2 = new DownloadTask();
        downloadTask2.execute(url2);

        String url3 = getDirectionsUrl(24.9033703,121.2689264 , 25.05716,121.5078186);
        DownloadTask downloadTask3 = new DownloadTask();
        downloadTask3.execute(url3);




    }



    private String getDirectionsUrl(double orlatitude, double orlongitude,
                                    double delatitude, double delongitude) {

        // Origin of route
        String str_origin = "origin=" + orlatitude + "," + orlongitude;

        // Destination of route
        String str_dest = "destination=" + delatitude + "," + delongitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + "mode=walking";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;

        Log.v("tt",url);
        // 這個是 聯大到新竹世博館 mode是walking
        //https://maps.googleapis.com/maps/api/directions/json?origin=24.545001 , 120.812032&destination=24.8063383,120.9930696&mode=walking
        return url;
    }

    /**從URL下載JSON資料的方法**/
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** 解析JSON格式 **/
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);  //導航路徑寬度
                lineOptions.color(Color.rgb(5,85,245)); //導航路徑顏色

            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }
}