package com.reload.petsstore.fav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reload.petsstore.R;
import com.reload.petsstore.items.ItemAdapter;
import com.reload.petsstore.items.Model.ItemResult;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    ArrayList<ItemResult> mList;
    Context mContext;

    public FavAdapter(ArrayList<ItemResult> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FavAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_item, parent, false);
        FavAdapter.MyViewHolder myViewHolder = new FavAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.MyViewHolder holder, int position) {
        final boolean hasFav = mList.get(position).getHasFavorite();
        if (hasFav) {
            holder.mFavImg.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            holder.mFavImg.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        holder.mItemTitle.setText(mList.get(position).getName());
        Glide.with(mContext).load(mList.get(position).getImage()).into(holder.mItemImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mItemTitle;
        ImageView mItemImage , mFavImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemTitle = itemView.findViewById(R.id.item_name);
            mItemImage = itemView.findViewById(R.id.item_img);
            mFavImg = itemView.findViewById(R.id.fav_item);
        }
    }
}
