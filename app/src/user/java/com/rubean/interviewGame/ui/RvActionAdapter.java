package com.rubean.interviewGame.ui;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.rubean.interviewGame.models.MoveActionModel;

public class RvActionAdapter extends ListAdapter<MoveActionModel,RvActionViewHolder>{

    public RvActionAdapter(){
        super(RvActionAdapter.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RvActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RvActionViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RvActionViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    public static final DiffUtil.ItemCallback<MoveActionModel> DIFF_CALLBACK =new DiffUtil.ItemCallback<MoveActionModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MoveActionModel oldItem, @NonNull MoveActionModel newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MoveActionModel oldItem, @NonNull MoveActionModel newItem) {
            return oldItem.actionText.equals(newItem.actionText);
        }
    };
}

