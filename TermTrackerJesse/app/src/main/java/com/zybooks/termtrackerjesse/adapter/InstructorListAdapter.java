package com.zybooks.termtrackerjesse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zybooks.termtrackerjesse.R;
import com.zybooks.termtrackerjesse.models.Assessment;
import com.zybooks.termtrackerjesse.models.Instructor;

import java.util.ArrayList;

public class InstructorListAdapter extends ArrayAdapter<Instructor> {
    private static final String TAG = "InstructorListAdapter";
    private Context mContext;
    int mResource;

    public InstructorListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Instructor> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String phone = getItem(position).getPhone();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView titleText = (TextView) convertView.findViewById(R.id.title);
        TextView titleContent = (TextView) convertView.findViewById(R.id.titleContent);
        TextView dateText = (TextView) convertView.findViewById(R.id.date);
        TextView dateContent = (TextView) convertView.findViewById(R.id.dateContent);

        titleText.setText("Name: ");
        titleContent.setText(name);
        dateText.setText("Phone: ");
        dateContent.setText(phone);

        return convertView;
    }
}
