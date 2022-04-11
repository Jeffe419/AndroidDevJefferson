package com.example.atigah_jefferson_s1918454;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateSetListener {
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<item> a_items = new ArrayList<>();
    private Button searchButton;
    private TextView txtDate;
    ArrayList<String> s_items;
    private Object AdapterView;
    Button datebutton;
    String  nCurrentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getItems();


        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ListView listView = view.findViewById(R.id.itemlist);

        searchButton = view.findViewById(R.id.btnSearch);
        txtDate = view.findViewById(R.id.btnSearch);
        datebutton = view.findViewById(R.id.btnDate);




      datebutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              showDatePicker();


          }
      });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

             if (nCurrentDate != null) {
                 s_items = new ArrayList<>();
                 String newDate[];
                 String curDate = nCurrentDate;
                 for (int i = 0; i < a_items.size(); i++) {
                     newDate = a_items.get(i).getDescription().split(" ");
                     String compare = newDate[2] + " " + newDate[4] + " " + newDate[3] + ", " + newDate[5];

                     if (curDate.equals(compare)) {
                         s_items.add(a_items.get(i).getTitle() + "\n" + a_items.get(i).getDescription().replace("<br />", " "));
                     }

                 }
                 ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, s_items);
                 listView.setAdapter(adapter);

                 System.out.println(curDate);

             }

                if(s_items==null){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Alert")
                            .setMessage("No incidents on selected date or please select a date")

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
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int i, long l) {


                for (int j = 0; j < s_items.size(); j++) {
                    String nDescription = a_items.get(i).getDescription().toString().replace("<br />", "\n");
                    if (i == j) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


        return view;

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


                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }


                }


            }
        });

    }


    @Override
    public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int i, long l) {
    }





    public void showDatePicker() {
       DatePickerDialog datePickerDialog = new DatePickerDialog(
               this.getContext(),
               this,
               Calendar.getInstance().get(Calendar.YEAR),
               Calendar.getInstance().get(Calendar.MONTH),
               Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
       );

       datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal2 = Calendar.getInstance();
       cal2.set(Calendar.YEAR,i );
       cal2.set(Calendar.MONTH, i1);
       cal2.set(Calendar.DAY_OF_MONTH,i2);
        nCurrentDate = DateFormat.getDateInstance(DateFormat.FULL).format(cal2.getTime());
  System.out.println(nCurrentDate);
    }
}
