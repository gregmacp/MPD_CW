//<!--Greg MacPherson S1509595-->


package com.example.mpd_cw;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class myAdapter extends ArrayAdapter<TrafficItem> implements Filterable {
    public ArrayList<TrafficItem> origTraffItems;
    public ArrayList<TrafficItem> traffItems;
    private Filter filter;
    private Context conx;
    private Boolean inc = false;


    private static LayoutInflater inflater = null;

    public myAdapter (Context context, ArrayList<TrafficItem> _items) {
        super(context, 0, _items);
        conx = context;
        this.origTraffItems = _items;
        this.traffItems = _items;

    }


    @Override
    public TrafficItem getItem(int position) {
        return traffItems.get(position);
    }

    @Override
    public int getCount() {
        return traffItems.size();
    }

    @Override
    public long getItemId(int position) {
        return traffItems.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TrafficItem it = getItem(position);

            ItemHolder holder = new ItemHolder();

            if (convertView == null) {
                Log.d("MyTag", "No Filter");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_list_view, parent, false);
                TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
                TextView tvPeriod = (TextView) convertView.findViewById(R.id.tvPeriod);
                LinearLayout LlClick= (LinearLayout) convertView.findViewById(R.id.LlClick);

                holder.clickable = LlClick  ;
                holder.itemTitleView = tvName;
                holder.itemPeriodView = tvPeriod;
//                holder.dateView= tvDate;

                tvName.setText(it.getTitle());
                tvPeriod.setText(it.getPeriod());


                inc = it.getInc();
                if (inc == false){
                TextView tvDuration= (TextView) convertView.findViewById(R.id.tvDuration);
                tvDuration.setText(Long.toString(it.getDuration())+" hrs");
                    tvDuration.setBackgroundColor(Color.parseColor(it.getColour()));
                holder.itemDurationView = tvDuration;
                }
                convertView.setTag(holder);
            }else{
                holder = (ItemHolder) convertView.getTag();
            }

            final TrafficItem t = traffItems.get(position);
                    Log.e("MyTag","TRAFFIC ITEM! -"+t);

           TextView tv =  holder.itemTitleView;
                   tv.setText(t.getTitle());
            if (inc == false) {
                holder.itemPeriodView.setText(t.getPeriod());
                holder.itemDurationView.setText(Long.toString(t.getDuration()) + " hrs");
                holder.itemDurationView.setBackgroundColor(Color.parseColor(it.getColour()));
            }else {
                holder.itemPeriodView.setText("");
            }
            holder.clickable.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Log.e("MyTag","clicked! ");
                    Intent i = new Intent(conx, ItemActivity.class);

                    i.putExtra("title", t.getTitle());
                    i.putExtra("desc", t.getDescr());
                    i.putExtra("inc", t.getInc());
                    i.putExtra("pub", t.getPubDate());
                    i.putExtra("geo", t.getGeoRss());

                    if(inc == false) {
                        i.putExtra("dur", Long.toString(t.getDuration()) + " hrs");
                        i.putExtra("period", t.getPeriod2());
                        i.putExtra("col", t.getColour());
                    }
                    conx.startActivity(i);
                }
            });
            return convertView;
    }


    public void resetData() {
        traffItems = origTraffItems;
    }

    private static class ItemHolder {
        public TextView itemTitleView;
        public TextView itemPeriodView;
        public TextView itemDurationView;
        public LinearLayout clickable;
    }

    @Override
    public Filter getFilter() {

        Log.e("MyTag"," ** FILTER ** ");
        if (filter == null)
            filter = new myFilter();
            Log.d("MyTag"," ** ");
        Log.d("MyTag"," * ");

        return filter;

    }

private class myFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Log.e("MyTag"," *PERFOMING FILTERING* ");
        FilterResults results = new FilterResults();
//        Log.e("MyTag"," 0.results.count "+results.count);

        // We implement here the filter logic
        if (constraint == null || constraint.length() == 0) {
            Log.e("MyTag"," *NO CONSTRAINTS* ");
            // No filter implemented we return all the list

            ArrayList<TrafficItem> ar = new ArrayList<TrafficItem>(origTraffItems);

            results.values = ar;
            results.count = ar.size();

//            Log.e("MyTag"," 1.results.count "+results.count);
//            Log.e("MyTag"," ");
        }
        else {
            Log.e("MyTag"," CONSTRAINTS = "+constraint);
            // We perform filtering operation
            ArrayList<TrafficItem> nItemList = new ArrayList<TrafficItem>();
//            Log.e("MyTag"," 3. results.count "+results.count);
//            Log.e("MyTag"," 3. nitem  " +nItemList.size());
            for (TrafficItem p : origTraffItems) {
                if ((p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) | (p.getPeriod().toUpperCase().contains(constraint.toString().toUpperCase()))) {
                        Log.e("MyTag","match: "+p.toString());
                        nItemList.add(p);
                }
            }
        Log.e("MyTag"," 2. nitem  " +nItemList.size());
            results.values = nItemList;
            results.count = nItemList.size();
        }
//        Log.e("MyTag"," 2. results.count "+results.count);
//            Log.e("MyTag"," ");



        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint,
                                  FilterResults results) {
        if (results.count == 0)
            notifyDataSetInvalidated();
        else {
            traffItems = (ArrayList<TrafficItem>) results.values;
            notifyDataSetChanged();
        }
    }
}

}




