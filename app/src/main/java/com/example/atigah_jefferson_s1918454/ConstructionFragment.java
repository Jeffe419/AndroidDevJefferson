package com.example.atigah_jefferson_s1918454;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

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

public class ConstructionFragment extends AppCompatActivity implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateSetListener{
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<item> a_items = new ArrayList<>();
    ArrayList<String> s_items;
    private Object AdapterView;
    ArrayAdapter<String> nadapter;
    Boolean done = false;
    @Nullable
    protected  void onCreate(Bundle savedInstanceState) {
        getItems();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.construction_fragment);


        ListView listView = (ListView) findViewById(R.id.consListview);

        while (done == false) {

            System.out.println("Not Done Parsing");

        }

        if (done) {

            s_items = new ArrayList<>();

            for (int i = 0; i < a_items.size(); i++) {
                s_items.add(a_items.get(i).getTitle() + "\n" + a_items.get(i).getDescription().replace("<br />", " "));

            }

            nadapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1,
                    s_items);
            listView.setAdapter(nadapter);
        }











            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int i, long l) {


                for (int j = 0; j < s_items.size(); j++) {
                    String nDescription = a_items.get(i).getDescription().toString().replace("<br />", "\n");
                    if (i == j) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("More Information")

                                .setMessage(a_items.get(i).getTitle())
                                .setMessage(nDescription+"\n")

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

                }




            }
        });  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int i, long l) {


                for (int j = 0; j < s_items.size(); j++) {
                    String nDescription = a_items.get(i).getDescription().toString().replace("<br />", "\n");
                    if (i == j) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("More Information")

                                .setMessage(a_items.get(i).getTitle())
                                .setMessage(nDescription+"\n")

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

                }




            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.Search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                nadapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

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
                Log.d(TAG, "onResponse: " + Thread.currentThread().getId());

                if (response.isSuccessful()) {

                    item items = new item();
                    try {

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = factory.newPullParser();

                        parser.setInput(response.body().byteStream(), "UTF-8");

                        int eventType = parser.getEventType();

                        while (eventType != XmlPullParser.END_DOCUMENT) {

                            if (eventType == XmlPullParser.START_TAG) {

                                if (parser.getName().equals("item")) {
                                    items = new item();
                                } else if (parser.getName().equals("title")) {
                                    items.setTitle(parser.nextText());

                                } else if (parser.getName().equals("description")) {
                                    items.setDescription(parser.nextText());
                                } else if (parser.getName().equals("link")) {
                                    items.setLink(parser.nextText());
                                } else if (parser.getName().equals("georss:point")) {
                                    items.setGeorss_point(parser.nextText());
                                } else if (parser.getName().equals("author")) {
                                    items.setAuthor(parser.nextText());
                                } else if (parser.getName().equals("comments")) {
                                    items.setComments(parser.nextText());
                                } else if (parser.getName().equals("pubDate")) {
                                    items.setPubDate(parser.nextText());
                                }

                                try {
                                    if (parser.getName().equals("item")) {
                                        a_items.add(items);

                                    }

                                } catch (Exception e) {
                                    Log.d(TAG, "onResponse: " + "Failed to Add to ArrayList");
                                }

                            }

                            eventType = parser.next();
                        }
                        done = true;


                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }


                }


            }
        });

    }



    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }



}
