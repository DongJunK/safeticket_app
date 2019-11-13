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

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private ArrayList<Ticket> ticketList;

    public TicketAdapter(ArrayList<Ticket> ticketList)
    {
        this.ticketList = ticketList;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        protected TextView ticketNameText;
        protected TextView eventDateText;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            ticketNameText = (TextView) itemView.findViewById(R.id.ticketNameText);
            eventDateText = (TextView) itemView.findViewById(R.id.eventDateText);
        }
    }

    @NonNull
    @Override
    public TicketAdapter.TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false);

        TicketAdapter.TicketViewHolder viewHolder = new TicketAdapter.TicketViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TicketAdapter.TicketViewHolder ticketViewHolder, final int position)
    {
        ticketViewHolder
                .ticketNameText
                .setText(ticketList.get(position).getEventName());
        ticketViewHolder
                .eventDateText
                .setText(ticketList.get(position).getEventDate());

        ticketViewHolder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TicketActivity.class);

                intent.putExtra("TicketCode", ticketList.get(position).getTicketCode());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }
}
