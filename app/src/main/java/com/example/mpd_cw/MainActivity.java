//<!--Greg MacPherson S1509595-->
package com.example.mpd_cw;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

//import java.util.concurrent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView itemsTitle;

    public ArrayAdapter adapter;

    private TextView roadworks;
    private TextView planned;
    private String result = "" ;
    private Button startIncidentsButton;
    private Button startPlannedButton;
    private Button startRoadworksButton;
    private Button loadButton;
    // Traffic Scotland URLs
    private String roadworksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private ArrayList<TrafficItem> itemArrayList;

    private ListView lv;
    private EditText search;

    private String titlesP[] = {};
    private String titlesI[] = {};
    private String titlesR[] = {};
    private ArrayList<TrafficItem> itemsI;
    private ArrayList<TrafficItem> itemsR;
    private ArrayList<TrafficItem> itemsP;

    private ProgressBar spinner;
    private String blankTitles[] = {};
    private ArrayList<TrafficItem> test = new ArrayList<>();
    TrafficItem t = new TrafficItem();
    private myAdapter adapterino;


//    ArrayList<String> blankTitles = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        blankTitles.add("");
        test.add(t);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsTitle = (TextView) findViewById(R.id.itemsTitle);

        loadButton = (Button) findViewById(R.id.load);
        loadButton.setOnClickListener(this);

        startIncidentsButton = (Button) findViewById(R.id.incidentsButton);
        startIncidentsButton.setOnClickListener(this);
//        roadworks = (TextView) findViewById(R.id.roadworks);
        startRoadworksButton = (Button) findViewById(R.id.roadworksButton);
        startRoadworksButton.setOnClickListener(this);
//        lv = (ListView) findViewById(R.id.incidentList);
//        planned = (TextView) findViewById(R.id.planned);
        startPlannedButton = (Button) findViewById(R.id.plannedButton);
        startPlannedButton.setOnClickListener(this);
//        lv = (ListView) findViewById(R.id.incidentList);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);


        lv = (ListView) findViewById(R.id.itemsList);
            search = (EditText) findViewById(R.id.filter);
                search.setVisibility(View.INVISIBLE);



//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, blankTitles);
        adapterino = new myAdapter(this, test);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    adapterino.resetData();
                }
                adapterino.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.d("MyTag", "*** Search value changed: " + s.toString());
