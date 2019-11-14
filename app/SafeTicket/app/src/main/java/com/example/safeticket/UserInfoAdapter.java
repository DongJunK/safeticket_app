package com.example.safeticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.UserInfoViewHolder> {
    private Activity activity;
    private String name;
    private ArrayList<UserInfo> userInfoList;

    public UserInfoAdapter(ArrayList<UserInfo> userInfoList, String name, Activity activity)
    {
        this.userInfoList = userInfoList;
        this.name = name;
        this.activity = activity;
    }

    public class UserInfoViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        protected TextView typeText;
        protected TextView idText;
        protected TextView issueText;
        protected ImageView imageIcon;
        protected LinearLayout userInfoLayout;

        public UserInfoViewHolder(View view)
        {
            super(view);
            mView = view;
            typeText = (TextView) view.findViewById(R.id.typeText);
            idText = (TextView) view.findViewById(R.id.idText);
            issueText = (TextView) view.findViewById(R.id.issueText);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            userInfoLayout = (LinearLayout) view.findViewById(R.id.userInfoLayout);
        }
    }

    @NonNull
    @Override
    public UserInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.userinfo_item, parent, false);

        UserInfoViewHolder viewHolder = new UserInfoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserInfoViewHolder horizontalViewHolder, final int position)
    {
        String infoType= userInfoList.get(position).getInfoType();

        horizontalViewHolder
                .typeText
                .setText(infoType);
        horizontalViewHolder
                .idText
                .setText(name);
        horizontalViewHolder
                .issueText
                .setText(userInfoList.get(position).getIssueNumber());

        if(infoType.equals("운전면허증"))
        {
            horizontalViewHolder
                    .imageIcon
                    .setImageResource(R.drawable.car_ico);
            horizontalViewHolder
                    .userInfoLayout
                    .setBackgroundResource(R.drawable.circle_layout);
        }
        else if(infoType.equals("주민등록증"))
        {
            horizontalViewHolder
                    .imageIcon
                    .setImageResource(R.drawable.student_ico);
            horizontalViewHolder
                    .userInfoLayout
                    .setBackgroundResource(R.drawable.circle_layout_register);
            horizontalViewHolder
                    .typeText
                    .setTextColor(Color.BLACK);
            horizontalViewHolder
                    .idText
                    .setTextColor(Color.BLACK);
            horizontalViewHolder
                    .issueText
                    .setTextColor(Color.BLACK);
        }


        horizontalViewHolder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, UserActivity.class);
                UserInfo userInfo = userInfoList.get(position);
                intent.putExtra("infoType", userInfo.getInfoType());
                intent.putExtra("issueNumber", userInfo.getIssueNumber());
                intent.putExtra("issuer", userInfo.getIssuer());
                intent.putExtra("issueDate", userInfo.getIssueDate());

                context.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }
}