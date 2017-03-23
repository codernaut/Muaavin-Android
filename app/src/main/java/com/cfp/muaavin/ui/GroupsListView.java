package com.cfp.muaavin.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.cfp.muaavin.adapter.GroupsAdapter;
import com.cfp.muaavin.facebook.Group;
import java.util.ArrayList;

public class GroupsListView extends Fragment {

    Context context;
    ArrayList Groups;
    ListView GroupsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        //super.onCreate(savedInstanceState);
        ///////////////////
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //////////////////
        //setContentView(R.layout.groups_list_view);
        View view  = inflater.inflate(R.layout.groups_list_view, container, false);
        context = getActivity();
        GroupsListView = (ListView) view.findViewById(R.id.GroupListView);
        Groups = (ArrayList<Group>)getArguments().getSerializable("Groups");
        GroupsAdapter c = new GroupsAdapter(Groups,context,getActivity());
        GroupsListView.setAdapter(c);
        return view;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_quote:
                // TODO put your code here to respond to the button tap
                LogOut();
                Intent intent = new Intent(GroupsListView.this,FacebookLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            default:  return super.onOptionsItemSelected(item);
        }
    }

    */
}
