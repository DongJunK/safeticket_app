package com.example.safeticket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.UserInfoViewHolder> {
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener = null;


    private String name;
    private ArrayList<UserInfo> dataList;

    public UserInfoAdapter(ArrayList<UserInfo> data, String name)
    {
        this.dataList = data;
        this.name = name;
    }

    public class UserInfoViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        protected TextView typeText;
        protected TextView idText;
        protected TextView issueText;

        public UserInfoViewHolder(View view)
        {
            super(view);
            mView = view;
            typeText = (TextView) view.findViewById(R.id.typeText);
            idText = (TextView) view.findViewById(R.id.idText);
            issueText = (TextView) view.findViewById(R.id.issueText);
        }
    }

    @NonNull
    @Override
    public UserInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        UserInfoViewHolder viewHolder = new UserInfoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserInfoViewHolder horizontalViewHolder, final int position)
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

        horizontalViewHolder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, UserActivity.class);
                UserInfo userInfo = dataList.get(position);
                intent.putExtra("infoType", userInfo.getInfoType());
                intent.putExtra("issueNumber", userInfo.getIssueNumber());
                intent.putExtra("issuer", userInfo.getIssuer());
                intent.putExtra("issueDate", userInfo.getIssueDate());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}