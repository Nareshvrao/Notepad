package com.example.apple.myapplication.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.example.apple.myapplication.util.DatabaseHandler;
import com.example.apple.myapplication.ListViewAdapter;
import com.example.apple.myapplication.util.NoteEntitity;
import com.example.apple.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    static HomeActivity homeActivity;
    static ArrayList<String> sub = new ArrayList<String>();
    Fragment fragment;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = this;

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("myFragmentTag");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new ListViewFragment();
            ft.add(android.R.id.content, fragment, "myFragmentTag");
            ft.commit();
        }

        db = new DatabaseHandler(this);

    }

    @Override
    public void onResume() {

        super.onResume();

       /* List<NoteEntitity> noteEntitities = db.getAllNotes();

        sub.clear();

        for (NoteEntitity cn : noteEntitities) {

            sub.add(cn.getTitle());

        }*/
    }




    @Override
    public void onBackPressed() {


    }


    public static class ListViewFragment extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_listview, container, false);

          //  Log.v("CHECK" , sub.get(0).toString());

            ListView list = (ListView) root.findViewById(android.R.id.list);

            ListViewAdapter listAdapter = new ListViewAdapter(getActivity(), homeActivity);

            list.setAdapter(listAdapter);


          FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(homeActivity, NoteActivity.class);

                    startActivity(intent);

                    homeActivity.finish();

                }
            });





            return root;
        }
    }


}