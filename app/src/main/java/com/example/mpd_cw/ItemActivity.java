//<!--Greg MacPherson S1509595-->
package com.example.mpd_cw;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ItemActivity extends AppCompatActivity implements View.OnClickListener{
    String geo;
    TextView tv_title;
    TextView tv_period;
    TextView tv_description;
    TextView tv_desc2;
    TextView tv_desc3;
    TextView tv_duration;
    TextView tv_geo;
    String type;


    Button mapBtn;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        tv_title = findViewById(R.id.itemTitle);
        tv_description = findViewById(R.id.itemD1);
        tv_geo = findViewById(R.id.itemGeo);
        tv_desc3= findViewById(R.id.itemD3);
        tv_desc2 = findViewById(R.id.itemD2);

        tv_duration = findViewById(R.id.itemDuration);
        tv_period = findViewById(R.id.itemPeriod);
        mapBtn = (Button) findViewById(R.id.mapBtn);
            mapBtn.setOnClickListener(this);



        String title = intent.getExtras().getString("title");
        String period = intent.getExtras().getString("period");
        String dur = intent.getExtras().getString("dur");
        String d = intent.getExtras().getString("desc");
        Boolean incident = intent.getExtras().getBoolean("inc");
        String description = "";
        String desc2 = "";
        String desc3 = "";
        Log.e("MyTag","title "+title);
        Log.e("MyTag","desc "+d);
        Log.e("MyTag","desc "+d);

        if (incident == true){type = "i";}
        else{
            if(d.contains("Works:")){
                type = "works";
            }if(d.contains("TYPE :")){
                type = "type";
            }if(d.contains("Delay Information")){
                type = "delay";
            }
        }

        String desc[];
        switch (type) {
            case "works":
                desc = d.split("Works:|Traffic Management:|Diversion Information:");
                    Log.e("MyTag","desc[0] -> "+desc[1]);
                description = "<b>Works:</b> "+desc[1];
                if (desc.length > 2){
                    Log.e("MyTag","desc[1] -> "+desc[2]);
                    desc2 = "<b>Traffic management:</b> "+desc[2];}
                if (desc.length > 3){
                    Log.e("MyTag","desc[2] -> "+desc[3]);
                    desc3 = "<b>Diversion Information:</b> "+desc[3];}
                break;

            case "type":
                desc = d.split("TYPE :|Lane Closures :|Location :");
                description = "<b>Type:</b> "+desc[1];
                if (desc.length > 2){
                    Log.e("MyTag","desc[2] -> "+desc[2]);
                    desc2 = "<b>Location:</b> "+desc[2]; }
                if (desc.length > 3){
                    Log.e("MyTag","desc[3] -> "+desc[3]);
                    desc3 = "<b>Lane CLosures:</b> "+desc[3]; }
                break;
            case "delay":
                desc = d.split("Delay Information");
                description = "<b>Delay Information</b>"+desc[1];
                break;
            case "i":
                description = "<b>Description: "+d;
                break;
        }
        geo = intent.getExtras().getString("geo");

        Log.e("MyTag","intent: "+intent);
        Log.e("MyTag"," "+title);
        Log.e("MyTag","--");
        tv_title.setText(title);
        tv_description.setText(Html.fromHtml(description));
        tv_geo.setText(geo);

        if (incident == false) {
        String col = intent.getExtras().getString("col");

        tv_desc2.setText(Html.fromHtml(desc2));
        tv_desc3.setText(Html.fromHtml(desc3));
            tv_period.setText(period);
            tv_duration.setText(dur);
            tv_duration.setBackgroundColor(Color.parseColor(col));
        }

    }

    public void onClick(View view) {
        Intent i = new Intent(ItemActivity.this, Map.class);
        i.putExtra("geo", geo);

        ItemActivity.this.startActivity(i);
    }
}

