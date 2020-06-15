package com.reload.petsstore.homecategory.homeFragment;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reload.petsstore.R;
import com.reload.petsstore.databinding.HomeCatItemBinding;
import com.reload.petsstore.items.ItemsActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    ArrayList<HomeCatResult> mList;
    Context mContext;

    public HomeAdapter(ArrayList<HomeCatResult> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        HomeCatItemBinding binding = DataBindingUtil.inflate(inflater,R.layout.home_cat_item,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //Glide.with(mContext).load(mList.get(position).getImage()).into(holder.mCatImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext , ItemsActivity.class)
                .putExtra("CatID" , mList.get(position).getId())
                .putExtra("catName" , mList.get(position).getName()));
            }
        });
    }

    public void setList(ArrayList<HomeCatResult> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

       HomeCatItemBinding binding;

        public MyViewHolder(HomeCatItemBinding binding) {
            super(binding.getRoot());
           this.binding = binding;

        }
    }
}
