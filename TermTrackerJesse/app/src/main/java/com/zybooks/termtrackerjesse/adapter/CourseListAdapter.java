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
import com.zybooks.termtrackerjesse.models.Course;
import com.zybooks.termtrackerjesse.models.Term;

import java.util.ArrayList;

public class CourseListAdapter extends ArrayAdapter<Course> {
    private static final String TAG = "CourseListAdapter";
    private Context mContext;
    int mResource;

    public CourseListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Course> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String start = getItem(position).getStartDate();
        String end = getItem(position).getEndDate();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView titleText = (TextView) convertView.findViewById(R.id.title);
        TextView titleContent = (TextView) convertView.findViewById(R.id.titleContent);
        TextView dateText = (TextView) convertView.findViewById(R.id.date);
        TextView dateContent = (TextView) convertView.findViewById(R.id.dateContent);

        titleText.setText("Title: ");
        titleContent.setText(title);
        dateText.setText("Course: ");
        dateContent.setText(start + " to " + end);

        return convertView;
    }
}
