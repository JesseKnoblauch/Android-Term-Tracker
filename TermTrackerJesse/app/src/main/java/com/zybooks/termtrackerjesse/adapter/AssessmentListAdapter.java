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
import com.zybooks.termtrackerjesse.models.Course;

import java.util.ArrayList;

public class AssessmentListAdapter extends ArrayAdapter<Assessment> {
    private static final String TAG = "AssessmentListAdapter";
    private Context mContext;
    int mResource;

    public AssessmentListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Assessment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String date = getItem(position).getDate();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView titleText = (TextView) convertView.findViewById(R.id.title);
        TextView titleContent = (TextView) convertView.findViewById(R.id.titleContent);
        TextView dateText = (TextView) convertView.findViewById(R.id.date);
        TextView dateContent = (TextView) convertView.findViewById(R.id.dateContent);

        titleText.setText("Title: ");
        titleContent.setText(title);
        dateText.setText("Date: ");
        dateContent.setText(date);

        return convertView;
    }
}
