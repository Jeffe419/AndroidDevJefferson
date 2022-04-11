package com.example.atigah_jefferson_s1918454;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapFragment extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final OkHttpClient client = new  OkHttpClient();


    private Marker currentMarker;


    ArrayList<item> a_items = new ArrayList<>();
    private GoogleMap mMap;
    ArrayList<LatLng> passed_items = new ArrayList<LatLng>();

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }




    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        getItems();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.fragment_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        for (int i = 0; i < a_items.size() ; i++) {

            String[] points = a_items.get(i).getGeorss_point().split(" ");
            LatLng point = new LatLng (Double.parseDouble(points[0]) , Double.parseDouble(points[1]));


            mMap.addMarker(new MarkerOptions().position(point).title(a_items.get(i).getTitle().toString()+"\n"+a_items.get(i).getDescription().toString())).setTag(0);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point,10);
            mMap.animateCamera(cameraUpdate);
            mMap.setOnMarkerClickListener(this);


        }


    }





    void getItems() {
        Request request = new Request.Builder()
                .url("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: "+Thread.currentThread().getId());

                if (response.isSuccessful()) {

                    item items = new item();
                    try {

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser =factory.newPullParser();

                        parser.setInput(response.body().byteStream(),"UTF-8");

                        int eventType = parser.getEventType();

                        while (eventType != XmlPullParser.END_DOCUMENT){

                            if (eventType ==XmlPullParser.START_TAG){

                                if (parser.getName().equals("item")){
                                    items = new item();
                                }else if(parser.getName().equals("title")){
                                    items.setTitle(parser.nextText());

                                }else if(parser.getName().equals("description")){
                                    items.setDescription(parser.nextText());
                                }
                                else if(parser.getName().equals("link")){
                                    items.setLink(parser.nextText());
                                }
                                else if(parser.getName().equals("georss:point")){
                                    items.setGeorss_point(parser.nextText());
                                }

                                else if(parser.getName().equals("author")){
                                    items.setAuthor(parser.nextText());
                                }

                                else if(parser.getName().equals("comments")){
                                    items.setComments(parser.nextText());
                                }

                                else if(parser.getName().equals("pubDate")){
                                    items.setPubDate(parser.nextText());
                                }

                                try {
                                    if (parser.getName().equals("item")){
                                        a_items.add(items);

                                    }

                                }catch (Exception e){
                                    Log.d(TAG, "onResponse: "+"Failed to Add to ArrayList");
                                }

                            }

                            eventType=parser.next();
                        }





                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }



                }


            }
        });

    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        String title = null;
        String Description = null;



        Integer clickCount = (Integer) marker.getTag();
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);

            for (int i = 0; i < a_items.size() ; i++) {
                if(marker.getTitle().equals(a_items.get(i).getTitle()+"\n"+ a_items.get(i).getDescription().toString())) {
                    title = a_items.get(i).getTitle().toString();
                    Description = a_items.get(i).getDescription().toString();
                }

            }


                String nDescription = Description.replace("<br />", "\n");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("More Information")
                    .setMessage(title.toString())

                    .setMessage(nDescription)

                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();



        }

        return false;
    }
}

