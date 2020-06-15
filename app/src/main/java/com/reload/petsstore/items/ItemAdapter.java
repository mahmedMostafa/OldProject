package com.reload.petsstore.items;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reload.petsstore.R;
import com.reload.petsstore.databinding.SubCatItemBinding;
import com.reload.petsstore.itemdetails.ItemDetailsActivity;
import com.reload.petsstore.items.Model.ItemResult;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    ArrayList<ItemResult> mList;
    Context mContext;
    public changeFav mInterface;
    SubCatItemBinding binding;

    public interface changeFav {
        void change(int hasFav, String ItemId);
    }

    ;

    public ItemAdapter(ArrayList<ItemResult> mList, Context mContext, changeFav mInterface) {
        this.mInterface = mInterface;
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding =SubCatItemBinding.inflate(inflater,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.MyViewHolder holder, final int position) {
        binding.itemName.setText(mList.get(position).getName());
        Glide.with(mContext).load(mList.get(position).getImage()).into(binding.itemImg);

        final boolean hasFav = mList.get(position).getHasFavorite();
        if (hasFav) {
            binding.fav.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            binding.fav.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        binding.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFav) {
                    mInterface.change(0, mList.get(position).getId());
                    binding.fav.setImageDrawable(mContext.getResources()
                            .getDrawable(R.drawable.ic_favorite_border_black_24dp));
                } else {
                    mInterface.change(1, mList.get(position).getId());
                    binding.fav.setImageDrawable(mContext.getResources()
                            .getDrawable(R.drawable.ic_favorite_black_24dp));

                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class)
                        .putExtra("itemId", mList.get(position).getId())
                        .putExtra("itemName", mList.get(position).getName()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SubCatItemBinding binding;

        public MyViewHolder(SubCatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
