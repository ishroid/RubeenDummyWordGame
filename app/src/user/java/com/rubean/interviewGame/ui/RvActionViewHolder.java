package com.rubean.interviewGame.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rubean.interviewGame.R;
import com.rubean.interviewGame.models.MoveActionModel;

class RvActionViewHolder extends RecyclerView.ViewHolder{
    private final TextView actionOwner;
    private final TextView tvActionText;

    public RvActionViewHolder(@NonNull View itemView) {
        super(itemView);
        actionOwner = itemView.findViewById(R.id.tvUserOrBot);
        tvActionText = itemView.findViewById(R.id.tvActionText);
    }

    public void bindData(MoveActionModel item){
        actionOwner.setText(item.actionOwner);
        tvActionText.setText(item.actionText);
    }

    static RvActionViewHolder from(ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rowView = layoutInflater.inflate(R.layout.item_actions,parent,false);
        return new RvActionViewHolder(rowView);
    }
}