//                adapterino.getFilter().filter(s.toString());
            }
        }
        );

    }


    public void clear(){
        Log.e("MyTag","clear i:" + titlesI.length);
        Log.e("MyTag","clear p:" + titlesP.length);
        Log.e("MyTag","clear r:" + titlesR.length);

        String blank[] = {};
        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                blank);
        lv.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        search.setVisibility(View.INVISIBLE);

    }


    public void setAdapter(String type){
        ArrayAdapter<String> adapter;
        myAdapter ad;
//        Log.e("MyTag","setAdapt: blanktitles:" + blankTitles.toString());
        switch(type) {
            case "incident":
                Log.e("MyTag","items i : "+itemsI);
//                this.adapter = new ArrayAdapter<String>(
                adapterino = new myAdapter(
                        MainActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        android.R.id.text1,
                        itemsI);
                lv.setAdapter(adapterino);

                break;
            case "roadworks":
                Log.e("MyTag","items r : "+itemsR);
//                adapter = new ArrayAdapter<String>(
                adapterino = new myAdapter(
                        MainActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        android.R.id.text1,
                        itemsR);
                lv.setAdapter(adapterino);
//                lv.setAdapter(adapter);
                break;
            case "planned":
                Log.e("MyTag","items P : "+itemsP);
//                adapter = new ArrayAdapter<>(
                adapterino = new myAdapter(
                        MainActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        android.R.id.text1,
                        itemsP);
                lv.setAdapter(adapterino);
                break;
        }
    }


    public void onClick(View view) {


    String type;
        Log.e("MyTag","Pressed a button...");

        switch(view.getId()){
            case R.id.incidentsButton:
                type = "incident";
                Log.e("MyTag","Button clicked:"+type);
                itemsTitle.setText("Incidents:");
//                Log.e("MyTag","TitlesI: "+titlesI[0]);
                if (titlesI.length != 0){
                    Log.e("MyTag","incident itles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(incidentsURL, type);
                }
                break;
            case R.id.roadworksButton:
                type = "roadworks";
                Log.e("MyTag","Button clicked:"+type);
                itemsTitle.setText("Roadworks:");
                Log.e("MyTag","TitlesI: "+titlesR.toString());
                if (titlesR.length != 0){
                    Log.e("MyTag","roadworks titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(roadworksURL, type);
                }
                break;
            case R.id.plannedButton:
                type = "planned";
                Log.e("MyTag","Button clicked:"+type);
                itemsTitle.setText("Planned Roadworks:");
                Log.e("MyTag","TitlesI: "+titlesP.toString());
                if (titlesP.length != 0){
                    Log.e("MyTag","planned titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(plannedURL, type);
                    }
                break;

            case R.id.load:
                startPlannedButton.setVisibility((startPlannedButton.getVisibility() == View.VISIBLE)
                        ? View.GONE
                        : View.VISIBLE);
                startRoadworksButton.setVisibility((startRoadworksButton.getVisibility() == View.VISIBLE)
                        ? View.GONE
                        : View.VISIBLE);
                startIncidentsButton.setVisibility((startIncidentsButton.getVisibility() == View.VISIBLE)
                        ? View.GONE
                        : View.VISIBLE);
        }

//        int id = aview.getId();
//        Log.e("MyTag", aview.toString());
//        Log.e("MyTag", id);
//        Log.e("MyTag", Integer.toString(id));
//        2131165258

    }

    public void startProgress(String url, String type)
    {
        // Run network access on a separate thread;
        String[] urlAndType = {url, type};
        Log.e("MyTag","startprogress... url: "+url + " type: "+type);

        new downloadTask().execute(urlAndType);


    } //

    public ArrayList handleResult(String data){
        Log.e("MyTag", "handleresult data: " + data);

        ParseIncident p = new ParseIncident();
        p.parse(data);

//        Log.e("MyTag", "handelresult itemArraylist size: " + itemArrayList.size());
//        Log.e("MyTag", "handelresult itemArraylist size: " + itemArrayList.size());

        return p.getApplications();
    }

    private class downloadTask extends AsyncTask<String[], Void, ArrayList<TrafficItem>> {
//        @Override
//        public void run()
        String tType;

        @Override
        public ArrayList<TrafficItem> doInBackground(String[]... params) {
            tType = params[0][1];
//            String url = params[0][0];
//            String type = params[0][1];
            Log.e("MyTag","In background, params: "+ params[0][0]+tType);
            return getData(params[0][0]);
        }

        @Override
        protected void onPostExecute(ArrayList<TrafficItem> result) {
            super.onPostExecute(result);
//            itemArrayList = result;
            spinner.setVisibility(View.GONE);
            if(result.get(0).getTitle() == "No Incidents Found") {
            }else{
                search.setVisibility(View.VISIBLE);
            }

            setAdapter(tType);
        }

        private ArrayList<TrafficItem> getData(String url)
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag","GETTING DATA FROM " + url);
            Log.e("MyTag","in run");

            try{
                result = "";
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","GETTING DATA FROMd " + url);

                Log.e("MyTag", "result:");
                while ((inputLine = in.readLine()) != null){
//                for (int i = 0; i < 15; i++){
                    result = result + inputLine;
//                    Log.e("MyTag",inputLine);
                }
                Log.d("MyTag", "result done : " + result);
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it

            ArrayList<TrafficItem> items= handleResult(result);
            Log.d("Post handle result ", "items: "+ items.size());
//            setTitle(result);

            String titles[];
            int i = 0;
            titles = new String [items.size()];
            for (TrafficItem o : items){
//                titles[i] = o.getTitle();
                titles[i] = o.getTitle();
                i++;
            }

            if (items.size() == 0) {
                TrafficItem neu = new TrafficItem();
                neu.setTitle("No Incidents Found");
                neu.setDesc("None");
                neu.setPubDate("Mon, 01 Jan 2020 00:00:00 GMT");
                neu.setLink("www yahoo");
                neu.setInc(true);
                items.add(neu);
            }

            switch(tType) {
                case "incident":
                    titlesI = titles;
                    itemsI = items;
                    break;
                case "roadworks":
                    titlesR = titles;
                    itemsR = items;
                    break;
                case "planned":
                    titlesP = titles;
                    itemsP = items;
                    break;
            }
            return items;
        }
    }

}