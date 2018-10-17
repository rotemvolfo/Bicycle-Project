package com.example.inbar.bicycle_client;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<TipObject> {

    private MapsActivity activity;
    private List<TipObject> tipsList;

    public ListViewAdapter(MapsActivity context, int resource, List<TipObject> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.tipsList = objects;
    }

    @Override
    public TipObject getItem(int position) {
        return tipsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tipName.setText(getItem(position).getName());
        holder.tipTitel.setText(getItem(position).getTitle());

        return convertView;
    }

    private static class ViewHolder {
        private TextView tipName;
        private TextView tipTitel;

        public ViewHolder(View view) {
            tipName = (TextView) view.findViewById(R.id.textName);
            tipTitel = (TextView) view.findViewById(R.id.textTitel);
        }
    }
}