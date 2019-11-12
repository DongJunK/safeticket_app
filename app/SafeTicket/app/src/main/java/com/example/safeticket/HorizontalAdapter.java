package com.example.safeticket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {
    private String name;
    private ArrayList<UserInfo> dataList;

    public HorizontalAdapter(ArrayList<UserInfo> data, String name)
    {
        this.dataList = data;
        this.name = name;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        protected TextView typeText;
        protected TextView idText;
        protected TextView issueText;

        public HorizontalViewHolder(View view)
        {
            super(view);
            typeText = (TextView) view.findViewById(R.id.typeText);
            idText = (TextView) view.findViewById(R.id.idText);
            issueText = (TextView) view.findViewById(R.id.issueText);
        }
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_item, null);

        return new HorizontalAdapter.HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder horizontalViewHolder, int position)
    {
        horizontalViewHolder
                .typeText
                .setText(dataList.get(position).getInfoType());
        horizontalViewHolder
                .idText
                .setText(name);
        horizontalViewHolder
                .issueText
                .setText(dataList.get(position).getIssueNumber());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}