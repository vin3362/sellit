package com.sellit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.code.linkedinapi.schema.Person;
import com.sellit.R;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: vinodh
 * Date: 2/20/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkedinAdapter extends ArrayAdapter<Person> {
    private final Context context;
    private ArrayList<Person> items;

    public LinkedinAdapter(Context context, int textViewResourceId, ArrayList<Person> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.li_row_layout, null);
        }
        Person liPerson = items.get(position);
        if (liPerson != null) {
            TextView name = (TextView) v.findViewById(R.id.label);
            TextView headLineText = (TextView) v.findViewById(R.id.headline);
            if (name != null) {
                name.setText(liPerson.getFirstName() + " " + liPerson.getLastName());
            }
            if(headLineText!=null && liPerson.getHeadline()!=null){
                headLineText.setText(liPerson.getHeadline());
            }
        }
        return v;
    }
}

