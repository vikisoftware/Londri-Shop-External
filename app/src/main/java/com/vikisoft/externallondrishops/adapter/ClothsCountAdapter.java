package com.vikisoft.externallondrishops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.helper.UpdatePriceResponce;

import java.util.List;

public class ClothsCountAdapter extends RecyclerView.Adapter<ClothsCountAdapter.MyViewHolder> {

    private List<UpdatePriceResponce> clothsList;
    private Context context;
    private int clothType;


    public ClothsCountAdapter(List<UpdatePriceResponce> clothsList, Context context, int clothType) {
        this.clothsList = clothsList;
        this.context = context;
        this.clothType = clothType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cloth_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UpdatePriceResponce cloth = clothsList.get(position);

       if (cloth.getWearId() == clothType) {
           holder.add.setVisibility(View.INVISIBLE);
           holder.remove.setVisibility(View.INVISIBLE);


           Glide.with(context)
                   .load(cloth.getClothImage())
                   .thumbnail(0.5f)
                   .into(holder.clothImage);

           holder.clothName.setText(cloth.getClothName());
           holder.clothCount.setText(cloth.getClothCount().toString() +  " Piece");
       }
       else {
           holder.parentContainer.setVisibility(View.GONE);
       }
    }

    @Override
    public int getItemCount() {
        return clothsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView clothImage, add, remove;
        TextView clothName, clothCount;
        LinearLayout parentContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            clothImage = itemView.findViewById(R.id.clothImage);
            clothName = itemView.findViewById(R.id.clothName);
            clothCount = itemView.findViewById(R.id.count);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.minus);
            parentContainer = itemView.findViewById(R.id.selected_row);
        }
    }
}
