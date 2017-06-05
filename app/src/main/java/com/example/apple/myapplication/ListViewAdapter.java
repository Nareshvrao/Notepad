package com.example.apple.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.apple.myapplication.activity.NoteActivity;
import com.example.apple.myapplication.util.DatabaseHandler;
import com.example.apple.myapplication.util.NoteEntitity;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter   {

    private final Context mContext;

    List<NoteEntitity> mDataset;

    DatabaseHandler db;

    // List<NoteEntitity> noteEntitities;

    Activity activity;

    public ListViewAdapter(Context context, Activity activity) {
        mContext = context;
     //   this.mDataset = mDataset;
        db = new DatabaseHandler(context);
        mDataset = db.getAllNotes();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public String getItem(int position) {
        return mDataset.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Log.v("CHECK" , " getview 1 ");

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.item_cell);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mDataset.get(position).getTitle());
        viewHolder.time.setText(mDataset.get(position).getTime());
        viewHolder.text.setText(mDataset.get(position).getText());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext.getApplicationContext(), NoteActivity.class);

                intent.putExtra("title", mDataset.get(position).getTitle());

                intent.putExtra("note", mDataset.get(position).getText());

                intent.putExtra("ID", String.valueOf(mDataset.get(position).getID()));

                intent.putExtra("image_url", String.valueOf(mDataset.get(position).getImageUrl()));

                mContext.startActivity(intent);

                activity.finish();

            }
        });


        return convertView;
    }


    private static class ViewHolder {

        public LinearLayout layout ;
        public TextView title;
        public TextView text;
        public TextView time;

    }
}/**/
